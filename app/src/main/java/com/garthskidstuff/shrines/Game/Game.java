package com.garthskidstuff.shrines.Game;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The God object for the "business logic"/Model. I.e. everything that isn't IO goes _here_.
 * Created by garthupshaw1 on 5/10/17.
 */

@SuppressWarnings("WeakerAccess")
public class Game {
    private static final int minShrines = 40;
    private static final int maxShrines = 100;
    private static final int minConnections = 3;
    private static final int maxConnections = 5;
    private static final int minMaxPopulation = 5; // The max size pop can grow is between min/max
    private static final int maxMaxPopulation = 40;

    final Shrine[] homes = {null, null};
    @SuppressWarnings("FieldCanBeLocal")
    private final List<Shrine> shrines = new ArrayList<>();
    private final Random random;
    @SuppressWarnings("FieldCanBeLocal")
    private final int minHomeDistance = 5;
    @SuppressWarnings("FieldCanBeLocal")
    private final int maxHomeDistance = 6;

    /**
     * This constructor is the one called by the actual app
     *
     * @param nameList  A list of names for shrines.
     * @param imageList A list of image ids for the shrines.
     */
    public Game(List<String> nameList, List<String> imageList) {
        this(nameList, imageList, new Random());
    }

    /**
     * This constructor is only directly called by test.
     * It lets us record the seed, and reuse it if needed.
     * <p>
     * This also where almost all the logic for initial set of a game is.
     * (The exception are some important parts are Shrine methods)
     *
     * @param nameList  A list of names for shrines.
     * @param imageList A list of image ids for the shrines.
     * @param random_   The RNG used by the whole game, it's a parameter so that a test harness can save/print/reuse the seed of the RNG.
     */
    public Game(List<String> nameList, List<String> imageList, Random random_) {
        random = random_;
        int numShrines = roll(minShrines, maxShrines);
        Shuffled<String> namesShuffled = new Shuffled<>(nameList);
        Shuffled<String> imagesShuffled = new Shuffled<>(imageList);

        for (int i = 0; i < numShrines; i++) {
            Shrine shrine = new Shrine(namesShuffled.next(), imagesShuffled.next(), roll(minMaxPopulation, maxMaxPopulation));
            shrines.add(shrine);
        }

        // Create the directed graph of Connections.
        boolean validGraph = false;
        do {

            //generate raw web
            for (Shrine shrine : shrines) {
                int numConnections = roll(minConnections, maxConnections);
                do {
                    Shrine newConnection = shrines.get(roll(0, numShrines - 1));
                    //  our graph is a simply connected graph. So at most one edge A to B, and no
                    //      edges A to A
                    //  I.E. all connections from the same shrine
                    //      must go to distinct shrines that aren't the origin shrine.
                    if (!newConnection != shrine)) {
                        connections.add(newConnection);
                    }
                } while (shrine.getConnectionsReadOnly(Shrine.Direction.FORWARD).size() < numConnections);
            }

            // Validate the web just created above.
            final int maxDistance = maxHomeDistance;
            final int minDistance = minHomeDistance;
            Set<Shrine> connected = shrines.get(0).getConnectedComponent();
            if (connected.size() >= minShrines) {
                //This will usually only do one iteration
                // But there might be no valid candidates.
                for (Shrine playerHome : connected) {
                    homes[0] = playerHome;
                    Set<Shrine> candidates = playerHome.getNtoMthNeighbors(minDistance, maxDistance);
                    for (Shrine candidate : candidates) {
                        boolean goodCandidate = (candidate.getConnections().size() == playerHome.getConnections().size()) &&
                                (candidate.getNtoMthNeighbors(minDistance, maxDistance).contains(playerHome));
                        if (!goodCandidate) {
                            candidates.remove(candidate);
                        }
                    }
                    int size = candidates.size();
                    if (size != 0) {
                        List<Shrine> candidateList = new ArrayList<>();
                        candidateList.addAll(candidates);
                        homes[1] = candidateList.get(roll(0, size - 1));
                        //We have gotten through all the checks and the web has what we need.
                        validGraph = true;
                    }
                }
            }
        } while (!validGraph);
    }

    public static Game mkTestGame() {
        List<String> names = new ArrayList<>();
        List<String> images = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            String number = Integer.toString(i);
            String name = "Name_" + number;
            String imageID = "Image_" + number;
            names.add(name);
            images.add(imageID);
        }
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        Log.d("MainActivity", "random seed = " + seed);
        return new Game(names, images, random);
    }

    public int roll(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    /**
     * Shuffled is an iterator (with remove) that accesses the elements of the passed in list in a
     * random order.
     * "Shuffled" is a bit of misnomer because it only actually "shuffles" on demand.
     * (By choosing a random remaining element.)
     *
     * @param <T> The type of the item returned by next/contained in oldList.
     */
    class Shuffled<T> implements Iterator {
        final List<T> oldList;
        final List<Integer> innerList = new ArrayList<>();
        private int now;

        /**
         * This just prepares a list of indexes, and keeps an reference to the passed in list.
         *
         * @param list the list we want to get random elements out of.
         */
        public Shuffled(List<T> list) {
            oldList = list;
            for (int ix = 0; ix < list.size(); ix++) {
                innerList.add(ix);
            }

        }

        /**
         * Do we have any indexes we haven't already used in next()?
         *
         * @return can we run next safely.
         */
        @Override
        public boolean hasNext() {
            return !innerList.isEmpty();
        }

        /**
         * Fetches a remaining item randomly.
         *
         * @return the random element from oldList.
         */
        @Override
        public T next() {
            now = roll(0, innerList.size());
            innerList.remove(now);
            return oldList.get(now);
        }

        /**
         * Not used yet. This removes the item at now (i.e the item returned by the most recent next)
         * from the original list.
         */
        @Override
        public void remove() {
            for (int ix = 0; ix < innerList.size(); ix++) {
                int item = innerList.get(ix);
                if (item > now) {
                    innerList.set(ix, 1 + item);
                }
            }
            oldList.remove(now);
        }
    }
}
