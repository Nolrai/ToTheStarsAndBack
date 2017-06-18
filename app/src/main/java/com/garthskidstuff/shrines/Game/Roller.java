package com.garthskidstuff.shrines.Game;

import java.util.Random;

/**
 * Created by garthupshaw1 on 6/17/17.
 */

public class Roller {
    private Random random;

    public Roller(long seed) {
        random = (-1 == seed) ? new Random() : new Random(seed);
    }

    public int roll(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

}