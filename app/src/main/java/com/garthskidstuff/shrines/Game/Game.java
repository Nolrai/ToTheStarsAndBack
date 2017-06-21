package com.garthskidstuff.shrines.Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        public int minMiningRateParts = (int) (Shrine.PARTS_MULTIPLIER * 1.5);
        public int maxMiningRateParts = (int) (Shrine.PARTS_MULTIPLIER * 5);
        public int miningDegradationRateParts = Shrine.PARTS_MULTIPLIER / 1000; // amount mining rate goes down each time a worker mines
        public int minWorkerRateParts = Shrine.PARTS_MULTIPLIER / 100;
        public int maxWorkerRateParts = Shrine.PARTS_MULTIPLIER / 5;
        public int homeMaxPopulation = 100;
        public int homeMiningRateParts = (int) (Shrine.PARTS_MULTIPLIER * 2);
        public int homeWorkerRateParts = Shrine.PARTS_MULTIPLIER / 10;
        public int homeNumAlters = 10;
        public int homeNumGold = 30;
        public int homeNumWorkers = 50;

        @SuppressWarnings("FieldCanBeLocal")
        public int minHomeDistance = 3;
        @SuppressWarnings("FieldCanBeLocal")
        public int maxHomeDistance = 6;
        public long seed = -1;

        public Constants(long seed) {
            this.seed = seed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Constants constants = (Constants) o;

            if (minShrines != constants.minShrines) return false;
            if (maxShrines != constants.maxShrines) return false;
            if (minConnections != constants.minConnections) return false;
            if (maxConnections != constants.maxConnections) return false;
            if (minMaxPopulation != constants.minMaxPopulation) return false;
            if (maxMaxPopulation != constants.maxMaxPopulation) return false;
            if (minMiningRateParts != constants.minMiningRateParts) return false;
            if (maxMiningRateParts != constants.maxMiningRateParts) return false;
            if (miningDegradationRateParts != constants.miningDegradationRateParts) return false;
            if (minWorkerRateParts != constants.minWorkerRateParts) return false;
            if (maxWorkerRateParts != constants.maxWorkerRateParts) return false;
            if (homeMaxPopulation != constants.homeMaxPopulation) return false;
            if (homeMiningRateParts != constants.homeMiningRateParts) return false;
            if (homeNumAlters != constants.homeNumAlters) return false;
            if (homeNumGold != constants.homeNumGold) return false;
            if (homeNumWorkers != constants.homeNumWorkers) return false;
            if (minHomeDistance != constants.minHomeDistance) return false;
            if (maxHomeDistance != constants.maxHomeDistance) return false;
            return seed == constants.seed;

        }

        @Override
        public int hashCode() {
            int result = minShrines;
            result = 31 * result + maxShrines;
            result = 31 * result + minConnections;
            result = 31 * result + maxConnections;
            result = 31 * result + minMaxPopulation;
            result = 31 * result + maxMaxPopulation;
            result = 31 * result + minMiningRateParts;
            result = 31 * result + maxMiningRateParts;
            result = 31 * result + miningDegradationRateParts;
            result = 31 * result + minWorkerRateParts;
            result = 31 * result + maxWorkerRateParts;
            result = 31 * result + homeMaxPopulation;
            result = 31 * result + homeMiningRateParts;
            result = 31 * result + homeNumAlters;
            result = 31 * result + homeNumGold;
            result = 31 * result + homeNumWorkers;
            result = 31 * result + minHomeDistance;
            result = 31 * result + maxHomeDistance;
            result = 31 * result + (int) (seed ^ (seed >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "Constants{" +
                    "minShrines=" + minShrines +
                    ", maxShrines=" + maxShrines +
                    ", minConnections=" + minConnections +
                    ", maxConnections=" + maxConnections +
                    ", minMaxPopulation=" + minMaxPopulation +
                    ", maxMaxPopulation=" + maxMaxPopulation +
                    ", minMiningRateParts=" + minMiningRateParts +
                    ", maxMiningRateParts=" + maxMiningRateParts +
                    ", miningDegradationRateParts=" + miningDegradationRateParts +
                    ", minWorkerRateParts=" + minWorkerRateParts +
                    ", maxWorkerRateParts=" + maxWorkerRateParts +
                    ", homeMaxPopulation=" + homeMaxPopulation +
                    ", homeMiningRateParts=" + homeMiningRateParts +
                    ", homeWorkerRateParts=" + homeWorkerRateParts +
                    ", homeNumAlters=" + homeNumAlters +
                    ", homeNumGold=" + homeNumGold +
                    ", homeNumWorkers=" + homeNumWorkers +
                    ", minHomeDistance=" + minHomeDistance +
                    ", maxHomeDistance=" + maxHomeDistance +
                    ", seed=" + seed +
                    '}';
        }
    }

    final Constants constants;
    final List<String> homeNames;
    World world;

    private volatile Roller roller;

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
        roller = new Roller(constants.seed);
        int numShrines = roller.roll(constants.minShrines, constants.maxShrines);
        Shuffled<String> namesShuffled = new Shuffled<>(nameList);
        Shuffled<String> imagesShuffled = new Shuffled<>(imageList);

        List<Shrine> shrines = new ArrayList<>();
        for (int i = 0; i < numShrines; i++) {
            Shrine shrine = new Shrine(namesShuffled.next(), imagesShuffled.next());
            shrines.add(shrine);
        }

        world = new World(roller);

        // Create the directed graph of Connections.
        boolean validGraph; // = false by default
        List<String> candidates = null;
        do {
            world.clear();

            //generate raw web
            for (Shrine shrine : shrines) {
                int numConnections = roller.roll(constants.minConnections, constants.maxConnections);
                List<String> connections = new ArrayList<>();
                do {
                    String newConnection = shrines.get(roller.roll(0, numShrines - 1)).getName();
                    //  our graph is a simply connected graph. So at most one edge A to B, and no
                    //      edges A to A
                    //  I.E. all connections from the same shrine
                    //      must go to distinct shrines that aren't the origin shrine.
                    if (!connections.contains(newConnection) && !Utils.equals(newConnection, shrine.getName())) {
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
        homeNames = candidates;

        // Init all the default shrine values
        for (Shrine shrine : world.getShrines()) {
            int maxPopulation = roller.roll(constants.minMaxPopulation, constants.maxMaxPopulation);
            int miningRateParts = roller.roll(constants.minMiningRateParts, constants.maxMiningRateParts);
            int miningDegradationRateParts = constants.miningDegradationRateParts;
            int workerRateParts = roller.roll(constants.minWorkerRateParts, constants.maxWorkerRateParts);
            shrine.initBasic(maxPopulation, miningRateParts, miningDegradationRateParts, workerRateParts);
        }

        // Init the home worlds
        for (String name : homeNames) {
            Shrine shrine = world.getShrine(name);
            shrine.initHome(constants.homeMaxPopulation, constants.homeMiningRateParts,
                    constants.miningDegradationRateParts, constants.homeWorkerRateParts,
                    constants.homeNumWorkers, constants.homeNumAlters, constants.homeNumGold);
        }

    }

    private List<String> findHomeWorlds(World world) {
        for (String home0 : world.getShrineNames()) {
            for (String home1 : world.getShrineNames()) {
                if (!Utils.equals(home0, home1) && (world.getConnections(home0).size() == world.getConnections(home1).size())) {
                    Set<List<String>> from0to1 = world.getPaths(world.getShrineNames(), home0, home1, World.FindPathSettings.useAllShortest());
                    Set<List<String>> from1to0 = world.getPaths(world.getShrineNames(), home1, home0, World.FindPathSettings.useAllShortest());
                    List<List<String>> sorted0to1 = world.sortPaths(from0to1);
                    List<List<String>> sorted1to0 = world.sortPaths(from1to0);
                    int length0to1 = sorted0to1.get(0).size();
                    int length1to0 = sorted1to0.get(0).size();
                    if ((length0to1 == length1to0) && (length0to1 >= constants.minHomeDistance) && (length0to1 <= constants.maxHomeDistance)) {
                        List<String> homes = new ArrayList<>();
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
         * As long as the innerList isn't empty then we can run Next.
         * (_Which_ item is next is decided in next().)
         *
         * @return can we run next safely.
         */
        @Override
        public boolean hasNext() {
            return !innerList.isEmpty();
        }

        /**
         * Randomly fetches a remaining item.
         *
         * @return the randomly selected element.
         */
        @Override
        public T next() {
            now = roller.roll(0, innerList.size() - 1);
            innerList.remove(now);
            return oldList.get(now);
        }

        /**
         * Not yet used. This removes the item at now (i.e the item returned by the most recent next)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (!constants.equals(game.constants)) return false;
        //noinspection SimplifiableIfStatement
        if (!homeNames.equals(game.homeNames)) return false;
        return world.equals(game.world);

    }

    @Override
    public int hashCode() {
        int result = constants.hashCode();
        result = 31 * result + homeNames.hashCode();
        result = 31 * result + world.hashCode();
        return result;
    }

    static List<String> readFile(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                lines.add(line);
            }
        } finally {
            br.close();
        }
        return lines;
    }

    private static Integer parseForParameter(String[] words) {
        Integer param = null;
        if (2 == words.length) {
            param = Integer.parseInt(words[1]);
        } else if (4 == words.length) {
            if ("*" == words[2]) {
                param = (int) (Shrine.PARTS_MULTIPLIER * Double.parseDouble(words[3]));
            } else if ("/" == words[2]) {
                param = (int) (Shrine.PARTS_MULTIPLIER / Double.parseDouble(words[3]));
            }
        }
        return param;
    }

    private static Game makeGameFromConfig(String pathToConfig) throws IOException {
        Constants constants = new Constants(-1);
        List<String> lines = readFile(pathToConfig);
        for(String line: lines) {
            String[] words = line.split(": ");
            if ((2 == words.length) || (4 == words.length)) {
                switch (words[0]) {
                    case "minShrines":
                        constants.minShrines = parseForParameter(words);
                        break;
                    case "maxShrines":
                        constants.maxShrines = parseForParameter(words);
                        break;
                    case "minConnections":
                        constants.minConnections = parseForParameter(words);
                        break;
                    case "maxConnections":
                        constants.maxConnections = parseForParameter(words);
                        break;
                    case "minMaxPopulation":
                        constants.minMaxPopulation = parseForParameter(words);
                        break;
                    case "maxMaxPopulation":
                        constants.maxMaxPopulation = parseForParameter(words);
                        break;
                    case "minMiningRateParts":
                        constants.minMiningRateParts = parseForParameter(words);
                        break;
                    case "maxMiningRateParts":
                        constants.maxMiningRateParts = parseForParameter(words);
                        break;
                    case "miningDegradationRateParts":
                        constants.miningDegradationRateParts = parseForParameter(words);
                        break;
                    case "minWorkerRateParts":
                        constants.minWorkerRateParts = parseForParameter(words);
                        break;
                    case "maxWorkerRateParts":
                        constants.maxWorkerRateParts = parseForParameter(words);
                        break;
                    case "homeMaxPopulation":
                        constants.homeMaxPopulation = parseForParameter(words);
                        break;
                    case "homeMiningRateParts":
                        constants.homeMiningRateParts = parseForParameter(words);
                        break;
                    case "homeWorkerRateParts":
                        constants.homeWorkerRateParts = parseForParameter(words);
                        break;
                    case "homeNumAlters":
                        constants.homeNumAlters = parseForParameter(words);
                        break;
                    case "homeNumGold":
                        constants.homeNumGold = parseForParameter(words);
                        break;
                    case "homeNumWorkers":
                        constants.homeNumWorkers = parseForParameter(words);
                        break;
                    case "minHomeDistance":
                        constants.minHomeDistance = parseForParameter(words);
                        break;
                    case "maxHomeDistance":
                        constants.maxHomeDistance = parseForParameter(words);
                        break;
                    case "seed":
                        constants.seed = parseForParameter(words);
                        break;
                }
            }
        }
        System.out.println(constants.toString());

        return mkTestGame(constants);
    }

    enum Order {
        HELP("?", "help ex: ?"),
        SHOW_SHRINE("show", "shows a shrine. Default is home world.  ex: 'show <shrine_name>'"),
        BUILD_FIGHTER("buildFighter", "builds fighter(s) with a number of workers at a shrine (default is last shown shrine).  ex: 'buildFighter <num_workers> <shrine_name>'"),
        BUILD_ALTAR("buildAltar", "builds altar(s) with a number of workers at a shrine (default is last shown shrine).  ex: 'buildAltar <num_workers> <shrine_name>'"),
        NEXT("next", "ends the current player's turn"),
        QUIT("quit", "quits the game");

        String order;
        String helpText;

        Order(String order, String helpText) {
            this.order = order;
            this.helpText = helpText;
        }
    }

    static String lastShownShrine = null;

    String runOrder(Order order, String currentPlayer, String[] words) {
        if (null == lastShownShrine) {
            lastShownShrine = currentPlayer;
        }
        switch (order) {
            case HELP: {
                for (Order o : Order.values()) {
                    System.out.println(o.order + ":  " + o.helpText);
                }
                break;
            }
            case SHOW_SHRINE: {
                Shrine shrine = world.getShrine((1 < words.length) ? words[1] : currentPlayer);
                if (null != shrine) {
                    lastShownShrine = shrine.getName();
                    System.out.println(shrine.toString());
                    System.out.println(world.getConnections(shrine.getName()).toString());
                } else {
                    System.out.println(words[1] + " not found");
                }
                break;
            }
            case BUILD_FIGHTER: {
                if (1 < words.length) {
                    Shrine shrine = world.getShrine((2 < words.length) ? words[2] : lastShownShrine);
                    int num = Integer.parseInt(words[1]);
                    if (!shrine.doOrder(Shrine.Order.BUILD_FIGHTER, num)) {
                        System.out.println("Error building!");
                    }

                    runOrder(Order.SHOW_SHRINE, currentPlayer, new String[] { Order.SHOW_SHRINE.order });
                }
                break;
            }
            case BUILD_ALTAR: {
                if (1 < words.length) {
                    Shrine shrine = world.getShrine((2 < words.length) ? words[2] : lastShownShrine);
                    int num = Integer.parseInt(words[1]);
                    if (!shrine.doOrder(Shrine.Order.BUILD_ALTAR, num)) {
                        System.out.println("Error building!");
                    }
                    runOrder(Order.SHOW_SHRINE, currentPlayer, new String[] { Order.SHOW_SHRINE.order });
                }
                break;
            }
            case NEXT: { //TODO Will fail if > 2 players
                for (String player : homeNames) {
                    if (!Utils.equals(player, currentPlayer)) {
                        currentPlayer = player;
                        break;
                    }
                }
                break;
            }
            case QUIT: {
                currentPlayer = null;
                break;
            }
        }

        return currentPlayer;
    }

    String getAndRunOrder(String currentPlayer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter an order, " + currentPlayer);

        String line = reader.readLine();
        System.out.println("line: " + line);
        String[] words = line.split(" ");
        if (0 < words.length) {
            for (Order order : Order.values()) {
                if (Utils.equals(order.order, words[0])) {
                    currentPlayer = runOrder(order, currentPlayer, words);
                    break;
                }
            }
        }

        return currentPlayer;
    }

    /////////////////////////////
    public static void main(String[] args) {
        try {
            if (1 != args.length) {
                System.out.println("Enter a configuration file!");
            } else {
                Game game = makeGameFromConfig(args[0]);

                String currentPlayer = game.homeNames.get(0);
                do {
                    currentPlayer = game.getAndRunOrder(currentPlayer);
                } while (null != currentPlayer);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
