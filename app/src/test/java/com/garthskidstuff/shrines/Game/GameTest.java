package com.garthskidstuff.shrines.Game;

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
    public void gameConstructor_test2Homes() {
        Game.Constants constants = new Game.Constants(1);
        Game game = Game.mkTestGame(constants);

        assertThat(game.homes.size(), is(2));
    }

    @Test
    public void gameConstructor_test2HomesDistance() {
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
}