package com.garthskidstuff.shrines.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Capture all map from a start Shrine to an end Shrine
 */

public class Paths {
    Shrine start;
    Shrine end;
    public Map<Shrine, List<Shrine>> map = new HashMap<>();

    public Paths(Shrine start, Shrine end) {
        this.start = start;
        this.end = end;
    }

    public void put(Shrine shrine, List<Shrine> connections) {
        map.put(shrine, connections);
    }

    public List<Shrine> get(Shrine shrine) {
        return map.get(shrine);
    }

    public void remove(Shrine shrine) {
        map.remove(shrine);
    }

    @Override
    public int hashCode() {
        return start.hashCode() ^ end.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        boolean ret = false;
        if (other instanceof Paths) {
            Paths otherPaths = (Paths) other;
            ret = Utils.equals(start, otherPaths.start) && Utils.equals(end, otherPaths.end) && (map.equals(otherPaths.map));
        }
        return ret;
    }

    Set<List<Shrine>> makeSetOfPathsFrom(Shrine start, Shrine end) {
        Set<List<Shrine>> allPaths = new HashSet<>();
        List<Shrine> path1 = new ArrayList<>();
        path1.add(start);
        allPaths.add(path1);

        boolean keepGoing = false;
        do {
            keepGoing = false;
            Set<List<Shrine>> newAllPaths = new HashSet<>();
            newAllPaths.addAll(allPaths);

            for (List<Shrine> path : allPaths) {
                Shrine endPath = path.get(path.size() - 1);
                if (!Utils.equals(endPath, end)) { // path is not terminated at destination
                    List<Shrine> connections = get(endPath);
                    newAllPaths.remove(path);
                    if (null != connections) {
                        for (Shrine shrine : connections) {
                            if (!path.contains(shrine)) { // not a loop
                                List<Shrine> newPath = new ArrayList<>();
                                newPath.addAll(path);
                                newPath.add(shrine);
                                newAllPaths.add(newPath);
                                keepGoing = true;
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
