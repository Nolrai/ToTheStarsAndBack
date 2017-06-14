package com.garthskidstuff.shrines.Game;

import android.renderscript.RSInvalidStateException;
import android.support.v4.util.Pair;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Map of all Shrines in the world: Shrine -> connections + convenience functions
 */

class World {
    private Map<String, List<String>> connectionMap = new HashMap<>(); // shrine name --> its children's names
    private Map<String, Shrine> shrineMap = new HashMap<>(); // shrine name -> shrine object

    public Collection<Shrine> getShrines() {
        return shrineMap.values();
    }


    enum FindPathType { USE_ALL_SHORTEST, USE_MAX_DEPTH }
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

    void addShrine(Shrine shrine, List<String> connectionNames) {
        connectionMap.put(shrine.getName(), connectionNames);
        shrineMap.put(shrine.getName(), shrine);
     }

    void addShrine(Shrine shrine) {
        connectionMap.put(shrine.getName(), new ArrayList<String>());
        shrineMap.put(shrine.getName(), shrine);
    }

    List<String> getConnections(String shrineName) {
        return connectionMap.get(shrineName);
    }

    Shrine getShrine(String shrineName) {
        return shrineMap.get(shrineName);
    }

    List<Shrine> getShrines(List<String> shrineNames) {
        List<Shrine> shrines = new ArrayList<>();
        for (String name : shrineNames) {
            shrines.add(getShrine(name));
        }
        return shrines;
    }

    void clear() {
        connectionMap.clear();
    }

    Set<List<String>> getPaths(String startName, String endName) {
        return getPaths(connectionMap.keySet(), startName, endName, FindPathSettings.useAllShortest());
    }

    Set<List<String>> getPaths(Set<String> knownShrines, String startName, String endName) {
        return getPaths(knownShrines, startName, endName, FindPathSettings.useAllShortest());
    }

    Set<List<String>> getPaths(String startName, String endName, FindPathSettings findPathSettings) {
        return getPaths(connectionMap.keySet(), startName, endName, findPathSettings);
    }

    Set<List<String>> getPaths(Set<String> knownShrines, String startName, String endName, FindPathSettings findPathSettings) {
        Paths paths = makePathsTo(knownShrines, startName, endName, findPathSettings);
        return paths.makeSetOfPathsFrom();
    }

    private Paths makePathsTo(Set<String> knownShrines, String start, String end, FindPathSettings findPathSettings) {
        Paths paths = new Paths(start, end);
        List<Pair<Integer, String>> q = new ArrayList<>();
        q.add(new Pair<>(0, paths.startName));

        //noinspection WhileLoopReplaceableByForEach
        for (int i = 0; i < q.size(); i++) {
            Pair<Integer, String> item = q.get(i);
            if (null == paths.get(item.second)) {
                List<String> connections = getConnections(item.second);
                if ((null != connections) &&
                        ((FindPathType.USE_ALL_SHORTEST == findPathSettings.findPathType) ||
                                (item.first < findPathSettings.depth))) {
                    List<String> pathConnections = new ArrayList<>();
                    for (String shrineName : connections) {
                        if (knownShrines.contains(shrineName)) {
                            pathConnections.add(shrineName);
                        }
                    }
                    paths.put(item.second, pathConnections);
                    for (String shrineName : pathConnections) {
                        q.add(new Pair<>(item.first + 1, shrineName));
                    }
                }
                if ((FindPathType.USE_ALL_SHORTEST == findPathSettings.findPathType) &&
                        (Utils.equals(item.second, paths.endName))) {
                    findPathSettings.findPathType = FindPathType.USE_MAX_DEPTH;
                    findPathSettings.depth = item.first;
                    paths.shortestLength = item.first;
                }
            }
        }
        return paths;
    }

    static Comparator<List<String>> SORT_SHORTEST_FIRST = new Comparator<List<String>>() {
        @Override
        public int compare(List<String> lhs, List<String> rhs) {
            Integer lhsSize = lhs.size();
            Integer rhsSize = rhs.size();
            return lhsSize.compareTo(rhsSize);
        }
    };

    List<List<String>> sortPaths(Set<List<String>> allPaths) {
        return sortPaths(allPaths, SORT_SHORTEST_FIRST);
    }

    List<List<String>> sortPaths(Set<List<String>> allPaths, Comparator<List<String>> comparator) {
        List<List<String>> sortedPaths = new ArrayList<>();
        sortedPaths.addAll(allPaths);
        Collections.sort(sortedPaths, comparator);
        return sortedPaths;
    }

    boolean isCompletelyConnected() {
        boolean connected = true;
        for (String shrineName : connectionMap.keySet()) {
            Paths paths = makePathsTo(connectionMap.keySet(), shrineName, null, FindPathSettings.useMaxDepth(Integer.MAX_VALUE));
            if (paths.map.keySet().size() != connectionMap.size()) {
                connected = false;
                break;
            }
        }

        return connected;
    }

    Set<String> getShrineNames() {
        return shrineMap.keySet();
    }

    void processMoves() throws InvalidObjectException {
        for (String shrineName : getShrineNames()) {
            Shrine shrine = getShrine(shrineName);
            //TODO get copy
            Map<String, Map<Shrine.MovableType, Integer>> departureMap = shrine.getDepartureMap();

            for (String destinationName : departureMap.keySet()) {
                if (connectionMap.get(shrine.getName()).contains(destinationName)) { // Prevent moves to unconnected shrines
                    Shrine destination = getShrine(destinationName);
                    Map<Shrine.MovableType, Integer> subMap = departureMap.get(destinationName);
                    for (Shrine.MovableType type : subMap.keySet()) {
                        int num = subMap.get(type);

                        destination.addArrival(shrine.getOwnerName(), type, num);
                    }
                } else {
                    throw new InvalidObjectException(shrine.getName() + " does not connect to " + destinationName);
                }
            }
            departureMap.clear();
        }
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
