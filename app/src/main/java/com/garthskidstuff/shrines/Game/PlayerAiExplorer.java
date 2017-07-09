package com.garthskidstuff.shrines.Game;

/**
 * Created by garthupshaw1 on 7/3/17.
 *
 */

public class PlayerAiExplorer extends PlayerAi {
    @Override
    void doInBackground() {
        if (2 <= board.turnNumber) {
            board = null;
            return;
        }

        for (Integer id : board.getOwnedShrineIds(getId())) {
            exploreWithWorkers(id);
        }
    }

}
