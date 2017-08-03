package com.garthskidstuff.shrines.Game;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * convenient holder of static utility methods
 */

@SuppressWarnings("WeakerAccess")
public class Utils {
    public static boolean equals(java.io.Serializable o1, java.io.Serializable o2) {
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

    @NonNull @SafeVarargs
    public static <T> List<T> makeList (T... array) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    @NonNull @SafeVarargs
    public static <T> Set<T> makeSet(T... array) {
        Set<T> list = new HashSet<>();
        for (T x : array) {
            list.add(x);
        }
        return list;
    }

    //@FunctionalInterface
    public interface Func<Domain, CoDomain> {
        CoDomain apply (Domain d);
    }
}
