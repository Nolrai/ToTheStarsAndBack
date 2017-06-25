package com.garthskidstuff.shrines.Game;

import com.garthskidstuff.shrines.Game.Shrine.MovableType;
import com.garthskidstuff.shrines.Game.Shrine.Order;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * This is a series of tests of the Shrine class.
 * Created by garthupshaw1 on 5/22/17.
 */
public class ShrineTest extends BaseTest {
    private static int maxWorker = 100;
    private static int miningRateParts = (int)(Shrine.PARTS_MULTIPLIER * 1.5);
    private static int miningDegradationRateParts = Shrine.PARTS_MULTIPLIER / 1000;
    private static int workerRateParts = Shrine.PARTS_MULTIPLIER / 20;

    private Roller roller;

    @Before
    public void setup() {
        super.setup();
        roller = new Roller(1);
    }


    @Test
    public void serialize_toJsonAndBack() {
        Gson gson = new Gson();
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        String json = gson.toJson(shrine);
        Shrine newShrine = gson.fromJson(json, Shrine.class);

        assertThat(newShrine, is(shrine));
    }

    @Test
    public void doOrder_log() {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.doOrder(Order.MINE, 10);

        verify(testLogs).i(eq(Shrine.TAG), eq(Shrine.ORDER + Order.MINE + " " + Shrine.NUM + 10));
    }

    @Test
    public void doOrder_mineNotEnoughWorkers() {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.doOrder(Order.MINE, 10);

        assertThat(shrine.getNumGold(), is(0));
    }

    @Test
    public void doOrder_mine1Success() {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.setNumWorker(1);
        shrine.doOrder(Order.MINE, 1);

        assertThat(shrine.getNumGoldParts(), is(miningRateParts - miningDegradationRateParts/2));
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
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.setAllValues();
        String savedState = shrine.makeSavedState();

        Shrine newShrine = makeBasicShrine(0, "name", "imageId");
        newShrine.restore(savedState);

        assertThat(shrine, is(newShrine));
    }

    @Test
    public void doOrder_workerInvariant() {
        int totalWorkers = 5;

        for (Order order : Order.values()) {
            Shrine shrine = makeBasicShrine(0, "name", "imageId");
            shrine.setNumWorker(totalWorkers);
            shrine.doOrder(order, totalWorkers);

            assertThat(shrine.getNumWorker() + shrine.getNumUsedWorker(), is(totalWorkers));
        }
    }

    @Test
    public void doOrder_goldInvariant() {
        int totalWorkers = 5;
        int startValue;

        for (Order order : Order.values()) {
            Shrine shrine = makeBasicShrine(0, "name", "imageId");
            shrine.setNumGold(100);
            startValue = totalValueParts(shrine);
            shrine.setNumWorker(totalWorkers);
            shrine.doOrder(order, totalWorkers);

            assertThat(totalValueParts(shrine), is(startValue));
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
        Shrine shrine = makeBasicShrine(0, "name", "iamgeId");

        for (MovableType type : MovableType.values()) {
            shrine.addArrival(shrine.getId(), type, 1);

            shrine.fight(roller);

            assertThat(shrine.getMovableType(type), is(1));
        }
    }

    @Test
    public void fight_combat1Vs1SameType() {

        for (MovableType type : MovableType.values()) {
            if (0 < type.fight) {
                Shrine shrine = makeBasicShrine(0, "name", "imageId");
                shrine.addArrival(shrine.getId(), type, 1);
                shrine.addArrival(1, type, 1);

                shrine.fight(roller);

                assertThat(shrine.getMovableType(type), is(1));
            }
        }
    }

    @Test
    public void fight_move1Vs1ZeroFight() {

        for (MovableType type : MovableType.values()) {
            if (0 == type.fight) {
                Shrine shrine = makeBasicShrine(0, "name", "imageId");
                shrine.addArrival(shrine.getId(), type, 1);
                shrine.addArrival(1, type, 1);

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
                    Shrine shrine = makeBasicShrine(0, "name", "imageId");
                    shrine.addArrival(shrine.getId(), typeA, 1);
                    shrine.addArrival(1, typeB, 1);

                    shrine.fight(roller);

                    assertThat(shrine.getMovableType(typeA) + shrine.getMovableType(typeB), is(1));
                }
            }
        }
    }

    @Test
    public void endTurn_unusedWorkersMine() {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.setNumWorker(90);

        shrine.endTurn();

        assertThat("" + shrine.getNumGoldParts() + " <= 0", (0 < shrine.getNumGoldParts()), is(true));
    }

    @Test
    public void endTurn_resetUsedWorkers() {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumUsedWorker(1);
        shrine.setNumGold(100); // so they won't starve

        shrine.endTurn();

        assertThat(shrine.getNumUsedWorker(), is(0));
        assertThat(shrine.getNumWorker(), is(2));
    }

