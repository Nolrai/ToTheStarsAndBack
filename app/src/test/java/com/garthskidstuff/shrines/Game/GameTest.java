package com.garthskidstuff.shrines.Game;

import com.google.gson.Gson;

import org.junit.Test;

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

        assertThat(game.homeNames.size(), is(2));
    }

    @Test
    public void gameConstructor_sameNumHomeConnection() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        String homeName0 = game.homeNames.get(0);
        String homeName1 = game.homeNames.get(1);
        assertThat(game.world.getConnections(homeName0).size(), is(game.world.getConnections(homeName1).size()));
    }

    @Test
    public void gameConstructor_shrineBasicInit() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        for (String name : game.world.getShrineNames()) {
            if (!game.homeNames.contains(name)) {
                Shrine shrine = game.world.getShrine(name);
                isBetween(constants.minMaxPopulation, constants.maxMaxPopulation, shrine.getMaxWorkers());
                isBetween(constants.minMiningRateParts, constants.maxMiningRateParts, shrine.getMiningRateParts());
                assertThat(shrine.getMiningDegradationRateParts(), is(constants.miningDegradationRateParts));
            }
        }
    }

    @Test
    public void gameConstructor_shrineHomeInit() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        for (String name : game.homeNames) {
            Shrine shrine = game.world.getShrine(name);
            assertThat(shrine.getMaxWorkers(), is(constants.homeMaxPopulation));
            assertThat(shrine.getMiningRateParts(), is(constants.homeMiningRateParts));
            assertThat(shrine.getNumAltar(), is(constants.homeNumAlters));
            assertThat(shrine.getNumGold(), is(constants.homeNumGold));
            assertThat(shrine.getNumWorker(), is(constants.homeNumWorkers));
        }
    }

    @Test
    public void gameConstructor_2HomesDistance() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        String homeName0 = game.homeNames.get(0);
        String homeName1 = game.homeNames.get(1);
        Set<List<String>> allPaths0to1 = game.world.getPaths(homeName0, homeName1, World.FindPathSettings.useAllShortest());
        List<List<String>> sortedPaths0to1 = game.world.sortPaths(allPaths0to1);
        Set<List<String>> allPaths1to0 = game.world.getPaths(homeName0, homeName1, World.FindPathSettings.useAllShortest());
        List<List<String>> sortedPaths1to0 = game.world.sortPaths(allPaths1to0);

        assertThat(sortedPaths0to1.get(0).size(), is(sortedPaths1to0.get(0).size()));
    }

    @Test
    public void serialize_toJsonAndBack () {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);
        Gson gson = new Gson();
        String json = gson.toJson(game);
        Game newGame = gson.fromJson(json, Game.class);

        assertThat(newGame, is(game));
    }

    private void isBetween(int low, int high, int value) {
        assertThat("value: " + value + " low: " + low, low <= value, is(true));
        assertThat("value: " + value + " high: " + high, high >= value, is(true));
    }
}