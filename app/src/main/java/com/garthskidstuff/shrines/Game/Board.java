package com.garthskidstuff.shrines.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garthupshaw1 on 7/3/17.
 */

class Board {
    static Comparator<List<Integer>> SORT_SHORTEST_FIRST = new Comparator<List<Integer>>() {
        @Override
        public int compare(List<Integer> lhs, List<Integer> rhs) {
            Integer lhsSize = lhs.size();
            Integer rhsSize = rhs.size();
            return lhsSize.compareTo(rhsSize);
        }
    };
    //shrine Id --> its children's Ids
    protected final Map<Integer, List<Integer>> connectionMap = new HashMap<>();
    // shrine Id -> shrine object
    protected final Map<Integer, Shrine> shrineMap = new HashMap<>();
    protected int turnNumber = 0;

    public Collection<Shrine> getShrines() {
        return shrineMap.values();
    }List<Integer> getConnections(Integer shrineId) {
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
    }Set<List<Integer>> getPaths(Integer startId, Integer endId) {
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

    protected Paths makePathsTo(Set<Integer> knownShrines, Integer start, Integer end, FindPathSettings findPathSettings) {
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

    List<List<Integer>> sortPaths(Set<List<Integer>> allPaths) {
        return sortPaths(allPaths, SORT_SHORTEST_FIRST);
    }

    List<List<Integer>> sortPaths(Set<List<Integer>> allPaths, Comparator<List<Integer>> comparator) {
        List<List<Integer>> sortedPaths = new ArrayList<>();
        sortedPaths.addAll(allPaths);
        Collections.sort(sortedPaths, comparator);
        return sortedPaths;
    }

    Set<Integer> getShrineIds() {
        return shrineMap.keySet();
    }

    void addShrine(Shrine shrine, List<Integer> connectionIds) {
        connectionMap.put(shrine.getId(), connectionIds);
        shrineMap.put(shrine.getId(), shrine);
    }

    void addShrine(Shrine shrine) {
        connectionMap.put(shrine.getId(), new ArrayList<Integer>());
        shrineMap.put(shrine.getId(), shrine);
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
}
