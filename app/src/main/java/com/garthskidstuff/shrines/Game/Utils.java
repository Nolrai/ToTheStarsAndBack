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
            Shrine shrine = new Shrine(i, "" + i, "" + i);
            shrine.initBasic(100, 0, 0, 0);
            shrines.add(shrine);
        }

        return shrines;
    }

    public static List<Integer> makeConnections(Integer c0) {
        List<Integer> connections = new ArrayList<>();
        connections.add(c0);
        return connections;
    }

    public static List<Integer> makeConnections(Integer c0, Integer c1) {
        List<Integer> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        return connections;
    }

    public static List<Integer> makeConnections(Integer c0, Integer c1, Integer c2) {
        List<Integer> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        connections.add(c2);
        return connections;
    }

    public static List<Integer> makeConnections(Integer c0, Integer c1, Integer c2, Integer c3) {
        List<Integer> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        connections.add(c2);
        connections.add(c3);
        return connections;
    }

    public static List<Integer> makeConnections(Integer c0, Integer c1, Integer c2, Integer c3, Integer c4) {
        List<Integer> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        connections.add(c2);
        connections.add(c3);
        connections.add(c4);
        return connections;
    }

    public static boolean isBetweenInclusive(int low, int high, int value) {
        return (low <= value) && (value <= high);
    }

}
