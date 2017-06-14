package com.garthskidstuff.shrines.Game;

import android.support.v4.util.Pair;

import com.garthskidstuff.shrines.Game.Shrine.*;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.Map;
import java.util.Vector;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * This is a serries of tests of the Shrine class.
 * Created by garthupshaw1 on 5/22/17.
 */
public class ShrineTest {
    private static int maxWorker = 100;
    private static int miningRateParts = 10;
    private static int miningDegradationRateParts = 1;


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
    public void doOrder_buildScoutSuccess() {
        testBuildSuccess(Order.BUILD_SCOUT);
    }

    @Test
    public void doOrder_buildScoutFailWorker() {
        testBuildFailWorker(Order.BUILD_SCOUT);
    }

    @Test
    public void doOrder_buildScoutFailGold() {
        testBuildFailGold(Order.BUILD_SCOUT);
    }

    @Test
    public void doOrder_buildScoutFailAltar() {
        testBuildFailAltar(Order.BUILD_SCOUT);
    }

    @Test
    public void doOrder_buildCargoSuccess() {
        testBuildSuccess(Order.BUILD_CARGO);
    }

    @Test
    public void doOrder_buildCargoFailWorker() {
        testBuildFailWorker(Order.BUILD_CARGO);
    }

    @Test
    public void doOrder_buildCargoFailGold() {
        testBuildFailGold(Order.BUILD_CARGO);
    }