    @Test
    public void endTurn_resetUsedAltars() {
        Shrine shrine = makeBasicShrine(1, "name", "imageId");
        shrine.setNumAltar(10);
        shrine.setNumUsedAltar(10);

        shrine.endTurn();

        assertThat(shrine.getNumUsedAltar(), is(0));
        assertThat(shrine.getNumAltar(), is(20));
    }

    @Test
    public void endTurn_workersStarveIfNoGold() {
        Shrine shrine = makeBasicShrine(1, "name", "imageId");
        shrine.setNumUsedWorker(10);

        shrine.endTurn();

        assertThat(shrine.getNumWorker(), is(0));
    }

    @Test
    public void endTurn_workersDontStarveIfEnoughGold() {
        Shrine shrine = makeBasicShrine(1, "name", "imageId");
        shrine.setNumUsedWorker(1);
        shrine.setNumGold(1);

        shrine.endTurn();

        assertThat(shrine.getNumWorker(), is(1));
    }

    @Test
    public void endTurn_someWorkersStarve() {
        Shrine shrine = makeBasicShrine(1, "name", "imageId");
        shrine.setNumUsedWorker(2);
        shrine.setNumGold(1);

        shrine.endTurn();

        assertThat(shrine.getNumWorker(), is(1));
    }

    @Test
    public void endTurn_workersReproduce() {
        Shrine shrine = makeBasicShrine(1, "name", "imageId");
        shrine.setNumUsedWorker(10);
        shrine.setNumGold(10);

        shrine.endTurn();

        assertThat(shrine.getNumWorkerParts(), is(10 * Shrine.PARTS_MULTIPLIER + 10 * workerRateParts));
    }

    @Test
    public void endTurn_workersDontGrowPastMax() {
        Shrine shrine = makeBasicShrine(1, "name", "imageId");
        shrine.setNumUsedWorker(maxWorker - 1);
        shrine.setNumGold(maxWorker);

        shrine.endTurn();

        assertThat(shrine.getNumWorker(), is(maxWorker));
    }

    private Shrine makeBasicShrine(int id, String name, String imageId) {
        Shrine shrine = new Shrine(id, name, imageId);
        shrine.initBasic(maxWorker, miningRateParts, miningDegradationRateParts, workerRateParts);
        return shrine;
    }

    private void testBuildSuccess(Order order) {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
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
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.setNumGold(1);
        shrine.setNumAltar(1);
        Shrine oldShrine = shrine.cloneShrine(0);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testBuildFailGold(Order order) {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumAltar(1);
        Shrine oldShrine = shrine.cloneShrine(0);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testBuildFailAltar(Order order) {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumGold(1);
        Shrine oldShrine = shrine.cloneShrine(0);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testMoveOrder(MovableType type) {
        int numToMove = 2;
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.setNumGold(type.moveCost * numToMove);
        shrine.setMovableType(type, numToMove);
        Integer destId = 1;
        shrine.doMoveOrder(destId, type, numToMove);

        assertThat(shrine.getDeparture(destId, type), is(numToMove));

        Map<Integer, Map<MovableType, Integer>> map = shrine.getDepartureMapCopy();
        assertThat(map.size(), is(1));
        Map<MovableType, Integer> subMap = map.get(destId);
        assertThat(subMap.size(), is(1));
        assertThat(subMap.get(type), is(numToMove));
    }

    private void testMoveOrderFailMovable(MovableType type) {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.addDeparture(0, type, 1);
        shrine.setNumGold(type.moveCost);
        shrine.doMoveOrder(1, type, 1);

        Shrine oldShrine = shrine.cloneShrine(0);
        Map<Integer, Map<MovableType, Integer>> map = shrine.getDepartureMapCopy();

        assertThat(map.size(), is(1));
        assertThat(shrine, is(oldShrine));
    }

    private void testMoveOrderFailGold(MovableType type) {
        Shrine shrine = makeBasicShrine(0, "name", "imageId");
        shrine.setMovableType(type, 1000);
        shrine.doMoveOrder(1, type, 1);

        Shrine oldShrine = shrine.cloneShrine(0);
        Map<Integer, Map<MovableType, Integer>> map = shrine.getDepartureMapCopy();

        assertThat(map.size(), is(0));
        assertThat(shrine, is(oldShrine));
    }

    private Integer totalValueParts(Shrine shrine) {
        Integer value = null;
        if (null != shrine) {
            value = shrine.getNumGoldParts();
            // Gold in built things
            value += shrine.getNumAltarParts() * Shrine.BUILD_ALTAR_COST;
            value += shrine.getNumFighterParts() * Shrine.BUILD_FIGHTER_COST;

            // Gold not mined yet
            value += (int) (((long) shrine.getMiningRateParts() * (long) shrine.getMiningRateParts()) /
                    (long) (shrine.getMiningDegradationRateParts() * 2));
        }
        return value;
    }

}
