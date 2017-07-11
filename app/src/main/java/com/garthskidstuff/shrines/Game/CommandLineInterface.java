package com.garthskidstuff.shrines.Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by garthupshaw1 on 7/3/17.
 * 
 */

class CommandLineInterface {
    private Game game;
    
    private CommandLineInterface(Game game) {
        this.game = game;
    }
    
    private static List<String> readFile(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        //noinspection TryFinallyCanBeTryWithResources
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
            if (Utils.equals("*", words[2])) {
                param = (int) (Shrine.PARTS_MULTIPLIER * Double.parseDouble(words[3]));
            } else if (Utils.equals("/", words[2])) {
                param = (int) (Shrine.PARTS_MULTIPLIER / Double.parseDouble(words[3]));
            }
        }
        return param;
    }

    private static Game makeGameFromConfig(String pathToConfig) throws IOException {
        Game.Constants constants = new Game.Constants(-1);
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

        return Game.mkTestGame(constants);
    }

    private enum Order {
        HELP("?", "help ex: ?"),
        SHOW_SHRINE("show", "shows a shrine. Default is home shrine.  ex: 'show <shrine_name>'"),
        BUILD_FIGHTER("buildfighter", "builds fighter(s) with a number of workers at a shrine (default is last shown shrine).  ex: 'buildFighter <num_workers> <shrine_name>'"),
        BUILD_ALTAR("buildAltar", "builds altar(s) with a number of workers at a shrine (default is last shown shrine).  ex: 'buildAltar <num_workers> <shrine_name>'"),
        MOVE("move", "moves items to a destination (must be connected). ex: 'move <num> <type> <destination>' Where type is MovableType"),
        NEXT("next", "ends the current player's turn"),
        QUIT("quit", "quits the game");

        String order;
        String helpText;

        Order(String order, String helpText) {
            this.order = order;
            this.helpText = helpText;
        }
    }

    private static Integer lastShownShrineId = null;

    private Integer runOrder(Order order, Integer currentPlayerIndex, String[] words) throws Exception {
        int currentPlayerId = game.homeIds.get(currentPlayerIndex);
        if (null == lastShownShrineId) {
            lastShownShrineId = currentPlayerId;
        }
        switch (order) {
            case HELP: {
                for (Order o : Order.values()) {
                    System.out.println(o.order + ":  " + o.helpText);
                }
                break;
            }
            case SHOW_SHRINE: {
                if ((1 < words.length) && Utils.equals("all", words[1])) {
                    for (Shrine shrine : game.board.getShrines()) {
                        if (shrine.getOwnerId() == currentPlayerId) {
                            showShrine(shrine);
                        }
                    }
                    System.out.println(">>>>");
                } else {
                    Shrine shrine = game.board.getShrine((1 < words.length) ? Integer.parseInt(words[1]) : lastShownShrineId);
                    if (null != shrine) {
                        lastShownShrineId = shrine.getId();
                    } else {
                        System.out.println(words[1] + " not found");
                    }
                }
                break;
            }
            case BUILD_FIGHTER: {
                if (1 < words.length) {
                    Shrine shrine = game.board.getShrine((2 < words.length) ? Integer.parseInt(words[2]) : lastShownShrineId);
                    int num = Integer.parseInt(words[1]);
                    if (!shrine.doOrder(Shrine.Order.BUILD_FIGHTER, num)) {
                        System.out.println("Error building!");
                    }
                }
                break;
            }
            case BUILD_ALTAR: {
                if (1 < words.length) {
                    Shrine shrine = game.board.getShrine((2 < words.length) ? Integer.parseInt(words[2]) : lastShownShrineId);
                    int num = Integer.parseInt(words[1]);
                    if (!shrine.doOrder(Shrine.Order.BUILD_ALTAR, num)) {
                        System.out.println("Error building!");
                    }
                }
                break;
            }
            case MOVE: {
                if (4 == words.length) {
                    int num = Integer.parseInt(words[1]);
                    Shrine.MovableType type = Shrine.MovableType.valueOf(words[2].toUpperCase());
                    int id = Integer.parseInt(words[3]);
                    Integer destinationId = game.board.getConnections(lastShownShrineId).contains(id) ? id : null;
                    Shrine shrine = game.board.getShrine(lastShownShrineId);
                    if ((null == destinationId) || !shrine.doMoveOrder(destinationId, type, num)) {
                        System.out.println("Error moving!");
                    }
                }
                break;
            }
            case NEXT: {
                currentPlayerIndex++;
                if(currentPlayerIndex == game.homeIds.size()) {
                    currentPlayerIndex = 0;
                    game.board.endTurn();
                }
                break;
            }
            case QUIT: {
                return null;
            }
        }
        showShrine(game.board.getShrine(lastShownShrineId));
        return currentPlayerIndex;
    }

    private void showShrine(Shrine shrine) {
        System.out.println(shrine.toCLIString());
        System.out.println("    " + game.board.getConnections(shrine.getId()).toString());
    }

    private Integer getAndRunOrder(Integer currentPlayerIndex) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter an order, " + game.homeIds.get(currentPlayerIndex));

        String line = reader.readLine();
        line = line.toLowerCase();
        System.out.println("line: " + line);
        String[] words = line.split(" ");
        if (0 < words.length) {
            for (Order order : Order.values()) {
                if (Utils.equals(order.order, words[0])) {
                    currentPlayerIndex = runOrder(order, currentPlayerIndex, words);
                    break;
                }
            }
        }

        return currentPlayerIndex;
    }

    /////////////////////////////
    public static void main(String[] args) {
        try {
            if (1 != args.length) {
                System.out.println("Enter a configuration file!");
            } else {
                Game game = makeGameFromConfig(args[0]);
                CommandLineInterface cli = new CommandLineInterface(game);

                Integer currentPlayerIndex = 0;
                do {
                    currentPlayerIndex = cli.getAndRunOrder(currentPlayerIndex);
                } while (null != currentPlayerIndex);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
