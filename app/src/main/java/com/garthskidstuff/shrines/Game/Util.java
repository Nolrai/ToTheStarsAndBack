package com.garthskidstuff.shrines.Game;

/**
 * Created by garthupshaw1 on 5/28/17.
 */

public class Util {

    public static <T> boolean equals(T s1, T s2) {
        return (null == s1) ? (null == s2) : s1.equals(s2);
    }
}
