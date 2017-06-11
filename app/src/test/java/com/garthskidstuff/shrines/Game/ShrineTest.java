package com.garthskidstuff.shrines.Game;

import com.google.gson.Gson;

import org.junit.Test;

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
        shrine.doOrder(Shrine.Order.MINE, 10);

        assertThat(shrine.getNumGold(), is(0));
    }

    @Test
    public void doOrder_mine1Success() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(1);
        shrine.doOrder(Shrine.Order.MINE, 1);

        assertThat(shrine.getNumGoldParts(), is(miningRateParts));
        assertThat(shrine.getMiningRateParts(), is(miningRateParts - miningDegradationRateParts));
        assertThat(shrine.getNumWorker(), is(0));
        assertThat(shrine.getNumUsedWorker(), is(1));
    }

    @Test
    public void doOrder_buildScoutSuccess() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumGold(1);
        shrine.setNumAltar(1);
        shrine.doOrder(Shrine.Order.BUILD_SCOUT, 1);

        assertThat(shrine.getNumGold(), is(0));
        assertThat(shrine.getNumAltar(), is(0));
        assertThat(shrine.getNumUsedAltar(), is(1));
        assertThat(shrine.getNumWorker(), is(0));
        assertThat(shrine.getNumUsedWorker(), is(1));
    }

    @Test
    public void doOrder_buildScoutFailWorker() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumGold(1);
        shrine.setNumAltar(1);
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(Shrine.Order.BUILD_SCOUT, 1);

        assertThat(shrine, is(oldShrine));
    }

    @Test
    public void doOrder_buildScoutFailGold() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumAltar(1);
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(Shrine.Order.BUILD_SCOUT, 1);

        assertThat(shrine, is(oldShrine));
    }

    @Test
    public void doOrder_buildScoutFailAltar() {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumWorker(1);
        shrine.setNumGold(1);
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(Shrine.Order.BUILD_SCOUT, 1);

        assertThat(shrine, is(oldShrine));
    }

    @Test
    public void doOrder_loadGoldSuccess() {
        testLoadSuccess(Shrine.Order.LOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_loadGoldFailGold() {
        testLoadFailResource(Shrine.Order.LOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_loadGoldFailCargoEmpty() {
        testLoadFailCargo(Shrine.Order.LOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_loadAltarSuccess() {
        testLoadSuccess(Shrine.Order.LOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_loadAltarFailAltar() {
        testLoadFailResource(Shrine.Order.LOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_loadAltarFailCargoEmpty() {
        testLoadFailCargo(Shrine.Order.LOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_loadWorkerSuccess() {
        testLoadSuccess(Shrine.Order.LOAD_CARGO_WORKER);
    }

    @Test
    public void doOrder_loadWorkerFailWorker() {
        testLoadFailResource(Shrine.Order.LOAD_CARGO_WORKER);
    }

    @Test
    public void doOrder_loadWorkerFailCargoEmpty() {
        testLoadFailCargo(Shrine.Order.LOAD_CARGO_WORKER);
    }

    @Test
    public void doOrder_unloadGoldSuccess() {
        testUnloadSuccess(Shrine.Order.UNLOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_unloadGoldFailCargo() {
        testUnloadFailCargo(Shrine.Order.UNLOAD_CARGO_GOLD);
    }

    @Test
    public void doOrder_unloadAltarSuccess() {
        testUnloadSuccess(Shrine.Order.UNLOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_unloadAltarFailCargo() {
        testUnloadFailCargo(Shrine.Order.UNLOAD_CARGO_ALTAR);
    }

    @Test
    public void doOrder_unloadWorkerSuccess() {
        testUnloadSuccess(Shrine.Order.UNLOAD_CARGO_WORKER);
    }

    @Test
    public void doOrder_loadWorkerFailCargo() {
        testUnloadFailCargo(Shrine.Order.UNLOAD_CARGO_WORKER);
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

        for (Shrine.Order order : Shrine.Order.values()) {
            Shrine shrine = makeBasicShrine("name", "imageId");
            shrine.setNumWorker(totalWorkers);
            shrine.doOrder(order, totalWorkers);

            assertThat(shrine.getNumWorker() + shrine.getNumUsedWorker() + shrine.getNumCargoWorker(), is(totalWorkers));
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

    private void testLoadSuccess(Shrine.Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        switch (order) {
            case LOAD_CARGO_ALTAR:
                shrine.setNumAltar(1);
                break;
            case LOAD_CARGO_GOLD:
                shrine.setNumGold(1);
                break;
            case LOAD_CARGO_WORKER:
                shrine.setNumWorker(1);
                break;
        }
        shrine.setNumCargoEmpty(1);
        shrine.doOrder(order, 1);

        switch (order) {
            case LOAD_CARGO_ALTAR:
                assertThat(shrine.getNumAltar(), is(0));
                assertThat(shrine.getNumCargoAltar(), is(1));
                break;
            case LOAD_CARGO_GOLD:
                assertThat(shrine.getNumGold(), is(0));
                assertThat(shrine.getNumCargoGold(), is(1));
                break;
            case LOAD_CARGO_WORKER:
                assertThat(shrine.getNumWorker(), is(0));
                assertThat(shrine.getNumCargoWorker(), is(1));
                break;
        }
        assertThat(shrine.getNumCargoEmpty(), is(0));
    }

    private void testLoadFailResource(Shrine.Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        shrine.setNumCargoEmpty(1);
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testLoadFailCargo(Shrine.Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        switch (order) {
            case LOAD_CARGO_ALTAR:
                shrine.setNumAltar(1);
                break;
            case LOAD_CARGO_GOLD:
                shrine.setNumGold(1);
                break;
            case LOAD_CARGO_WORKER:
                shrine.setNumWorker(1);
                break;
        }
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }

    private void testUnloadSuccess(Shrine.Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        switch (order) {
            case UNLOAD_CARGO_ALTAR:
                shrine.setNumCargoAltar(1);
                break;
            case UNLOAD_CARGO_GOLD:
                shrine.setNumCargoGold(1);
                break;
            case UNLOAD_CARGO_WORKER:
                shrine.setNumCargoWorker(1);
                break;
        }
        shrine.doOrder(order, 1);

        switch (order) {
            case UNLOAD_CARGO_ALTAR:
                assertThat(shrine.getNumAltar(), is(1));
                assertThat(shrine.getNumCargoAltar(), is(0));
                break;
            case UNLOAD_CARGO_GOLD:
                assertThat(shrine.getNumGold(), is(1));
                assertThat(shrine.getNumCargoGold(), is(0));
                break;
            case UNLOAD_CARGO_WORKER:
                assertThat(shrine.getNumWorker(), is(1));
                assertThat(shrine.getNumCargoWorker(), is(0));
                break;
        }
        assertThat(shrine.getNumCargoEmpty(), is(1));
    }

    private void testUnloadFailCargo(Shrine.Order order) {
        Shrine shrine = makeBasicShrine("name", "imageId");
        Shrine oldShrine = copyShrine(shrine);
        shrine.doOrder(order, 1);

        assertThat(shrine, is(oldShrine));
    }
}
