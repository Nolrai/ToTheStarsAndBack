package com.garthskidstuff.shrines.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Capture all map from a startName String to an endName String
 */

public class Paths {
    String startName;
    String endName;
    int shortestLength = -1;
    public Map<String, List<String>> map = new HashMap<>();

    public Paths(String startName, String endName) {
        this.startName = startName;
        this.endName = endName;
    }

    public void put(String shrineName, List<String> connections) {
        map.put(shrineName, connections);
    }

    public List<String> get(String shrineName) {
        return map.get(shrineName);
    }

    public void remove(String shrineName) {
        map.remove(shrineName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paths paths = (Paths) o;

        if (shortestLength != paths.shortestLength) return false;
        if (!startName.equals(paths.startName)) return false;
        if (!endName.equals(paths.endName)) return false;
        return map.equals(paths.map);

    }

    @Override
    public int hashCode() {
        int result = startName.hashCode();
        result = 31 * result + endName.hashCode();
        result = 31 * result + shortestLength;
        result = 31 * result + map.hashCode();
        return result;
    }

    Set<List<String>> makeSetOfPathsFrom() {
        Set<List<String>> allPaths = new HashSet<>();
        List<String> path1 = new ArrayList<>();
        path1.add(startName);
        allPaths.add(path1);

        boolean keepGoing = false;
        do {
            keepGoing = false;
            Set<List<String>> newAllPaths = new HashSet<>();
            newAllPaths.addAll(allPaths);

            for (List<String> path : allPaths) {
                String endPath = path.get(path.size() - 1);
                boolean tooLong = (-1 != shortestLength) && (shortestLength < path.size());
                if (!tooLong && !Utils.equals(endPath, endName)) { // partial path hasn't hit endName
                    List<String> connections = get(endPath);
                    newAllPaths.remove(path);
                    if (null != connections) {
                        for (String shrineName : connections) {
                            if (!path.contains(shrineName)) { // not a loop
                                List<String> newPath = new ArrayList<>();
                                newPath.addAll(path);
                                if ((-1 == shortestLength) ||
                                        (newPath.size() < shortestLength) ||
                                        (shrineName == endName)) {
                                    newPath.add(shrineName);
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
