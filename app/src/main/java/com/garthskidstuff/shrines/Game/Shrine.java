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

    public Set<List<Shrine>> getPathsTo(World world, Set<Shrine> knownShrines, Shrine end) {
        Paths paths = new Paths(this, end);
        makePathsTo(world, knownShrines, this, paths);
        return makeListOfPathsFrom(paths, this, end);
    }

    private static void makePathsTo(World world, Set<Shrine> knownShrines, Shrine start, Paths paths) {
        if (null == paths.get(start)) {
            List<Shrine> connections = world.get(start);
            if (null != connections) {
                List<Shrine> pathConnections = new ArrayList<Shrine>();
                for (Shrine shrine : connections) {
                    if (knownShrines.contains(shrine)) {
                        pathConnections.add(shrine);
                    }
                }
                paths.put(start, pathConnections);
                for (Shrine shrine : pathConnections) {
                    if (shrine != paths.end) {
                        makePathsTo(world, knownShrines, shrine, paths);
                    }
                }
            }
        }
    }

    private Set<List<Shrine>> makeListOfPathsFrom(Paths paths, Shrine start, Shrine end) {
        Set<List<Shrine>> allPaths = new HashSet<>();
        List<Shrine> path1 = new ArrayList<>();
        path1.add(start);
        allPaths.add(path1);

        boolean keepGoing = false;
        do {
            keepGoing = false;
            Set<List<Shrine>> newAllPaths = new HashSet<>();
            newAllPaths.addAll(allPaths);

            for (List<Shrine> path : allPaths) {
                Shrine endPath = path.get(path.size() - 1);
                if (!Utils.equals(endPath, end)) { // path is not terminated at destination
                    List<Shrine> connections = paths.get(endPath);
                    newAllPaths.remove(path);
                    if (null != connections) {
                        for (Shrine shrine : connections) {
                            if (!path.contains(shrine)) { // not a loop
                                List<Shrine> newPath = new ArrayList<>();
                                newPath.addAll(path);
                                newPath.add(shrine);
                                newAllPaths.add(newPath);
                                keepGoing = true;
                            }
                        }
                    }
                }
            }

            allPaths = newAllPaths;
        } while (keepGoing);

        return allPaths;
    }

    public static Comparator<List<Shrine>> SORT_SHORTEST_FIRST = new Comparator<List<Shrine>>() {
        @Override
        public int compare(List<Shrine> lhs, List<Shrine> rhs) {
            Integer lhsSize = lhs.size();
            Integer rhsSize = rhs.size();
            return lhsSize.compareTo(rhsSize);
        }
    };

    public static Comparator<List<Shrine>> SORT_BY_POPULATION = new Comparator<List<Shrine>>() {
        @Override
        public int compare(List<Shrine> lhs, List<Shrine> rhs) {
            Integer lhsSize = 0;
            for (Shrine shrine : lhs) {
                lhsSize += shrine.getNumWorkers();
            }
            Integer rhsSize = 0;
            for (Shrine shrine : rhs) {
                rhsSize += shrine.getNumWorkers();
            }
            return lhsSize.compareTo(rhsSize);
        }
    };

    public static List<List<Shrine>> sortPaths(Set<List<Shrine>> allPaths) {
        return sortPaths(allPaths, SORT_SHORTEST_FIRST);
    }

    public static List<List<Shrine>> sortPaths(Set<List<Shrine>> allPaths, Comparator<List<Shrine>> comparator) {
        List<List<Shrine>> sortedPaths = new ArrayList<>();

        for (List<Shrine> path : allPaths) {
            sortedPaths.add(path);
        }

        Collections.sort(sortedPaths, comparator);

        return sortedPaths;
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
}
