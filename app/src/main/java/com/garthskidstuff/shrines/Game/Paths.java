package com.garthskidstuff.shrines.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Capture all paths from a start Shrine to an end Shrine
 */

public class Paths {
    Shrine start;
    Shrine end;
    private Map<Shrine, List<Shrine>> paths = new HashMap<>();

    public Paths(Shrine start, Shrine end) {
        this.start = start;
        this.end = end;
    }

    public void put(Shrine shrine, List<Shrine> connections) {
        paths.put(shrine, connections);
    }

    public List<Shrine> get(Shrine shrine) {
        return paths.get(shrine);
    }

    public void remove(Shrine shrine) {
        paths.remove(shrine);
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
            ret = Utils.equals(start, otherPaths.start) && Utils.equals(end, otherPaths.end) && (paths.equals(otherPaths.paths));
        }
        return ret;
    }
}
