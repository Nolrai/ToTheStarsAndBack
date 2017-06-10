package com.garthskidstuff.shrines.Game;

import android.support.annotation.NonNull;

/**
 * Created by garthupshaw1 on 5/10/17.
 * A single node in the World graph
 */

public class Shrine implements Comparable {
    private final String name;
    
    private final static int PARTS_MULTIPLIER = 1000;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String imageId;

    private int maxPopulation; // This is really final, but set in an init call AND is the actual int -- not 100th

    private int numWorkersParts;

    private int numAltersParts;

    private int miningRateParts;  // amount of gold generated when 1 worker mines

    private int miningDegradationRateParts;  // mining rate drops each time a worker mines

    private int numGoldParts;

    private int numScoutParts;

    private int numFighterParts;

    private int numCargoEmptyParts;

    private int numCargoWorkers;

    private int numCargoGold;

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

    @Override

    public int compareTo(@NonNull Object other) {
        if (other instanceof Shrine) {
            Shrine otherShrine = (Shrine) other;
            return name.compareTo(otherShrine.getName());
        }
        return -1;
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

}
