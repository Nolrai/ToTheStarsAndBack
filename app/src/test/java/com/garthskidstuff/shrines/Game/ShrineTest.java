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
    private static int maxPop = 100;
    private static int miningRateParts = 10;
    private static int miningDegradationRateParts = 1;


    @Test
    public void serialize_toJsonAndBack() {
        Gson gson = new Gson();
        Shrine shrine = makeBasicShrine();
        String json = gson.toJson(shrine);
        Shrine newShrine = gson.fromJson(json, Shrine.class);

        assertThat(newShrine, is(shrine));
    }

    @Test
    public void doOrder_mineNotEnoughWorkers() {
        Shrine shrine = makeBasicShrine();
        shrine.doOrder(Shrine.Order.MINE, 10);

        assertThat(shrine.getNumGold(), is(0));
    }

    @Test
    public void doOrder_mine1() {
        Shrine shrine = makeBasicShrine();
        shrine.setNumWorkers(1);
        shrine.doOrder(Shrine.Order.MINE, 1);

        assertThat(shrine.getNumGoldParts(), is(miningRateParts));
        assertThat(shrine.getMiningRateParts(), is(miningRateParts - miningDegradationRateParts));
        assertThat(shrine.getNumWorkersParts(), is(0));
        assertThat(shrine.getNumUsedWorkersParts(), is(Shrine.PARTS_MULTIPLIER));
    }

    @Test
    public void doOrder_workersInvariant() {
        int totalWorkers = 1;

        for (Shrine.Order order : Shrine.Order.values()) {
            Shrine shrine = makeBasicShrine();
            shrine.setNumWorkers(totalWorkers);
            shrine.doOrder(order, totalWorkers);

            assertThat(shrine.getNumWorkers() + shrine.getNumUsedWorkers(), is(totalWorkers));
        }
    }

    private Shrine makeBasicShrine() {
        Shrine shrine = new Shrine("name", "imageId");
        shrine.initBasic(maxPop, miningRateParts, miningDegradationRateParts);
        return shrine;
    }
}
