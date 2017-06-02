package com.garthskidstuff.shrines.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Map of all Shrines in the world: Shrine -> connections + convenience functions
 */

public class World {
    Map<Shrine, List<Shrine>> shrineMap = new HashMap<>();

    public void addShrine(Shrine shrine, List<Shrine> connections) {
        shrineMap.put(shrine, connections);
    }

    public List<Shrine> get(Shrine shrine) {
        return shrineMap.get(shrine);
    }
}
