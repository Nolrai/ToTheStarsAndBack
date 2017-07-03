package com.garthskidstuff.shrines.Game;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by garthupshaw1 on 6/4/17.
 *
 */
public class GameTest extends BaseTest {

    @Test
    public void gameConstructor_2Homes() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        assertThat(game.homeIds.size(), is(2));
    }

    @Test
    public void gameConstructor_sameNumHomeConnection() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        Integer homeId0 = game.homeIds.get(0);
        Integer homeId1 = game.homeIds.get(1);
        assertThat(game.board.getConnections(homeId0).size(), is(game.board.getConnections(homeId1).size()));
    }

    @Test
    public void gameConstructor_shrineBasicInit() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        for (Integer name : game.board.getShrineIds()) {
            if (!game.homeIds.contains(name)) {
                Shrine shrine = game.board.getShrine(name);
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

        for (Integer name : game.homeIds) {
            Shrine shrine = game.board.getShrine(name);
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

        Integer homeId0 = game.homeIds.get(0);
        Integer homeId1 = game.homeIds.get(1);
        Set<List<Integer>> allPaths0to1 = game.board.getPaths(homeId0, homeId1, BoardEngine.FindPathSettings.useAllShortest());
        List<List<Integer>> sortedPaths0to1 = game.board.sortPaths(allPaths0to1);
        Set<List<Integer>> allPaths1to0 = game.board.getPaths(homeId0, homeId1, BoardEngine.FindPathSettings.useAllShortest());
        List<List<Integer>> sortedPaths1to0 = game.board.sortPaths(allPaths1to0);

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