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
    protected void exploreWithWorkers(int sourceId) {
        Shrine source = board.getShrine(sourceId);
        for (Integer id : board.getConnections(sourceId)) {
            Shrine target = board.getShrine(id);
            if ((null == target) && (0 < source.getNumWorker())) {
                source.doMoveOrder(id, Shrine.MovableType.WORKER, 1);
            }
        }
    }

    /**
     * for each unexplored connection find a worker to move closer
     */
    @SuppressWarnings("WeakerAccess")
    protected void moveWorkersTowardsExplore() {
        Board backwards = board.transpose();
        for (Integer sourceId : board.getShrineIds()) {
            for (Integer targetId : board.getConnections(sourceId)) {
                if (!board.getShrineIds().contains(targetId)) {//We found a unexplored shrine
                    backwards.getPaths(targetId, EndOn.HasWorker, Board.FindPathSettings.useAllShortest());
                }
            }
        }

    }

//    protected int buildWithReserve(int shrineId, boolean buildFighters, int workerReserve, int altarReserve) {
//        Shrine shrine = board.getShrine(shrineId);
//        int amount = 0;
//        int change = 2 ^ 8;
//        // Linear search, (if this gets called a lot we should optimise)
//
//
//    }

}
