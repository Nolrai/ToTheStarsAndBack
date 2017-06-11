package com.garthskidstuff.shrines.Game;

import com.google.gson.Gson;

/**
 * Created by garthupshaw1 on 5/10/17.
 * A single node in the World graph
 */

public class Shrine  {
    private final String name;

    final static int PARTS_MULTIPLIER = 1000;

    // All costs need to divide 1000 evenly.
    final static int BUILD_SCOUT_COST = 1;
    final static int BUILD_CARGO_COST = 2;
    final static int BUILD_FIGHTER_COST = 5;
    final static int BUILD_ALTAR_COST = 10;

    private final String imageId;

    private int maxWorkers; // This is really final, but set in an init call AND is the actual int -- not 100th

    private int numWorkerParts;

    private int numUsedWorker;

    private int numAltarParts;

    private int numUsedAltar;

    private int miningRateParts;  // amount of gold generated when 1 worker mines

    private int miningDegradationRateParts;  // mining rate drops each time a worker mines

    private int numGoldParts;

    private int numScoutParts;

    private int numFighterParts;

    private int numCargoEmptyParts;

    private int numCargoWorker;

    private int numCargoGold;

    private int numCargoAltar;

    enum Order {
        MINE,
        BUILD_SCOUT,
        BUILD_CARGO,
        BUILD_FIGHTER,
        BUILD_ALTAR,
        LOAD_CARGO_GOLD,
        LOAD_CARGO_WORKER,
        LOAD_CARGO_ALTAR,
        UNLOAD_CARGO_GOLD,
        UNLOAD_CARGO_WORKER,
        UNLOAD_CARGO_ALTAR,
    };

