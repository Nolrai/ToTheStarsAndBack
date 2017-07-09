package com.garthskidstuff.shrines.Game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by garthupshaw1 on 7/3/17.
 */
public class ManagerTest extends BaseTest {

    @Test
    public void manager_startGame() {
        Game.Constants constants = new Game.Constants(1);
        List<Player> players = new ArrayList<>();
        players.add(new PlayerAiExplorer());
        players.add(new PlayerAiExplorer());
        Manager manager = new Manager(constants, players);

        Board board = manager.runGame();

        Set<Integer> owned = board.getOwnedShrineIds(players.get(0).getId());
        owned.addAll(board.getOwnedShrineIds(players.get(1).getId()));


    }

}