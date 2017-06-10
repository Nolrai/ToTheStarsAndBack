package com.garthskidstuff.shrines.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * convenient holder of static utility methods
 */

public class Utils {
    public static boolean equals(Object o1, Object o2) {
        return (null == o1) ? (null == o2) : o1.equals(o2);
    }

    /**
     * Useful method for tests
     */
    public static List<String> generateShrineNames(int num) {
        List<String> shrineNames = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            shrineNames.add("" + i);
        }

        return shrineNames;
    }

    public static List<Shrine> generateShrines(int num) {
        List<Shrine> shrines = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            Shrine shrine = new Shrine("" + i, "" + i);
            shrines.add(shrine);
        }

        return shrines;
    }

    public static List<String> makeConnections(String c0) {
        List<String> connections = new ArrayList<>();
        connections.add(c0);
        return connections;
    }

    public static List<String> makeConnections(String c0, String c1) {
        List<String> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        return connections;
    }

    public static List<String> makeConnections(String c0, String c1, String c2) {
        List<String> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        connections.add(c2);
        return connections;
    }

    public static List<String> makeConnections(String c0, String c1, String c2, String c3) {
        List<String> connections = new ArrayList<>();
        connections.add(c0);
        connections.add(c1);
        connections.add(c2);
        connections.add(c3);
        return connections;
    }

    public static List<String> makeConnections(String c0, String c1, String c2, String c3, String c4) {
        List<String> connections = new ArrayList<>();
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
