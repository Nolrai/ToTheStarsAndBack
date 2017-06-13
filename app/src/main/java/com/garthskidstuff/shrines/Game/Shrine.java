package com.garthskidstuff.shrines.Game;

/**
 * Created by garthupshaw1 on 5/10/17.
 * A single node in the World graph
 */

public class Shrine  {
    private final String name;
    
    final static int PARTS_MULTIPLIER = 1000;

    private final String imageId;

    private int maxPopulation; // This is really final, but set in an init call AND is the actual int -- not 100th

    private int numWorkersParts;

    private int numUsedWorkersParts;

    private int numAltersParts;

    private int miningRateParts;  // amount of gold generated when 1 worker mines

    private int miningDegradationRateParts;  // mining rate drops each time a worker mines

    private int numGoldParts;

    private int numScoutParts;

    private int numFighterParts;

    private int numCargoEmptyParts;

    private int numCargoWorkers;

    private int numCargoGold;

    enum Order { MINE, BUILD_SCOUT };

    public Shrine(String name, String imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public void initBasic(int maxPopulation, int miningRateParts, int miningDegradationRateParts) {
        this.maxPopulation = maxPopulation;
        this.miningRateParts = miningRateParts;
        this.miningDegradationRateParts = miningDegradationRateParts;
    }

    public void initHome(int maxPopulation, int miningRate, int miningDegradationRateParts,
                         int numWorkers, int numAlters, int numGold) {
        initBasic(maxPopulation, miningRate, miningDegradationRateParts);
        
        numWorkersParts = numWorkers * PARTS_MULTIPLIER;
        numAltersParts = numAlters * PARTS_MULTIPLIER;
        numGoldParts = numGold * PARTS_MULTIPLIER;
    }


    public String getName() {
        return name;
    }

    public String getImageId() {
        return imageId;
    }

    public int getMaxPopulation() {
        return maxPopulation;
    }

    public void setMaxPopulation(int maxPopulation) {
        this.maxPopulation = maxPopulation;
    }

    public int getNumWorkersParts() {
        return numWorkersParts;
    }

    public int getNumWorkers() {
        return numWorkersParts / PARTS_MULTIPLIER;
    }

    public void setNumWorkersParts(int numWorkersParts) {
        this.numWorkersParts = numWorkersParts;
    }

    public void setNumWorkers(int numWorkers) {
        this.numWorkersParts = numWorkers * PARTS_MULTIPLIER;
    }

    public int getNumUsedWorkersParts() {
        return numUsedWorkersParts;
    }

    public int getNumUsedWorkers() {
        return numUsedWorkersParts / PARTS_MULTIPLIER;
    }

    public void setNumUsedWorkersParts(int numUsedWorkersParts) {
        this.numUsedWorkersParts = numUsedWorkersParts;
    }

    public void setNumUsedWorkers(int numUsedWorkers) {
        this.numUsedWorkersParts = numUsedWorkers * PARTS_MULTIPLIER;
    }

    public int getNumAltersParts() {
        return numAltersParts;
    }

    public int getNumAlters() {
        return numAltersParts / PARTS_MULTIPLIER;
    }

    public void setNumAltersParts(int numAltersParts) {
        this.numAltersParts = numAltersParts;
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

    public int getNumGold() {
        return numGoldParts / PARTS_MULTIPLIER;
    }

    public void setNumGoldParts(int numGoldParts) {
        this.numGoldParts = numGoldParts;
    }

    public int getNumScoutParts() {
        return numScoutParts;
    }

    public int getNumScout() {
        return numScoutParts / PARTS_MULTIPLIER;
    }

    public void setNumScoutParts(int numScoutParts) {
        this.numScoutParts = numScoutParts;
    }

    public int getNumFighterParts() {
        return numFighterParts;
    }

    public int getNumFighter() {
        return numFighterParts / PARTS_MULTIPLIER;
    }

    public void setNumFighterParts(int numFighterParts) {
        this.numFighterParts = numFighterParts;
    }

    public int getNumCargoEmptyParts() {
        return numCargoEmptyParts;
    }

    public int getNumCargoEmpty() {
        return numCargoEmptyParts / PARTS_MULTIPLIER;
    }

    public void setNumCargoEmptyParts(int numCargoEmptyParts) {
        this.numCargoEmptyParts = numCargoEmptyParts;
    }

    public int getNumCargoWorkers() {
        return numCargoWorkers;
    }

    public void setNumCargoWorkers(int numCargoWorkers) {
        this.numCargoWorkers = numCargoWorkers;
    }

    public int getNumCargoGold() {
        return numCargoGold;
    }

    public void setNumCargoGoldParts(int numCargoGold) {
        this.numCargoGold = numCargoGold;
    }


    void doOrder(Order order, int num) {
        int numParts = num * PARTS_MULTIPLIER;
        switch (order) {
            case MINE:
                if (numParts <= numWorkersParts) {
                    useWorkers(num);
                    numGoldParts += num * miningRateParts;
                    miningRateParts -= num * miningDegradationRateParts;
                }
                break;
            case BUILD_SCOUT:
                if ((numParts <= numWorkersParts) {
                    useWorkers(num);

                }
                break;
        }
    }

    private void useWorkers(int num) {
        int numParts = num * PARTS_MULTIPLIER;
        numUsedWorkersParts += numParts;
        numWorkersParts -= numParts;
    }

    @Override
    public String toString() {
        return "Shrine{" +
                "name='" + name + '\'' +
                ", imageId='" + imageId + '\'' +
                ", maxPopulation=" + maxPopulation +
                ", numWorkersParts=" + numWorkersParts +
                ", numAltersParts=" + numAltersParts +
                ", miningRateParts=" + miningRateParts +
                ", miningDegradationRateParts=" + miningDegradationRateParts +
                ", numGoldParts=" + numGoldParts +
                ", numScoutParts=" + numScoutParts +
                ", numFighterParts=" + numFighterParts +
                ", numCargoEmptyParts=" + numCargoEmptyParts +
                ", numCargoWorkers=" + numCargoWorkers +
                ", numCargoGold=" + numCargoGold +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shrine shrine = (Shrine) o;

        if (maxPopulation != shrine.maxPopulation) return false;
        if (numWorkersParts != shrine.numWorkersParts) return false;
        if (numAltersParts != shrine.numAltersParts) return false;
        if (miningRateParts != shrine.miningRateParts) return false;
        if (miningDegradationRateParts != shrine.miningDegradationRateParts) return false;
        if (numGoldParts != shrine.numGoldParts) return false;
        if (numScoutParts != shrine.numScoutParts) return false;
        if (numFighterParts != shrine.numFighterParts) return false;
        if (numCargoEmptyParts != shrine.numCargoEmptyParts) return false;
        if (numCargoWorkers != shrine.numCargoWorkers) return false;
        if (numCargoGold != shrine.numCargoGold) return false;
        if (!name.equals(shrine.name)) return false;
        return imageId.equals(shrine.imageId);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + imageId.hashCode();
        result = 31 * result + maxPopulation;
        result = 31 * result + numWorkersParts;
        result = 31 * result + numAltersParts;
        result = 31 * result + miningRateParts;
        result = 31 * result + miningDegradationRateParts;
        result = 31 * result + numGoldParts;
        result = 31 * result + numScoutParts;
        result = 31 * result + numFighterParts;
        result = 31 * result + numCargoEmptyParts;
        result = 31 * result + numCargoWorkers;
        result = 31 * result + numCargoGold;
        return result;
    }
}
