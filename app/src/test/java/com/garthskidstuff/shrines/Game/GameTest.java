package com.garthskidstuff.shrines.Game;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by garthupshaw1 on 6/4/17.
 */
public class GameTest {

    @Test
    public void gameConstructor_2Homes() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        assertThat(game.homes.size(), is(2));
    }

    @Test
    public void gameConstructor_sameNumHomeConnection() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        Shrine home0 = game.homes.get(0);
        Shrine home1 = game.homes.get(1);
        assertThat(game.world.get(home0).size(), is(game.world.get(home1).size()));
    }

    @Test
    public void gameConstructor_shrineBasicInit() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        for (Shrine shrine : game.world.getShrines()) {
            if (!game.homes.contains(shrine)) {
                isBetween(constants.minMaxPopulation, constants.maxMaxPopulation, shrine.getMaxPopulation());
                isBetween(constants.minMiningRateParts, constants.maxMiningRateParts, shrine.getMiningRateParts());
                assertThat(shrine.getMiningDegradationRateParts(), is(constants.miningDegradationRateParts));
            }
        }
    }

    @Test
    public void gameConstructor_shrineHomeInit() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        for (Shrine shrine : game.homes) {
            assertThat(shrine.getMaxPopulation(), is(constants.homeMaxPopulation));
            assertThat(shrine.getMiningRateParts(), is(constants.homeMiningRateParts));
            assertThat(shrine.getNumAlters(), is(constants.homeNumAlters));
            assertThat(shrine.getNumGold(), is(constants.homeNumGold));
            assertThat(shrine.getNumWorkers(), is(constants.homeNumWorkers));
        }
    }

    @Test
    public void gameConstructor_2HomesDistance() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        Shrine home0 = game.homes.get(0);
        Shrine home1 = game.homes.get(1);
        Set<List<Shrine>> allPaths0to1 = game.world.getPaths(home0, home1, World.FindPathSettings.useAllShortest());
        List<List<Shrine>> sortedPaths0to1 = World.sortPaths(allPaths0to1);
        Set<List<Shrine>> allPaths1to0 = game.world.getPaths(home0, home1, World.FindPathSettings.useAllShortest());
        List<List<Shrine>> sortedPaths1to0 = World.sortPaths(allPaths1to0);

        assertThat(sortedPaths0to1.get(0).size(), is(sortedPaths1to0.get(0).size()));
    }

    private void isBetween(int low, int high, int value) {
        assertThat("value: " + value + " low: " + low, low <= value, is(true));
        assertThat("value: " + value + " high: " + high, high >= value, is(true));
    }
}