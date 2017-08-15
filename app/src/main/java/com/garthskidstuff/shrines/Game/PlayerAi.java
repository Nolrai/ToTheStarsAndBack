package com.garthskidstuff.shrines.Game;

import java.util.List;
import java.util.Set;

/**
 * Created by garthupshaw1 on 7/3/17.
 *
 */

abstract class PlayerAi extends Player {
    Roller mRoller;

    public PlayerAi (Roller r) {
        mRoller = r;
    }

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

        Game.Shuffled<Integer> knownWorlds = new Game.Shuffled<>(board.getShrineIds());
        for (Integer sourceId : ) {
            for (Integer targetId : board.getConnections(sourceId)) {
                if (!board.getShrineIds().contains(targetId)) {//We found a unexplored shrine
                    Set<List<Integer>> paths = backwards.getPaths(targetId, new Utils.Func<Shrine, Boolean>() {
                        @Override
                        public Boolean apply(Shrine shrine) {
                            return isOwnedByMe(shrine) && shrine.getNumWorker() > 0;
                        }
                    }, Board.FindPathSettings.useAllShortest());
                    //TODO: Choose at random?

                    for (List<Integer> path : paths) {

                    }
                }
            }
        }

    }

    private boolean isOwnedByMe(Shrine shrine) {
        return shrine.getOwnerId() == PlayerAi.this.getId();
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
