package com.garthskidstuff.shrines.Game;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by garthupshaw1 on 7/3/17.
 *
 */

class Manager {
    private Game game;

    private List<Player> players;

     Manager(Game.Constants constants, List<Player> players) {
         game = Game.mkTestGame(constants);
         for (int i = 0; i < game.homeIds.size(); i++) {
             players.get(i).setId(game.homeIds.get(i));
         }
         this.players = players;
    }

    Board runGame() {
        boolean continueGame = true;
        do {
            for (Player player : players) {
                player.startTurn(game.board.getBoardForPlayer(player.getId()));
            }

            for (Player player : players) {
                Board board = player.getTurnResult();
                if (null == board) {
                    continueGame = false;
                    break;
                }

                for (Integer id : board.getShrineIds()) {
                    Shrine shrine = board.getShrine(id);
                    if (shrine.getOwnerId() == player.getId()) {
                        game.board.replaceShrine(shrine);
                    }
                }
            }
            if (continueGame) {
                game.board.endTurn();
            }
        } while (continueGame);

        return game.board;
    }

}
