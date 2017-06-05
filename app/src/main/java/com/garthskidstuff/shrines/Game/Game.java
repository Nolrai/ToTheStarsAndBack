package com.garthskidstuff.shrines.Game;

import android.util.Log;

import java.util.ArrayList;
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

    public static class Constants {
        public int minShrines = 40;
        public int maxShrines = 100;
        public int minConnections = 3;
        public int maxConnections = 5;
        public int minMaxPopulation = 5; // The max size pop can grow is between min/max
        public int maxMaxPopulation = 40;
        @SuppressWarnings("FieldCanBeLocal")
        public int minHomeDistance = 3;
        @SuppressWarnings("FieldCanBeLocal")
        public int maxHomeDistance = 6;
        public long seed = -1;

        public Constants(long seed) {
            this.seed = seed;
        }
    }
    
    final Constants constants;
    final List<Shrine> homes;
    World world;

    private Random random;

    /**
     * This constructor is the one called by the actual app
     *
     * @param nameList  A list of names for shrines.
     * @param imageList A list of image ids for the shrines.
     */
    public Game(List<String> nameList, List<String> imageList) {
        this(nameList, imageList, new Constants(-1));
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
     */
    public Game(List<String> nameList, List<String> imageList, Constants constants) {
        this.constants = constants;
        random = (-1 == constants.seed) ? new Random() : new Random(constants.seed);
        int numShrines = roll(constants.minShrines, constants.maxShrines);
        Shuffled<String> namesShuffled = new Shuffled<>(nameList);
        Shuffled<String> imagesShuffled = new Shuffled<>(imageList);

        List<Shrine> shrines = new ArrayList<>();
        for (int i = 0; i < numShrines; i++) {
            Shrine shrine = new Shrine(namesShuffled.next(), imagesShuffled.next(), roll(constants.minMaxPopulation, constants.maxMaxPopulation));
            shrines.add(shrine);
        }

        world = new World();

        // Create the directed graph of Connections.
        boolean validGraph; // = false by default
        List<Shrine> candidates = null;
        do {
            world.clear();

            //generate raw web
            for (Shrine shrine : shrines) {
                int numConnections = roll(constants.minConnections, constants.maxConnections);
                List<Shrine> connections = new ArrayList<>();
                do {
                    Shrine newConnection = shrines.get(roll(0, numShrines - 1));
                    //  our graph is a simply connected graph. So at most one edge A to B, and no
                    //      edges A to A
                    //  I.E. all connections from the same shrine
                    //      must go to distinct shrines that aren't the origin shrine.
                    if (!connections.contains(newConnection) && (newConnection != shrine)) {
                        connections.add(newConnection);
                    }
                } while (connections.size() < numConnections);

                // Add connections to World
                world.addShrine(shrine, connections);
            }

            // Validate the web just created above.
            validGraph = world.isCompletelyConnected();
            if (validGraph) {
                candidates = findHomeWorlds(world);
                validGraph = null != candidates;
            }
        } while (!validGraph);
        homes = candidates;
    }

    private List<Shrine> findHomeWorlds(World world) {
        for (Shrine home0 : world.getShrines()) {
            for (Shrine home1 : world.getShrines()) {
                if(home0 != home1) {
                    Set<List<Shrine>> from0to1 = world.getPaths(world.getShrines(), home0, home1, World.FindPathSettings.useAllShortest());
                    Set<List<Shrine>> from1to0 = world.getPaths(world.getShrines(), home1, home0, World.FindPathSettings.useAllShortest());
                    List<List<Shrine>> sorted0to1 = World.sortPaths(from0to1);
                    List<List<Shrine>> sorted1to0 = World.sortPaths(from1to0);
                    int length0to1 = sorted0to1.get(0).size();
                    int length1to0 = sorted1to0.get(0).size();
                    if ((length0to1 == length1to0) && (length0to1 >= constants.minHomeDistance) && (length0to1 <= constants.maxHomeDistance)) {
                        List<Shrine> homes = new ArrayList<>();
                        homes.add(home0);
                        homes.add(home1);
                        return homes;
                    }
                }
            }
        }
        return null;
    }

    public static Game mkTestGame(Constants constants) {
        List<String> names = new ArrayList<>();
        List<String> images = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            String number = Integer.toString(i);
            String name = "Name_" + number;
            String imageID = "Image_" + number;
            names.add(name);
            images.add(imageID);
        }
//        Utils.Logd("MainActivity", "random seed = " + seed);
        return new Game(names, images, constants);
    }

    public int roll(int min, int max) {
        return random.nextInt(max - min + 1) + min;
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
            now = roll(0, innerList.size() - 1);
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
