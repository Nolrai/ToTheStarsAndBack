package com.garthskidstuff.shrines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by garthupshaw1 on 5/10/17.
 */

public class Game {
    private List<Shrine> shrines = new ArrayList<>();

    private static final int numPlayers = 2;

    private Random random = new Random();

    private static final int minShrines = 40;
    private static final int maxShrines = 100;

    public Game() {
        int numShrines = random.nextInt(maxShrines-minShrines) + minShrines;

        for (int i = 0; i < numShrines; i++) {
            Shrine shrine = new Shrine();

        }
    }
}
