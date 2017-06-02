package com.garthskidstuff.shrines.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * convenient holder of static utility methods
 */

public class Utils {
    public static boolean equals(Object o1, Object o2) {
        return (null == o1) ? (null == o2) : o1.equals(o2);
    }
    
    public static List<Shrine> generateShrines(int num) {
        List<Shrine> shrines = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            Shrine s = new Shrine("" + i, "" + i, i);
            shrines.add(s);
        }

        return shrines;
    }

    public static List<Shrine> makeConnections() {
        List<Shrine> connections = new ArrayList<>();
        return connections;
    }

    public static List<Shrine> makeConnections(Shrine c0) {
        List<Shrine> connections = new ArrayList<>();
        connections.add(c0);
        return connections;
    }

    public static List<Shrine> makeConnections(Shrine c0, Shrine c1) {
        List<Shrine> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        return connections;
    }

    public static List<Shrine> makeConnections(Shrine c0, Shrine c1, Shrine c2) {
        List<Shrine> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        connections.add(c2);
        return connections;
    }

    public static List<Shrine> makeConnections(Shrine c0, Shrine c1, Shrine c2, Shrine c3) {
        List<Shrine> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        connections.add(c2);
        connections.add(c3);
        return connections;
    }

    public static List<Shrine> makeConnections(Shrine c0, Shrine c1, Shrine c2, Shrine c3, Shrine c4) {
        List<Shrine> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        connections.add(c2);
        connections.add(c3);
        connections.add(c4);
        return connections;
    }

}
