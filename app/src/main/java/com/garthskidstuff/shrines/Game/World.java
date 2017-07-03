package com.garthskidstuff.shrines.Game;


import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Map of all Shrines in the world: Shrine -> connections + convenience functions
 */

class World {
    //shrine Id --> its children's Ids
    private final Map<Integer, List<Integer>> connectionMap = new HashMap<>();
    // shrine Id -> shrine object
    private final Map<Integer, Shrine> shrineMap = new HashMap<>();
    // homeId --> (shrineId --> Shrine): Saves a shrine state at the "last seen" moment
    private final Map<Integer, Map<Integer, Shrine>> knownShrineStatesMap = new HashMap<>();

    private int turnNumber = 0;

    private final Roller roller;

    World(Roller randomRoller) {
        this.roller = randomRoller;
    }

    public Collection<Shrine> getShrines() {
        return shrineMap.values();
    }

    private enum FindPathType { USE_ALL_SHORTEST, USE_MAX_DEPTH }
    static class FindPathSettings {
        FindPathType findPathType;
        int depth = -1;

        static FindPathSettings useAllShortest() {
            FindPathSettings findPathSettings = new FindPathSettings();
            findPathSettings.findPathType = FindPathType.USE_ALL_SHORTEST;
            return findPathSettings;
        }

        static FindPathSettings useMaxDepth(int depth) {
            FindPathSettings findPathSettings = new FindPathSettings();
            findPathSettings.findPathType = FindPathType.USE_MAX_DEPTH;
            findPathSettings.depth = depth;
            return findPathSettings;
        }
    }

    void initForHomeIds(List<Integer> homeIds) {
        for (Integer id : homeIds) {
            knownShrineStatesMap.put(id, new HashMap<Integer, Shrine>());
        }
    }

    void addShrine(Shrine shrine, List<Integer> connectionIds) {
        connectionMap.put(shrine.getId(), connectionIds);
        shrineMap.put(shrine.getId(), shrine);
     }

    void addShrine(Shrine shrine) {
        connectionMap.put(shrine.getId(), new ArrayList<Integer>());
        shrineMap.put(shrine.getId(), shrine);
    }

    List<Integer> getConnections(Integer shrineId) {
        return connectionMap.get(shrineId);
    }

    Shrine getShrine(Integer shrineId) {
        return shrineMap.get(shrineId);
    }

    @SuppressWarnings("unused")
    List<Shrine> getShrines(List<Integer> shrineIds) {
        List<Shrine> shrines = new ArrayList<>();
        for (Integer id : shrineIds) {
            shrines.add(getShrine(id));
        }
        return shrines;
    }

    void clear() {
        connectionMap.clear();
    }

    Set<List<Integer>> getPaths(Integer startId, Integer endId) {
        return getPaths(connectionMap.keySet(), startId, endId, FindPathSettings.useAllShortest());
    }

    Set<List<Integer>> getPaths(Set<Integer> knownShrines, Integer startId, Integer endId) {
        return getPaths(knownShrines, startId, endId, FindPathSettings.useAllShortest());
    }

    Set<List<Integer>> getPaths(Integer startId, Integer endId, FindPathSettings findPathSettings) {
        return getPaths(connectionMap.keySet(), startId, endId, findPathSettings);
    }

    Set<List<Integer>> getPaths(Set<Integer> knownShrines, Integer startId, Integer endId, FindPathSettings findPathSettings) {
        Paths paths = makePathsTo(knownShrines, startId, endId, findPathSettings);
        return paths.makeSetOfPathsFrom();
    }

    private Paths makePathsTo(Set<Integer> knownShrines, Integer start, Integer end, FindPathSettings findPathSettings) {
        Paths paths = new Paths(start, end);
        List<Pair<Integer, Integer>> q = new ArrayList<>();
        q.add(new Pair<>(0, paths.startId));

        //noinspection WhileLoopReplaceableByForEach
        for (int i = 0; i < q.size(); i++) {
            Pair<Integer, Integer> item = q.get(i);
            if (null == paths.get(item.second)) {
                List<Integer> connections = getConnections(item.second);
                if ((null != connections) &&
                        ((FindPathType.USE_ALL_SHORTEST == findPathSettings.findPathType) ||
                                (item.first < findPathSettings.depth))) {
                    List<Integer> pathConnections = new ArrayList<>();
                    for (Integer shrineId : connections) {
                        if (knownShrines.contains(shrineId)) {
                            pathConnections.add(shrineId);
                        }
                    }
                    paths.put(item.second, pathConnections);
                    for (Integer shrineId : pathConnections) {
                        q.add(new Pair<>(item.first + 1, shrineId));
                    }
                }
                if ((FindPathType.USE_ALL_SHORTEST == findPathSettings.findPathType) &&
                        (Utils.equals(item.second, paths.endId))) {
                    findPathSettings.findPathType = FindPathType.USE_MAX_DEPTH;
                    findPathSettings.depth = item.first;
                    paths.shortestLength = item.first;
                }
            }
        }
        return paths;
    }

