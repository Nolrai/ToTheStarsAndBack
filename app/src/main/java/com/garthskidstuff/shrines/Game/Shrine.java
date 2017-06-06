package com.garthskidstuff.shrines.Game;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by garthupshaw1 on 5/10/17.
 * A single node in the World graph
 */

public class Shrine implements Comparable {
    private final String name;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String imageId;

    private final int maxPopulation;
    private int numWorkers;
    private int numAlters;
    private double miningRate;  // [0, 1] chance of getting a resource when 1 worker mines
    private int numGold;

    private int numDragonflies;

    private int numCargoEmpty;

    private int numCargoWorkers;

    private int numCargoGold;

    public Shrine(String name, String imageId, int maxPopulation) {
        this.name = name;
        this.imageId = imageId;
        this.maxPopulation = maxPopulation;
    }

    public String getName() {
        return name;
    }

    public int getNumWorkers() {
        return numWorkers;
    }

    public void setNumWorkers(int numWorkers) {
        this.numWorkers = numWorkers;
    }

    public int getNumAlters() {
        return numAlters;
    }

    public void setNumAlters(int numAlters) {
        this.numAlters = numAlters;
    }

    public double getMiningRate() {
        return miningRate;
    }

    public void setMiningRate(double miningRate) {
        this.miningRate = miningRate;
    }

    public int getMaxPopulation() {
        return maxPopulation;
    }

    public int getNumGold() {
        return numGold;
    }

    public void setNumGold(int numGold) {
        this.numGold = numGold;
    }

    public int getNumDragonflies() {
        return numDragonflies;
    }

    public void setNumDragonflies(int numDragonflies) {
        this.numDragonflies = numDragonflies;
    }

    public int getNumCargoEmpty() {
        return numCargoEmpty;
    }

    public void setNumCargoEmpty(int numCargoEmpty) {
        this.numCargoEmpty = numCargoEmpty;
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

    public void setNumCargoGold(int numCargoGold) {
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
        String s = name;

        return s;
    }
}