    public Shrine(String name, String imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public void initBasic(int maxPopulation, int miningRateParts, int miningDegradationRateParts) {
        this.maxWorkers = maxPopulation;
        this.miningRateParts = miningRateParts;
        this.miningDegradationRateParts = miningDegradationRateParts;
    }

    public void initHome(int maxPopulation, int miningRate, int miningDegradationRateParts,
                         int numWorkers, int numAlters, int numGold) {
        initBasic(maxPopulation, miningRate, miningDegradationRateParts);
        
        numWorkerParts = numWorkers * PARTS_MULTIPLIER;
        numAltarParts = numAlters * PARTS_MULTIPLIER;
        numGoldParts = numGold * PARTS_MULTIPLIER;
    }

    public String getName() {
        return name;
    }

    public String getImageId() {
        return imageId;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public void setMaxWorkers(int maxWorkers) {
        this.maxWorkers = maxWorkers;
    }

    public int getNumWorkerParts() {
        return numWorkerParts;
    }

    public void setNumWorkerParts(int numWorkerParts) {
        this.numWorkerParts = numWorkerParts;
    }

    public int getNumUsedWorker() {
        return numUsedWorker;
    }

    public void setNumUsedWorker(int numUsedWorker) {
        this.numUsedWorker = numUsedWorker;
    }

    public int getNumAltarParts() {
        return numAltarParts;
    }

    public void setNumAltarParts(int numAltarParts) {
        this.numAltarParts = numAltarParts;
    }

    public int getNumUsedAltar() {
        return numUsedAltar;
    }

    public void setNumUsedAltar(int numUsedAltar) {
        this.numUsedAltar = numUsedAltar;
    }

    public int getMiningRateParts() {
        return miningRateParts;
    }

    public void setMiningRateParts(int miningRateParts) {
        this.miningRateParts = miningRateParts;
    }

    public int getMiningDegradationRateParts() {
        return miningDegradationRateParts;
    }

    public void setMiningDegradationRateParts(int miningDegradationRateParts) {
        this.miningDegradationRateParts = miningDegradationRateParts;
    }

    public int getNumGoldParts() {
        return numGoldParts;
    }

    public void setNumGoldParts(int numGoldParts) {
        this.numGoldParts = numGoldParts;
    }

    public int getNumScoutParts() {
        return numScoutParts;
    }

    public void setNumScoutParts(int numScoutParts) {
        this.numScoutParts = numScoutParts;
    }

    public int getNumFighterParts() {
        return numFighterParts;
    }

    public void setNumFighterParts(int numFighterParts) {
        this.numFighterParts = numFighterParts;
    }

    public int getNumCargoEmptyParts() {
        return numCargoEmptyParts;
    }

    public void setNumCargoEmptyParts(int numCargoEmptyParts) {
        this.numCargoEmptyParts = numCargoEmptyParts;
    }

    public int getNumCargoWorker() {
        return numCargoWorker;
    }

    public void setNumCargoWorker(int numCargoWorker) {
        this.numCargoWorker = numCargoWorker;
    }

    public int getNumCargoGold() {
        return numCargoGold;
    }

    public void setNumCargoGold(int numCargoGold) {
        this.numCargoGold = numCargoGold;
    }

    public int getNumCargoAltar() {
        return numCargoAltar;
    }

    public void setNumCargoAltar(int numCargoAltar) {
        this.numCargoAltar = numCargoAltar;
    }

    // The following are convenience functions to get/set whole integer values
    public int getNumGold() {
        return numGoldParts / PARTS_MULTIPLIER;
    }

    public void setNumGold(int num) {
        numGoldParts = num * PARTS_MULTIPLIER;
    }

    public int getNumWorker() {
        return numWorkerParts / PARTS_MULTIPLIER;
    }

    public void setNumWorker(int num) {
        numWorkerParts = num * PARTS_MULTIPLIER;
    }

    public int getNumAltar() {
        return numAltarParts / PARTS_MULTIPLIER;
    }

    public void setNumAltar(int num) {
        numAltarParts = num * PARTS_MULTIPLIER;
    }

    public int getNumScout() {
        return numScoutParts / PARTS_MULTIPLIER;
    }

    public void setNumScout(int num) {
        numScoutParts = num * PARTS_MULTIPLIER;
    }

    public int getNumCargoEmpty() {
        return numCargoEmptyParts / PARTS_MULTIPLIER;
    }

    public void setNumCargoEmpty(int num) {
        numCargoEmptyParts = num * PARTS_MULTIPLIER;
    }

    String makeSavedState() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    void restore(String savedState) {
        Gson gson = new Gson();
        Shrine shrine = gson.fromJson(savedState, Shrine.class);
        setShrine(shrine);
    }

    void doOrder(Order order, int num) {
        int numParts = num * PARTS_MULTIPLIER;
        String savedState = makeSavedState();
        boolean success = true;
        switch (order) {
            case MINE:
                success = useWorkers(num);
                numGoldParts += num * miningRateParts;
                miningRateParts -= num * miningDegradationRateParts;
                break;

            case BUILD_SCOUT:
                success = payBuildCost(num);
                numScoutParts += (numParts / BUILD_SCOUT_COST);
                break;

            case BUILD_CARGO:
                success = payBuildCost(num);
                numCargoEmptyParts += (numParts / BUILD_CARGO_COST);
                break;

            case BUILD_FIGHTER:
                success = payBuildCost(num);
                numFighterParts += (numParts / BUILD_FIGHTER_COST);
                break;

            case BUILD_ALTAR:
                success = payBuildCost(num);
                numAltarParts += (numParts / BUILD_ALTAR_COST);
                break;

            case LOAD_CARGO_GOLD:
                success = loadUnloadCargoGold(num);
                break;

            case LOAD_CARGO_WORKER:
                success = loadUnloadCargoWorker(num);
                break;

            case LOAD_CARGO_ALTAR:
                success = loadUnloadCargoAltar(num);
                break;

            case UNLOAD_CARGO_GOLD:
                success = loadUnloadCargoGold(-num);
                break;

            case UNLOAD_CARGO_WORKER:
                success = loadUnloadCargoWorker(-num);
                break;

            case UNLOAD_CARGO_ALTAR:
                success = loadUnloadCargoAltar(-num);
                break;
        }
        if (!success) {
            restore(savedState);
        }
    }

    private boolean payBuildCost(int num) {
        return useWorkers(num) && useGold(num) && useAltars(num);
    }

    private boolean useWorkers(int num) {
        int numParts = num * PARTS_MULTIPLIER;
        numUsedWorker += num;
        numWorkerParts -= numParts;
        return (numWorkerParts >= 0);
    }

    private boolean useGold(int num) {
        int numParts = num * PARTS_MULTIPLIER;
        numGoldParts -= numParts;
        return (numGoldParts >= 0);
    }

    private boolean useAltars(int num) {
        int numParts = num * PARTS_MULTIPLIER;
        numUsedAltar += num;
        numAltarParts -= numParts;
        return (numAltarParts >= 0);
    }

    private boolean loadUnloadCargoGold(int num) { // num < 0 ==> unloading
        int numParts = num * PARTS_MULTIPLIER;
        numGoldParts -= numParts;
        numCargoEmptyParts -= numParts;
        numCargoGold += num;
        return (numGoldParts >= 0) && (numCargoEmptyParts >= 0) && (numCargoGold >= 0);
    }

    private boolean loadUnloadCargoWorker(int num) { // num < 0 ==> unloading
        int numParts = num * PARTS_MULTIPLIER;
        numWorkerParts -= numParts;
        numCargoEmptyParts -= numParts;
        numCargoWorker += num;
        return (numWorkerParts >= 0) && (numCargoEmptyParts >= 0) && (numCargoWorker >= 0);
    }

    private boolean loadUnloadCargoAltar(int num) { // num < 0 ==> unloading
        int numParts = num * PARTS_MULTIPLIER;
        numAltarParts -= numParts;
        numCargoEmptyParts -= numParts;
        numCargoAltar += num;
        return (numAltarParts >= 0) && (numCargoEmptyParts >= 0) && (numCargoAltar >= 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shrine shrine = (Shrine) o;

        if (maxWorkers != shrine.maxWorkers) return false;
        if (numWorkerParts != shrine.numWorkerParts) return false;
        if (numUsedWorker != shrine.numUsedWorker) return false;
        if (numAltarParts != shrine.numAltarParts) return false;
        if (numUsedAltar != shrine.numUsedAltar) return false;
        if (miningRateParts != shrine.miningRateParts) return false;
        if (miningDegradationRateParts != shrine.miningDegradationRateParts) return false;
        if (numGoldParts != shrine.numGoldParts) return false;
        if (numScoutParts != shrine.numScoutParts) return false;
        if (numFighterParts != shrine.numFighterParts) return false;
        if (numCargoEmptyParts != shrine.numCargoEmptyParts) return false;
        if (numCargoWorker != shrine.numCargoWorker) return false;
        if (numCargoGold != shrine.numCargoGold) return false;
        if (numCargoAltar != shrine.numCargoAltar) return false;
        if (!name.equals(shrine.name)) return false;
        return imageId.equals(shrine.imageId);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + imageId.hashCode();
        result = 31 * result + maxWorkers;
        result = 31 * result + numWorkerParts;
        result = 31 * result + numUsedWorker;
        result = 31 * result + numAltarParts;
        result = 31 * result + numUsedAltar;
        result = 31 * result + miningRateParts;
        result = 31 * result + miningDegradationRateParts;
        result = 31 * result + numGoldParts;
        result = 31 * result + numScoutParts;
        result = 31 * result + numFighterParts;
        result = 31 * result + numCargoEmptyParts;
        result = 31 * result + numCargoWorker;
        result = 31 * result + numCargoGold;
        result = 31 * result + numCargoAltar;
        return result;
    }

    // This does not set name and imageId
    public void setShrine(Shrine other) {
        maxWorkers = other.maxWorkers;
        numWorkerParts = other.numWorkerParts;
        numUsedWorker = other.numUsedWorker;
        numAltarParts = other.numAltarParts;
        numUsedAltar = other.numUsedAltar;
        miningRateParts = other.miningRateParts;
        miningDegradationRateParts = other.miningDegradationRateParts;
        numGoldParts = other.numGoldParts;
        numScoutParts = other.numScoutParts;
        numFighterParts = other.numFighterParts;
        numCargoEmptyParts = other.numCargoEmptyParts;
        numCargoWorker = other.numCargoWorker;
        numCargoGold = other.numCargoGold;
        numCargoAltar = other.numCargoAltar;
    }

    /**
     * Set all of a shrine's values -- for testing only
     */
    void setAllValues() {
        maxWorkers = 1;
        numWorkerParts = 2;
        numUsedWorker = 3;
        numAltarParts = 4;
        numUsedAltar = 5;
        miningRateParts = 6;
        miningDegradationRateParts = 7;
        numGoldParts = 8;
        numScoutParts = 9;
        numFighterParts = 10;
        numCargoEmptyParts = 11;
        numCargoWorker = 12;
        numCargoGold = 13;
        numCargoAltar = 14;
    }

    @Override
    public String toString() {
        return "Shrine{" +
                "name='" + name + '\'' +
                ", imageId='" + imageId + '\'' +
                ", maxWorkers=" + maxWorkers +
                ", numWorkerParts=" + numWorkerParts +
                ", numUsedWorker=" + numUsedWorker +
                ", numAltarParts=" + numAltarParts +
                ", numUsedAltar=" + numUsedAltar +
                ", miningRateParts=" + miningRateParts +
                ", miningDegradationRateParts=" + miningDegradationRateParts +
                ", numGoldParts=" + numGoldParts +
                ", numScoutParts=" + numScoutParts +
                ", numFighterParts=" + numFighterParts +
                ", numCargoEmptyParts=" + numCargoEmptyParts +
                ", numCargoWorker=" + numCargoWorker +
                ", numCargoGold=" + numCargoGold +
                ", numCargoAltar=" + numCargoAltar +
                '}';
    }


}
