package com.garthskidstuff.shrines.Game;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

    enum Direction
    {
        FORWARD, BACKWARD;

        Direction toggle ()
        {
            Direction ret;
            switch (this) {
                case FORWARD:
                    ret = BACKWARD;
                    break;
                case BACKWARD:
                    ret = FORWARD;
                    break;
                default:
                    ret = null;
            }
            return ret;
        }
    }

    //It's Game's responsibility to make sure that all shrines in reverseConnections have this shrine in their connections list.
    private final Set<Shrine> connections = new TreeSet<>();
    private final Set<Shrine> backConnections = new TreeSet<>();
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
    Set<Shrine> getConnectedComponent(Direction d) {
        Set<Shrine> visited = new TreeSet<>();
        //noinspection StatementWithEmptyBody
        for (Iterator<Set<Shrine>> iter = new NthNeighbors(d, visited); iter.hasNext(); iter.next()) {
            //do nothing
        }
        return visited;
    }

    /**
     * This returns all the minimal paths (using only the shrines in subGraph),
     * from a to b.
     * @param subGraph The shrines we are are allowed to use. (If this does not contain both this shrine and source, then no path will be found.)
     * @param source The source shrine
     * @return An empty List if no path was found, a list of posibilities for each step of the path
     */
     Set<List<Shrine>> findPathsFrom (Shrine source, Set<Shrine> subGraph)
    {
        Deque<Set<Shrine>> backAcumulator = new ArrayDeque<>();
        //Build a reverse spanning "tree" (really a queue of sets)
        Iterator<Set<Shrine>> breadthFirst = new NthNeighbors(Direction.BACKWARD, new TreeSet<Shrine>());
        for (Set<Shrine> stage = breadthFirst.next();
             breadthFirst.hasNext();
             stage = breadthFirst.next()) {
            stage.retainAll(subGraph); //Remove everything not in the subGraph
            backAcumulator.add(stage);
            if (stage.contains(this)) {
                break;
            }
        }
        if (backAcumulator.peek().isEmpty()){
            //No path was found.
            return new TreeSet<>();
        }
        else
        {
            Set<List<Shrine>> ret = new HashSet<>();
            
            return null;
        }
    }

    /**
     * Finds All shrines with shortest path from this shrine is between minDistance and maxDistance (inclusive).
     *
     * @param minDistance Shrines closer then this are ignored.
     * @param maxDistance We stop before looking at shrines farther then this.
     * @return All shrines with shortest path from this is between minDistance and maxDistance (inclusive).
     */
    Set<Shrine> getNtoMthNeighbors(Direction d, int minDistance, int maxDistance) {
        Set<Shrine> visited = new TreeSet<>();
        Set<Shrine> ret = new TreeSet<>();
        Iterator<Set<Shrine>> iter = new NthNeighbors(d, visited);
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
    class NthNeighbors implements Iterator<Set<Shrine>>
    {
        Set<Shrine> toVisitNext, visited;
        Direction d;

        /**
         *
         * @param visited_ The acumulation of all shrines visited is put hear, inorder to be checked after
         */
        public NthNeighbors(Direction d_, Set<Shrine> visited_)
        {
            toVisitNext = new TreeSet<>();
            toVisitNext.add(Shrine.this);
            visited = visited_;
            visited.add(Shrine.this);
            d = d_;
        }

        public Set<Shrine> next() {
            Set<Shrine> new_ = new HashSet<>();
            for (Shrine connection : toVisitNext) {
                new_.addAll(connection.getNewConnections(d, visited));
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
    private Set<Shrine> getNewConnections(Direction d, Set<Shrine> visited) {
        Set<Shrine> toVisitNext = new android.support.v4.util.ArraySet<>();
        for (Shrine connection : getConnectionsReadOnly(d)) {
            if (visited.add(connection)) {
                /*
                Set::add only returns true iff the add changes the set:
                i.e. only if the item was not already in visited.
                */
                toVisitNext.add(connection);
            }
        }
        return toVisitNext;
    }

    public String getName() {
        return name;
    }

    public List<Shrine> getForwardConnectionsReadOnly() {
        return new ArrayList<>(connections);
    }

    Set<Shrine> getConnectionsReadOnly (Direction d){
        switch (d)
        {
            case FORWARD:
                return new TreeSet<> (connections);
            break;
            case BACKWARD:
                return new TreeSet<> (backConnections);
            break;
        }
    }

    public void addConnection (Shrine target) {
        getConnectionsWriteable(Direction.FORWARD).add(target);
        target.getConnectionsWriteable(Direction.BACKWARD).add(this);
    }

    private Set<Shrine> getConnectionsWriteable (Direction d){
        switch (d)
        {
            case FORWARD:
                return connections;
            break;
            case BACKWARD:
                return backConnections;
            break;
        }
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

    /*
     * For test:
     */

    /**
     * Comfirms that each (-+back)connection (of this shrine)
     * is mirrored by a (+-back)connection at its destination.
     * @param d (are we testing forward here against back there, or back here against forward there)
     * @return
     */
    boolean validateConnections(Direction d)
    {
        for (Shrine neighbor : this.getConnectionsReadOnly(d)) {
            if(!neighbor.getConnectionsReadOnly(d.toggle()).contains(this)) {
                return false;
            }
        }
        return true;
    }

    boolean validateConnections()
    {
        return validateConnections(Direction.FORWARD) && validateConnections(Direction.BACKWARD);
    }
}
