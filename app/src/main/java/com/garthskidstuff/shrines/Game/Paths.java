package com.garthskidstuff.shrines.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Capture all map from a startId Integer to an endId Integer
 */

public class Paths {
    Integer startId;
    Integer endId;
    int shortestLength = -1;
    public Map<Integer, List<Integer>> map = new HashMap<>();

    public Paths(Integer startId, Integer endId) {
        this.startId = startId;
        this.endId = endId;
    }

    public void put(Integer shrineId, List<Integer> connections) {
        map.put(shrineId, connections);
    }

    public List<Integer> get(Integer shrineId) {
        return map.get(shrineId);
    }

    public void remove(Integer shrineId) {
        map.remove(shrineId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paths paths = (Paths) o;

        if (shortestLength != paths.shortestLength) return false;
        if (!startId.equals(paths.startId)) return false;
        if (!endId.equals(paths.endId)) return false;
        return map.equals(paths.map);

    }

    @Override
    public int hashCode() {
        int result = startId.hashCode();
        result = 31 * result + endId.hashCode();
        result = 31 * result + shortestLength;
        result = 31 * result + map.hashCode();
        return result;
    }

    Set<List<Integer>> makeSetOfPathsFrom() {
        Set<List<Integer>> allPaths = new HashSet<>();
        List<Integer> path1 = Utils.makeList(startId);
        allPaths.add(path1);

        boolean keepGoing = false;
        do {
            keepGoing = false;
            Set<List<Integer>> newAllPaths = new HashSet<>();
            newAllPaths.addAll(allPaths);

            for (List<Integer> path : allPaths) {
                Integer endPath = path.get(path.size() - 1);
                boolean tooLong = (-1 != shortestLength) && (shortestLength < path.size());
                if (!tooLong && !Utils.equals(endPath, endId)) { // partial path hasn't hit endId
                    List<Integer> connections = get(endPath);
                    newAllPaths.remove(path);
                    if (null != connections) {
                        for (Integer shrineId : connections) {
                            if (!path.contains(shrineId)) { // not a loop
                                List<Integer> newPath = Utils.makeList();
                                newPath.addAll(path);
                                if ((-1 == shortestLength) ||
                                        (newPath.size() < shortestLength) ||
                                        (shrineId == endId)) {
                                    newPath.add(shrineId);
                                    newAllPaths.add(newPath);
                                    keepGoing = true;
                                }
                            }
                        }
                    }
                }
            }

            allPaths = newAllPaths;
        } while (keepGoing);

        return allPaths;
    }
}
