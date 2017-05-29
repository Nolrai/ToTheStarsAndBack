package com.garthskidstuff.shrines.Game;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by garthupshaw1 on 5/10/17.
 */

@SuppressWarnings({"DefaultFileTemplate", "WeakerAccess", "unused"})
public class Shrine implements Comparable {
    private final String name;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String imageId;

    private final List<Shrine> connections = new ArrayList<>();
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

    // Gets all shrines that can be reached (eventually) from here
    Set<Shrine> getConnectedComponent() {
        Set<Shrine> visited = new TreeSet<>();
        //noinspection StatementWithEmptyBody
        for (Iterator<Set<Shrine>> iter = new NthNeighbors(visited); iter.hasNext(); iter.next()) {
            //do nothing
        }
        return visited;
    }

    /**
     * Finds All shrines with shortest path from this is between minDistance and maxDistance (inclusive).
     *
     * @param minDistance Shrines closer then this are ignored.
     * @param maxDistance We stop before looking at shrines farther then this.
     * @return All shrines with shortest path from this is between minDistance and maxDistance (inclusive).
     */
    Set<Shrine> getNtoMthNeighbors(int minDistance, int maxDistance) {
        Set<Shrine> visited = new TreeSet<>();
        Set<Shrine> ret = new TreeSet<>();
        Iterator<Set<Shrine>> iter = new NthNeighbors(visited);
        for (int i = 1; i <= maxDistance && iter.hasNext(); i++) {
            Set<Shrine> now = iter.next();
            if (i >= minDistance) {
                ret.addAll(now);
            }
        }
        return ret;
    }

    /**
     * This is an iterator who's nth next produces the (not already reached) shrines n connections
     * downstream from this.
     *
     * It also adds every visited shrine to the set passed in with visited_.
     * (Where visited means "We have checked to see if it connects to new planets.")
     */
    protected class NthNeighbors implements Iterator<Set<Shrine>>
    {
        Set<Shrine> toVisitNext, visited;

        /**
         *
         * @param visited_ The acumulation of all shrines visited is put hear, inorder to be checked after
         */
        public NthNeighbors(Set<Shrine> visited_)
        {
            toVisitNext = new TreeSet<>();
            toVisitNext.add(Shrine.this);
            visited = visited_;
            visited.add(Shrine.this);
        }

        public Set<Shrine> next() {
            Set<Shrine> new_ = new HashSet<>();
            for (Shrine connection : toVisitNext) {
                new_.addAll(connection.getNewConnections(visited));
            }
            toVisitNext = new_;
            return toVisitNext;
        }

        public boolean hasNext() {
            return !toVisitNext.isEmpty();
        }

    }

    /**
     * filters out the shrines we have already visted from the connections list.
     * @param visited (Shrines we have already seen, and so do _not_ want to see now.
     * @return The shrines that are one step from this shrine, and not in "visited".
     */
    private Set<Shrine> getNewConnections(Set<Shrine> visited) {
        Set<Shrine> toVisitNext = new android.support.v4.util.ArraySet<>();
        for (Shrine connection : getConnections()) {
            if (visited.add(connection)) {
                // Set::add only returns true if the add changes the set:
                //  i.e. only if the item was not already in visited.
                toVisitNext.add(connection);
            }
        }
        return toVisitNext;
    }

    public Tree<Shrine> getPathTo(Set<Shrine> knownShrines, Shrine destination) {
        Tree<Shrine> tree = makePathTo(knownShrines, destination);
        pruneTree(tree, destination);
        return tree;
    }

    private Tree<Shrine> makePathTo(Set<Shrine> knownShrines,  Shrine destination) {
        Collection<Tree<Shrine>> thisRow = new HashSet<>();
        Collection<Tree<Shrine>> nextRow = new HashSet<>();
        Tree<Shrine> root = new Tree<>(this);
        nextRow.add(root);
        // Another breadth first search, but this one builds a tree and so needs information getNthNeighbors doesn't give
        boolean done;
        do {

            thisRow.clear();
            thisRow.addAll(nextRow);
            nextRow.clear();

            done = false;
            Map<Tree<Shrine>, Set<Shrine>> map = new HashMap<>();

            // Get the next row per each tree in this row without adding them yet (otherwise we won't get all the leaves in a diamond)
            for (Tree<Shrine> tree : thisRow) {
                TreeSet<Shrine> leaves = new TreeSet<>();
                for (Shrine shrine : tree.here.getConnections()) {
                    if (knownShrines.contains(shrine) && !root.contains(shrine)) {
                        leaves.add(shrine);
                    }
                }
                map.put(tree, leaves);
            }

            // Now that we've found all the leaves for all the trees in this row, we can add them to our main tree
            for (Tree<Shrine> tree : thisRow) {
                Set<Shrine> leaves = map.get(tree);
                tree.addAll(leaves);
                nextRow.addAll(tree.children);
                // We're done iff a leaf in the next row is our destination
                done |= leaves.contains(destination);
            }
        } while (!done);

        return root;
    }

    private boolean pruneTree(Tree<Shrine> tree, Shrine destination) {
        if (tree.here == destination) {
            return true;
        }

        if (tree.children.isEmpty()) {
            return false;
        }

        //noinspection unchecked
        List<Tree<Shrine>> remove = new ArrayList<>();
        //noinspection unchecked
        for (Tree<Shrine> t : tree.children) {
            if (!pruneTree(t, destination)) {
                remove.add(t);
            }
        }

        tree.children.removeAll(remove);
        return !tree.children.isEmpty();
    }

    public String getName() {
        return name;
    }

    public List<Shrine> getConnections() {
        return connections;
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

    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        boolean ret = false;
        if (other instanceof Shrine) {
            Shrine otherShrine = (Shrine) other;
            ret = Util.equals(name, otherShrine.getName());
        }
        return ret;
    }
}
