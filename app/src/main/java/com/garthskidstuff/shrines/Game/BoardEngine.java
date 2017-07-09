package com.garthskidstuff.shrines.Game;


import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Map of all Shrines in the board: Shrine -> connections + convenience functions
 */

class BoardEngine extends Board {
    // homeId --> (shrineId --> Shrine): Saves a shrine state at the "last seen" moment
    private final Map<Integer, Map<Integer, Shrine>> knownShrineStatesMap = new HashMap<>();

    private final Roller roller;

    BoardEngine(Roller randomRoller) {
        this.roller = randomRoller;
    }

    void initForHomeIds(List<Integer> homeIds) {
        for (Integer id : homeIds) {
            knownShrineStatesMap.put(id, new HashMap<Integer, Shrine>());
        }
    }

    void clear() {
        connectionMap.clear();
    }

    boolean isCompletelyConnected() {
        boolean connected = true;
        for (Integer shrineId : connectionMap.keySet()) {
            Paths paths = makePathsTo(connectionMap.keySet(), shrineId, null, FindPathSettings.useMaxDepth(Integer.MAX_VALUE));
            if (paths.map.keySet().size() != connectionMap.size()) {
                connected = false;
                break;
            }
        }

        return connected;
    }

    void endTurn() {
        turnNumber++;

        for (Integer shrineId : getShrineIds()) {
            Shrine shrine = getShrine(shrineId);
            shrine.endTurn(turnNumber); // grow workers etc.
        }

        saveShrineStates();

        processMoves(); // and fight if needed
    }

    private void saveShrineStates() {
        for (Integer shrineId : getShrineIds()) {
            Shrine shrine = getShrine(shrineId);
            Integer ownerId = shrine.getOwnerId();
            Shrine shrineCopy = shrine.cloneShrine();
            shrineCopy.setLastSeenTurnNum(turnNumber);
            Map<Integer, Shrine> shrineStates = knownShrineStatesMap.get(ownerId);
            if (null == shrineStates) {
                shrineStates = new HashMap<>();
                knownShrineStatesMap.put(ownerId, shrineStates);
            }
            shrineStates.put(shrine.getId(), shrineCopy);
        }
    }

    private void processMoves() {
        // Move everything from departuresMap to arrivalMap for all Shrines
        for (Integer shrineId : getShrineIds()) {
            Shrine shrine = getShrine(shrineId);
            Map<Integer, Map<Shrine.MovableType, Integer>> departureMap = shrine.getDepartureMapCopy();

            for (Integer destinationId : departureMap.keySet()) {
                if (connectionMap.get(shrine.getId()).contains(destinationId)) { // Prevent moves to unconnected shrines
                    Shrine destination = getShrine(destinationId);
                    Map<Shrine.MovableType, Integer> subMap = departureMap.get(destinationId);
                    for (Shrine.MovableType type : subMap.keySet()) {
                        int num = subMap.get(type);
                        destination.addArrival(shrine.getOwnerId(), type, num);
                    }
                }
            }
            shrine.clearDepartureMap();
        }

        // Move all stuff from each shrine to its own arrivalMap, resolve conflict, and update known shrine states
        for (Integer shrineId : getShrineIds()) {
            Shrine shrine = getShrine(shrineId);
            Set<Integer> playerIdsForUpdate = new HashSet<>();
            playerIdsForUpdate.addAll(shrine.getArrivalMapCopy().keySet());
            playerIdsForUpdate.add(shrine.getOwnerId());
            shrine.moveAllToArrivalMap();
            shrine.fight(roller);
            updateShrineState(playerIdsForUpdate, shrine);
        }
    }

    private void updateShrineState(Set<Integer> playerIds, Shrine shrine) {
        Shrine shrineCopy = shrine.cloneShrine();

        for (Integer id : playerIds) {
            Map<Integer, Shrine> shrineStates = knownShrineStatesMap.get(id);
            if (shrine.getOwnerId() != id) {
                shrineStates.put(shrine.getId(), shrineCopy);
            } else {
                shrineStates.remove(shrine.getId());
            }
        }
    }

    /**
     * Get a subset of the main board for a particular player.  Will show all owned shrines
     * as well as the last-known state of unowned shrines known to the player
     */
    Board getBoardForPlayer(Integer playerId) {
        Board board = new Board();
        board.turnNumber = turnNumber;

        // Add (copies of) all shrines owned by the player
        for (Integer id : shrineMap.keySet()) {
            Shrine shrine = shrineMap.get(id);
            if (shrine.getOwnerId() == playerId) {
                Shrine shrineCopy = shrine.cloneShrine();
                board.addShrine(shrineCopy, getConnections(shrine.getId()));
            }
        }

        // Add (previously  copied) shrines this player knows about (but doesn't own)
        Map<Integer, Shrine> knownShrines = knownShrineStatesMap.get(playerId);
        for (Integer id : knownShrines.keySet()) {
            Shrine shrine = knownShrines.get(id);
            board.addShrine(shrine, getConnections(shrine.getId()));
        }

        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        //noinspection SimplifiableIfStatement
        if (!connectionMap.equals(board.connectionMap)) return false;
        return shrineMap.equals(board.shrineMap);
    }

    @Override
    public int hashCode() {
        int result = connectionMap.hashCode();
        result = 31 * result + shrineMap.hashCode();
        return result;
    }
}
