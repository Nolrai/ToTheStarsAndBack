package com.garthskidstuff.shrines.Game;

/**
 * Created by garthupshaw1 on 7/3/17.
 *
 */

abstract class PlayerAi extends Player {

    /**
     * Send a single worker from the passed-in source to every unowned connection
     */
    @SuppressWarnings("WeakerAccess")
    protected void exploreWithWorkers(Integer sourceId) {
        Shrine source = board.getShrine(sourceId);
        for (Integer id : board.getConnections(sourceId)) {
            Shrine target = board.getShrine(id);
            if ((null == target) && (0 < source.getNumWorker())) {
                source.doMoveOrder(id, Shrine.MovableType.WORKER, 1);
            }
        }
    }


}
