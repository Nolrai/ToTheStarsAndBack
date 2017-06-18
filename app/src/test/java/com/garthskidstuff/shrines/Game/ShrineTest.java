package com.garthskidstuff.shrines.Game;

import com.garthskidstuff.shrines.Game.Shrine.*;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * This is a serries of tests of the Shrine class.
 * Created by garthupshaw1 on 5/22/17.
 */
public class ShrineTest {
    private static int maxWorker = 100;
    private static int miningRateParts = (int)(Shrine.PARTS_MULTIPLIER * 1.5);
    private static int miningDegradationRateParts = Shrine.PARTS_MULTIPLIER / 1000;
    private static int workerRateParts = Shrine.PARTS_MULTIPLIER / 20;

    private Roller roller;

    @Before
    public void setup() {
        roller = new Roller(1);
    }


    @Test
    public void serialize_toJsonAndBack() {
        Gson gson = new Gson();
        Shrine shrine = makeBasicShrine("name", "imageId");
        String json = gson.toJson(shrine);
        Shrine newShrine = gson.fromJson(json, Shrine.class);

        assertThat(newShrine, is(shrine));
    }

    @Test
    public void doOrder_mineNotEnoughWorkers() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.doOrder(Order.MINE, 10);

        assertThat(shrine.getNumGold(), is(0));
    }

    @Test
    public void doOrder_mine1Success() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(1);
        shrine.doOrder(Order.MINE, 1);

        assertThat(shrine.getNumGoldParts(), is(miningRateParts));
        assertThat(shrine.getMiningRateParts(), is(miningRateParts - miningDegradationRateParts));
        assertThat(shrine.getNumWorker(), is(0));
        assertThat(shrine.getNumUsedWorker(), is(1));
    }

    @Test
    public void doOrder_buildFighterSuccess() {
        testBuildSuccess(Order.BUILD_FIGHTER);
    }

    @Test
    public void doOrder_buildFighterFailWorker() {
        testBuildFailWorker(Order.BUILD_FIGHTER);
    }

    @Test
    public void doOrder_buildFighterFailGold() {
        testBuildFailGold(Order.BUILD_FIGHTER);
    }

    @Test
    public void doOrder_buildFighterFailAltar() {
        testBuildFailAltar(Order.BUILD_FIGHTER);
    }

    @Test
    public void doOrder_buildAltarSuccess() {
        testBuildSuccess(Order.BUILD_ALTAR);
    }

    @Test
    public void doOrder_buildAltarFailWorker() {
        testBuildFailWorker(Order.BUILD_ALTAR);
    }

    @Test
    public void doOrder_buildAltarFailGold() {
        testBuildFailGold(Order.BUILD_ALTAR);
    }

    @Test
    public void doOrder_buildAltarFailAltar() {
        testBuildFailAltar(Order.BUILD_ALTAR);
    }

    @Test
    public void makeSavedState_restore() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setAllValues();
        String savedState = shrine.makeSavedState();

        Shrine newShrine = makeBasicShrine("name", "imageId");
        newShrine.restore(savedState);

        assertThat(shrine, is(newShrine));
    }

    @Test
    public void doOrder_workerInvariant() {
        int totalWorkers = 1;

        for (Order order : Order.values()) {
            Shrine shrine = makeBasicShrine("name", "imageId");
            shrine.setNumWorker(totalWorkers);
            shrine.doOrder(order, totalWorkers);

            assertThat(shrine.getNumWorker() + shrine.getNumUsedWorker(), is(totalWorkers));
        }
    }

    @Test
    public void doMoveOrder_success() {
        for (MovableType type : MovableType.values()) {
            testMoveOrder(type);
        }
    }

    @Test
    public void doMoveOrder_failMovable() {
        for (MovableType type : MovableType.values()) {
            testMoveOrderFailMovable(type);
        }
    }

    @Test
    public void doMoveOrder_failGold() {
        for (MovableType type : MovableType.values()) {
            if (0 < type.moveCost) {
                testMoveOrderFailGold(type);
            }
        }
    }

    @Test
    public void fight_trivial() {
        Shrine shrine = makeBasicShrine("name", "iamgeId");

        for (MovableType type : MovableType.values()) {
            shrine.addArrival(shrine.getName(), type, 1);

            shrine.fight(roller);

            assertThat(shrine.getMovableType(type), is(1));
        }
    }

    @Test
    public void fight_combat1Vs1SameType() {

        for (MovableType type : MovableType.values()) {
            if (0 < type.fight) {
                Shrine shrine = makeBasicShrine("name", "imageId");
                shrine.addArrival(shrine.getName(), type, 1);
                shrine.addArrival("enemy", type, 1);

                shrine.fight(roller);

                assertThat(shrine.getMovableType(type), is(1));
            }
        }
    }

    @Test
    public void fight_move1Vs1ZeroFight() {

        for (MovableType type : MovableType.values()) {
            if (0 == type.fight) {
                Shrine shrine = makeBasicShrine("name", "imageId");
                shrine.addArrival(shrine.getName(), type, 1);
                shrine.addArrival("enemy", type, 1);

                shrine.fight(roller);

                assertThat(shrine.getMovableType(type), is(2));
            }
        }
    }

    @Test
    public void fight_combat1Vs1DifferentType() {
        for (MovableType typeA : MovableType.values()) {
            for (MovableType typeB : MovableType.values()) {
                if (!Utils.equals(typeA, typeB) && (0 < typeA.fight) && (0 < typeB.fight)) {
                    Shrine shrine = makeBasicShrine("name", "imageId");
                    shrine.addArrival(shrine.getName(), typeA, 1);
                    shrine.addArrival("enemy", typeB, 1);

                    shrine.fight(roller);

                    assertThat(shrine.getMovableType(typeA) + shrine.getMovableType(typeB), is(1));
                }
            }
        }
    }

    @Test
    public void endTurn_unusedWorkersMine() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(90);

        shrine.endTurn();

        assertThat("" + shrine.getNumGoldParts() + " <= 0", (0 < shrine.getNumGoldParts()), is(true));
    }

    @Test
    public void endTurn_resetUsedWorkers() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumUsedWorker(1);
        shrine.setNumGold(100); // so they won't starve

        shrine.endTurn();

        assertThat(shrine.getNumUsedWorker(), is(0));
        assertThat(shrine.getNumWorker(), is(2));
    }

    @Test
    public void endTurn_resetUsedAltars() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumAltar(10);
        shrine.setNumUsedAltar(10);

        shrine.endTurn();

        assertThat(shrine.getNumUsedAltar(), is(0));
        assertThat(shrine.getNumAltar(), is(20));
    }

    @Test
    public void endTurn_workersStarveIfNoGold() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumUsedWorker(10);

        shrine.endTurn();

        assertThat(shrine.getNumWorker(), is(0));
    }

    @Test
    public void endTurn_workersDontStarveIfEnoughGold() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumUsedWorker(1);
        shrine.setNumGold(1);

        shrine.endTurn();

        assertThat(shrine.getNumWorker(), is(1));
    }

    @Test
    public void endTurn_someWorkersStarve() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumUsedWorker(2);
        shrine.setNumGold(1);

        shrine.endTurn();

        assertThat(shrine.getNumWorker(), is(1));
    }

    @Test
    public void endTurn_workersReproduce() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumUsedWorker(10);
        shrine.setNumGold(10);

        shrine.endTurn();

        assertThat(shrine.getNumWorkerParts(), is(10 * Shrine.PARTS_MULTIPLIER + 10 * workerRateParts));
    }

    @Test
    public void endTurn_workersDontGrowPastMax() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumUsedWorker(maxWorker - 1);
        shrine.setNumGold(maxWorker);

        shrine.endTurn();

        assertThat(shrine.getNumWorker(), is(maxWorker));
    }

    private Shrine makeBasicShrine(String name, String imageId) {
        Shrine shrine = new Shrine(name, imageId);
        shrine.initBasic(maxWorker, miningRateParts, miningDegradationRateParts, workerRateParts);
        return shrine;
    }
    
    private Shrine copyShrine(Shrine shrine) {
        Shrine copy = makeBasicShrine(shrine.getName(), shrine.getImageId());
        copy.setShrine(shrine);
        return copy;
    }

    private void testBuildSuccess(Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumGold(1);
        shrine.setNumAltar(1);
        shrine.doOrder(order, 1);

        assertThat(shrine.getNumGold(), is(0));
        assertThat(shrine.getNumAltar(), is(0));
        assertThat(shrine.getNumUsedAltar(), is(1));
        assertThat(shrine.getNumWorker(), is(0));
        assertThat(shrine.getNumUsedWorker(), is(1));

        switch(order) {
            case BUILD_ALTAR:
                assertThat(shrine.getNumAltarParts(), is(Shrine.PARTS_MULTIPLIER / MovableType.ALTAR.buildCost));
                break;
            case BUILD_FIGHTER:
                assertThat(shrine.getNumFighterParts(), is(Shrine.PARTS_MULTIPLIER / MovableType.FIGHTER.buildCost));
                break;
        }
    }

    private void testBuildFailWorker(Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumGold(1);
        shrine.setNumAltar(1);
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testBuildFailGold(Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumAltar(1);
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testBuildFailAltar(Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumGold(1);
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testMoveOrder(MovableType type) {
        int numToMove = 2;
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumGold(type.moveCost * numToMove);
        shrine.setMovableType(type, numToMove);
        String dest = "destination";
        shrine.doMoveOrder(dest, type, numToMove);

        assertThat(shrine.getDeparture(dest, type), is(numToMove));

        Map<String, Map<MovableType, Integer>> map = shrine.getDepartureMapCopy();
        assertThat(map.size(), is(1));
        Map<MovableType, Integer> subMap = map.get(dest);
        assertThat(subMap.size(), is(1));
        assertThat(subMap.get(type), is(numToMove));
    }

    private void testMoveOrderFailMovable(MovableType type) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.addDeparture("id", type, 1);
        shrine.setNumGold(type.moveCost);
        shrine.doMoveOrder("destination", type, 1);

        Shrine oldShrine = copyShrine(shrine);
        Map<String, Map<MovableType, Integer>> map = shrine.getDepartureMapCopy();

        assertThat(map.size(), is(1));
        assertThat(shrine, is(oldShrine));
    }

    private void testMoveOrderFailGold(MovableType type) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setMovableType(type, 1000);
        shrine.doMoveOrder("destination", type, 1);

        Shrine oldShrine = copyShrine(shrine);
        Map<String, Map<MovableType, Integer>> map = shrine.getDepartureMapCopy();

        assertThat(map.size(), is(0));
        assertThat(shrine, is(oldShrine));
    }

}