    @Test
    public void doOrder_buildCargoFailAltar() {
        testBuildFailAltar(Order.BUILD_CARGO);
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
    public void doOrder_loadGoldSuccess() {
        testLoadSuccess(Order.LOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_loadGoldFailGold() {
        testLoadFailResource(Order.LOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_loadGoldFailCargoEmpty() {
        testLoadFailCargo(Order.LOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_loadAltarSuccess() {
        testLoadSuccess(Order.LOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_loadAltarFailAltar() {
        testLoadFailResource(Order.LOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_loadAltarFailCargoEmpty() {
        testLoadFailCargo(Order.LOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_loadWorkerSuccess() {
        testLoadSuccess(Order.LOAD_CARGO_WORKER);
    }

    @Test
    public void doOrder_loadWorkerFailWorker() {
        testLoadFailResource(Order.LOAD_CARGO_WORKER);
    }

    @Test
    public void doOrder_loadWorkerFailCargoEmpty() {
        testLoadFailCargo(Order.LOAD_CARGO_WORKER);
    }

    @Test
    public void doOrder_unloadGoldSuccess() {
        testUnloadSuccess(Order.UNLOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_unloadGoldFailCargo() {
        testUnloadFailCargo(Order.UNLOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_unloadAltarSuccess() {
        testUnloadSuccess(Order.UNLOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_unloadAltarFailCargo() {
        testUnloadFailCargo(Order.UNLOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_unloadWorkerSuccess() {
        testUnloadSuccess(Order.UNLOAD_CARGO_WORKER);
    }

    @Test
    public void doOrder_loadWorkerFailCargo() {
        testUnloadFailCargo(Order.UNLOAD_CARGO_WORKER);
    }

    @Test
    public void doMoveOrder_scoutSuccess() {
        testMoveOrder(ShipType.SCOUT);
    }

    @Test
    public void doMoveOrder_scoutFail() {
        testMoveOrderFail(ShipType.SCOUT);
    }

    @Test
    public void doMoveOrder_fighterSuccess() {
        testMoveOrder(ShipType.FIGHTER);
    }

    @Test
    public void doMoveOrder_fighterFail() {
        testMoveOrderFail(ShipType.FIGHTER);
    }

    @Test
    public void doMoveOrder_cargoEmptySuccess() {
        testMoveOrder(ShipType.CARGO_EMPTY);
    }

    @Test
    public void doMoveOrder_cargoEmptyFail() {
        testMoveOrderFail(ShipType.CARGO_EMPTY);
    }

    @Test
    public void doMoveOrder_cargoGoldSuccess() {
        testMoveOrder(ShipType.CARGO_GOLD);
    }

    @Test
    public void doMoveOrder_cargoGoldFail() {
        testMoveOrderFail(ShipType.CARGO_GOLD);
    }

    @Test
    public void doMoveOrder_cargoAltarSuccess() {
        testMoveOrder(ShipType.CARGO_ALTAR);
    }

    @Test
    public void doMoveOrder_cargoAltarFail() {
        testMoveOrderFail(ShipType.CARGO_ALTAR);
    }

    @Test
    public void doMoveOrder_cargoWorkerSuccess() {
        testMoveOrder(ShipType.CARGO_WORKER);
    }

    @Test
    public void doMoveOrder_cargoWorkerFail() {
        testMoveOrderFail(ShipType.CARGO_WORKER);
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

            assertThat(shrine.getNumWorker() + shrine.getNumUsedWorker() + shrine.getNumShip(ShipType.CARGO_WORKER), is(totalWorkers));
        }
    }

    private Shrine makeBasicShrine(String name, String imageId) {
        Shrine shrine = new Shrine(name, imageId);
        shrine.initBasic(maxWorker, miningRateParts, miningDegradationRateParts);
        return shrine;
    }
    
    private Shrine copyShrine(Shrine shrine) {
        Shrine copy = makeBasicShrine(shrine.getName(), shrine.getImageId());
        copy.setShrine(shrine);
        return copy;
    }

    private void testLoadSuccess(Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        CargoType cargoType = shrine.getCargoType(order);
        shrine.setNumCargo(cargoType, 1);
        shrine.setNumShip(ShipType.CARGO_EMPTY, 1);
        shrine.doOrder(order, 1);

        assertThat(shrine.getNumCargo(cargoType), is(0));
        assertThat(shrine.getNumShip(shrine.getCargoShipType(cargoType)), is(1));
        assertThat(shrine.getNumShip(ShipType.CARGO_EMPTY), is(0));
    }

    private void testLoadFailResource(Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumShip(ShipType.CARGO_EMPTY, 1);
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testLoadFailCargo(Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        CargoType cargoType = shrine.getCargoType(order);
        shrine.setNumCargo(cargoType, 1);
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testUnloadSuccess(Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        CargoType cargoType = shrine.getCargoType(order);
        ShipType shipType = shrine.getCargoShipType(cargoType); 
        shrine.setNumShip(shipType, 1);
        shrine.doOrder(order, 1);

        assertThat(shrine.getNumCargo(cargoType), is(1));
        assertThat(shrine.getNumShip(shipType), is(0));
        assertThat(shrine.getNumShip(ShipType.CARGO_EMPTY), is(1));
    }

    private void testUnloadFailCargo(Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
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
                assertThat(shrine.getNumAltarParts(), is(Shrine.PARTS_MULTIPLIER / Shrine.BUILD_ALTAR_COST));
                break;
            case BUILD_CARGO:
                assertThat(shrine.getNumShipParts(ShipType.CARGO_EMPTY), is(Shrine.PARTS_MULTIPLIER / Shrine.BUILD_CARGO_COST));
                break;
            case BUILD_SCOUT:
                assertThat(shrine.getNumShipParts(ShipType.SCOUT), is(Shrine.PARTS_MULTIPLIER / Shrine.BUILD_SCOUT_COST));
                break;
            case BUILD_FIGHTER:
                assertThat(shrine.getNumShipParts(ShipType.FIGHTER), is(Shrine.PARTS_MULTIPLIER / Shrine.BUILD_FIGHTER_COST));
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

    private void testMoveOrder(ShipType type) {
        int numToMove = 2;
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumShip(type, numToMove);
        String dest = "destination";
        shrine.doMoveOrder(dest, type, numToMove);

        assertThat(shrine.getMove(dest, type), is(numToMove));

        Map<String, Map<ShipType, Integer>> map = shrine.getMovementMap();
        assertThat(map.size(), is(1));
        Map<ShipType, Integer> subMap = map.get(dest);
        assertThat(subMap.size(), is(1));
        assertThat(subMap.get(type), is(numToMove));
    }

    private void testMoveOrderFail(ShipType type) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.doMoveOrder("destination", type, 1);

        Shrine oldShrine = copyShrine(shrine);
        Map<String, Map<ShipType, Integer>> map = shrine.getMovementMap();

        assertThat(map.size(), is(0));
        assertThat(shrine, is(oldShrine));
    }

}
