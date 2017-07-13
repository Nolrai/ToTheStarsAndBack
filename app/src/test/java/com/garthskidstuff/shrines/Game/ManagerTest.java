package com.garthskidstuff.shrines.Game;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;

/**
 * Created by garthupshaw1 on 7/3/17.
 */
public class ManagerTest extends BaseTest {

    @Test
    public void manager_startGame() {
        Game.Constants constants = new Game.Constants(1);
        //The cast to Player is to help Java's type checker
        List<Player> players = Utils.makeList((Player) new PlayerAiExplorer(), new PlayerAiExplorer());
        Manager manager = new Manager(constants, players);

        Board board = manager.runGame();

        Set<Integer> owned = board.getOwnedShrineIds(players.get(0).getId());
        owned.addAll(board.getOwnedShrineIds(players.get(1).getId()));


    }

}