    static Comparator<List<Integer>> SORT_SHORTEST_FIRST = new Comparator<List<Integer>>() {
        @Override
        public int compare(List<Integer> lhs, List<Integer> rhs) {
            Integer lhsSize = lhs.size();
            Integer rhsSize = rhs.size();
            return lhsSize.compareTo(rhsSize);
        }
    };

    List<List<Integer>> sortPaths(Set<List<Integer>> allPaths) {
        return sortPaths(allPaths, SORT_SHORTEST_FIRST);
    }

    List<List<Integer>> sortPaths(Set<List<Integer>> allPaths, Comparator<List<Integer>> comparator) {
        List<List<Integer>> sortedPaths = new ArrayList<>();
        sortedPaths.addAll(allPaths);
        Collections.sort(sortedPaths, comparator);
        return sortedPaths;
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

    Set<Integer> getShrineIds() {
        return shrineMap.keySet();
    }

    void endTurn() throws InvalidObjectException {
        turnNumber++;

        for (Integer shrineId : getShrineIds()) {
            Shrine shrine = getShrine(shrineId);
            shrine.endTurn(); // grow workers etc.
        }

        saveShrineStates();

        processMoves(); // and fight if needed
    }

    private void saveShrineStates() {
        for (Integer shrineId : getShrineIds()) {
            Shrine shrine = getShrine(shrineId);
            Integer ownerId = shrine.getOwnerId();
            Shrine shrineCopy = shrine.cloneShrine(-1);
            shrineCopy.setLastSeenTurnNum(turnNumber);
            Map<Integer, Shrine> shrineStates = knownShrineStatesMap.get(ownerId);
            if (null == shrineStates) {
                shrineStates = new HashMap<>();
                knownShrineStatesMap.put(ownerId, shrineStates);
            }
            shrineStates.put(shrine.getId(), shrineCopy);
        }
    }

    private void processMoves() throws InvalidObjectException {
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
                } else {
                    throw new InvalidObjectException(shrine.getId() + " does not connect to " + destinationId);
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
        Shrine shrineCopy = shrine.cloneShrine(-1);

        for (Integer id : playerIds) {
            Map<Integer, Shrine> shrineStates = knownShrineStatesMap.get(id);
            if (shrine.getOwnerId() != id) {
                shrineStates.put(shrine.getId(), shrineCopy);
            } else {
                shrineStates.remove(shrine.getId());
            }
        }
    }

    Set<Integer> getKnownIds(Integer playerId) {
        // Add all shrines owned by the player
        Set<Integer> knownIds = new HashSet<>();
        for (Integer id : shrineMap.keySet()) {
            Shrine shrine = shrineMap.get(id);
            if (shrine.getOwnerId() == playerId) {
                knownIds.add(id);
            }
        }

        // Add shrines this player knows about (but doesn't own)
        Map<Integer, Shrine> knownShrines = getKnownShrineState(playerId);
        knownIds.addAll(knownShrines.keySet());

        return knownIds;
    }

    Map<Integer, Shrine> getKnownShrineState(Integer playerId) {
        return knownShrineStatesMap.get(playerId);
    }

    /**
     * Get all the owned AND known shrines (known shrines are as of the last seen turn)
     */
    @SuppressWarnings("unused")
    Map<Integer, Shrine> getPlayerShrines(Integer playerId) {
        Map<Integer, Shrine> shrines = new HashMap<>();
        for (Integer id : shrineMap.keySet()) {
            Shrine shrine = shrineMap.get(id);
            if (shrine.getOwnerId() == playerId) {
                shrines.put(shrine.getId(), shrine);
            }
        }

        for (Shrine shrine : getKnownShrineState(playerId).values()) {
            shrines.put(shrine.getId(), shrine);
        }

        return shrines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        World world = (World) o;

        //noinspection SimplifiableIfStatement
        if (!connectionMap.equals(world.connectionMap)) return false;
        return shrineMap.equals(world.shrineMap);
    }

    @Override
    public int hashCode() {
        int result = connectionMap.hashCode();
        result = 31 * result + shrineMap.hashCode();
        return result;
    }
}
