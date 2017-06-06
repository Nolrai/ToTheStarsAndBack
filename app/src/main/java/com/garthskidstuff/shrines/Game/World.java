package com.garthskidstuff.shrines.Game;

import android.support.v4.util.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Map of all Shrines in the world: Shrine -> connections + convenience functions
 */

public class World {
    private Map<Shrine, List<Shrine>> shrineMap = new HashMap<>();

    public enum FindPathType { USE_ALL_SHORTEST, USE_MAX_DEPTH }
    public static class FindPathSettings {
        FindPathType findPathType;
        int depth = -1;

        public static FindPathSettings useAllShortest() {
            FindPathSettings findPathSettings = new FindPathSettings();
            findPathSettings.findPathType = FindPathType.USE_ALL_SHORTEST;
            return findPathSettings;
        }

        public static FindPathSettings useMaxDepth(int depth) {
            FindPathSettings findPathSettings = new FindPathSettings();
            findPathSettings.findPathType = FindPathType.USE_MAX_DEPTH;
            findPathSettings.depth = depth;
            return findPathSettings;
        }
    };

    public void addShrine(Shrine shrine, List<Shrine> connections) {
        shrineMap.put(shrine, connections);
    }

    public void addShrine(Shrine shrine) {
        shrineMap.put(shrine, new ArrayList<Shrine>());
    }

    public List<Shrine> get(Shrine shrine) {
        return shrineMap.get(shrine);
    }

    public void clear() {
        shrineMap.clear();
    }

    public Set<List<Shrine>> getPaths(Shrine start, Shrine end) {
        return getPaths(shrineMap.keySet(), start, end, FindPathSettings.useAllShortest());
    }

    public Set<List<Shrine>> getPaths(Set<Shrine> knownShrines, Shrine start, Shrine end) {
        return getPaths(knownShrines, start, end, FindPathSettings.useAllShortest());
    }

    public Set<List<Shrine>> getPaths(Shrine start, Shrine end, FindPathSettings findPathSettings) {
        return getPaths(shrineMap.keySet(), start, end, findPathSettings);
    }

    public Set<List<Shrine>> getPaths(Set<Shrine> knownShrines, Shrine start, Shrine end, FindPathSettings findPathSettings) {
        Paths paths = copyKnownWorlds(knownShrines, start, end);
        return paths.makeSetOfPathsFrom(start, end, findPathSettings);
    }

    private Paths copyKnownWorlds(Set<Shrine> knownShrines, Shrine start, Shrine end) {
        Paths paths = new Paths(start, end);

        for (Shrine shrine : shrineMap.keySet()) {
            if (knownShrines.contains(shrine)) {
                List<Shrine> connections = new ArrayList<>();
                for (Shrine s : shrineMap.get(shrine)) {
                    if (knownShrines.contains(s)) {
                        connections.add(s);
                    }
                }
                paths.map.put(shrine, connections);
            }
        }

        return paths;
    }

    public static Comparator<List<Shrine>> SORT_SHORTEST_FIRST = new Comparator<List<Shrine>>() {
        @Override
        public int compare(List<Shrine> lhs, List<Shrine> rhs) {
            Integer lhsSize = lhs.size();
            Integer rhsSize = rhs.size();
            return lhsSize.compareTo(rhsSize);
        }
    };

    public static Comparator<List<Shrine>> SORT_BY_POPULATION = new Comparator<List<Shrine>>() {
        @Override
        public int compare(List<Shrine> lhs, List<Shrine> rhs) {
            Integer lhsSize = 0;
            for (Shrine shrine : lhs) {
                lhsSize += shrine.getNumWorkers();
            }
            Integer rhsSize = 0;
            for (Shrine shrine : rhs) {
                rhsSize += shrine.getNumWorkers();
            }
            return lhsSize.compareTo(rhsSize);
        }
    };

    public static List<List<Shrine>> sortPaths(Set<List<Shrine>> allPaths) {
        return sortPaths(allPaths, SORT_SHORTEST_FIRST);
    }

    public static List<List<Shrine>> sortPaths(Set<List<Shrine>> allPaths, Comparator<List<Shrine>> comparator) {
        List<List<Shrine>> sortedPaths = new ArrayList<>();

        for (List<Shrine> path : allPaths) {
            sortedPaths.add(path);
        }

        Collections.sort(sortedPaths, comparator);

        return sortedPaths;
    }

    public boolean isCompletelyConnected() {
        for (Shrine start : shrineMap.keySet()) {
            for (Shrine end : shrineMap.keySet()) {
                if (start != end) {
                    Set<List<Shrine>> shortestPaths = getPaths(start, end, FindPathSettings.useMaxDepth(Integer.MAX_VALUE));
                    if (shortestPaths.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public Set<Shrine> getShrines() {
        return shrineMap.keySet();
    }

}
