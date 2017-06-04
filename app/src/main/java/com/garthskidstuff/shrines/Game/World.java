package com.garthskidstuff.shrines.Game;

import java.util.ArrayList;
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

public class World {
    private Map<Shrine, List<Shrine>> shrineMap = new HashMap<>();

    public void addShrine(Shrine shrine, List<Shrine> connections) {
        shrineMap.put(shrine, connections);
    }

    public List<Shrine> get(Shrine shrine) {
        return shrineMap.get(shrine);
    }

    public void clear() {
        shrineMap.clear();
    }

    public Set<List<Shrine>> getPaths(Set<Shrine> knownShrines, Shrine start, Shrine end) {
        Paths paths = new Paths(start, end);
        makePathsTo(knownShrines, start, paths);
        return paths.makeSetOfPathsFrom(start, end);
    }

    private void makePathsTo(Set<Shrine> knownShrines, Shrine start, Paths paths) {
        if (null == paths.get(start)) {
            List<Shrine> connections = get(start);
            if (null != connections) {
                List<Shrine> pathConnections = new ArrayList<Shrine>();
                for (Shrine shrine : connections) {
                    if (knownShrines.contains(shrine)) {
                        pathConnections.add(shrine);
                    }
                }
                paths.put(start, pathConnections);
                for (Shrine shrine : pathConnections) {
                    if (shrine != paths.end) {
                        makePathsTo(knownShrines, shrine, paths);
                    }
                }
            }
        }
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
        boolean connected = true;
        Set<Shrine> knownShrines = new HashSet<>();
        knownShrines.addAll(shrineMap.keySet());

        for (Shrine shrine : shrineMap.keySet()) {
            Set<Shrine> included = new HashSet<>();
            Paths paths = new Paths(shrine, null);
            makePathsTo(knownShrines, shrine, paths);
            if (paths.map.keySet().size() != knownShrines.size()) {
                connected = false;
                break;
            }

        }

        return connected;
    }

    public Set<Shrine> getShrines() {
        return shrineMap.keySet();
    }

}
