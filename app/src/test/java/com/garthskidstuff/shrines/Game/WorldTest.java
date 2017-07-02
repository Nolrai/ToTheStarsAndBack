package com.garthskidstuff.shrines.Game;

import org.junit.Before;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by garthupshaw1 on 6/3/17.
 * Test World logic with paths etc.
 */
public class WorldTest extends BaseTest {
    private World world;

    @Before
    public void setUp() {
        world = new World(new Roller(1));
        List<Integer> homeIds = new ArrayList<>();
        homeIds.add(0);
        homeIds.add(1);
        world.initForHomeIds(homeIds);
    }

    @Test
    public void getPathsTo_trivialPath() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simplePath() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getId()));
        world.addShrine(shrines.get(2));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(2).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_diamond() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        world.addShrine(shrines.get(3));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(3).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));
        testAllPaths.add(makePath(shrines, new int[]{0, 2, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_doubleDiamond() {
        List<Shrine> shrines = Utils.generateShrines(7);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        makeDiamond(world, shrines, 3, 4, 5, 6);
        world.addShrine(shrines.get(6));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(6).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3, 4, 6}));
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3, 5, 6}));
        testAllPaths.add(makePath(shrines, new int[]{0, 2, 3, 4, 6}));
        testAllPaths.add(makePath(shrines, new int[]{0, 2, 3, 5, 6}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialLoop() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId()));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleLoop() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getId()));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(0).getId()));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(2).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialLoopWithDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId(), shrines.get(2).getId()));
        world.addShrine(shrines.get(2));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(2).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialDeadEndLoop() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId(),shrines.get(2).getId()));
        world.addShrine(shrines.get(1));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(0).getId()));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId(), shrines.get(2).getId()));
        world.addShrine(shrines.get(1));
        world.addShrine(shrines.get(2));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(4);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId(), shrines.get(2).getId()));
        world.addShrine(shrines.get(1));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(3).getId()));
        world.addShrine(shrines.get(3));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleDeadEndTwo() {
        List<Shrine> shrines = Utils.generateShrines(4);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId(), shrines.get(2).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(3).getId()));
        world.addShrine(shrines.get(2));
        world.addShrine(shrines.get(3));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(3).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_oneLongOneShort() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId(), shrines.get(2).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getId()));
        world.addShrine(shrines.get(2));

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(2).getId(), World.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 2}));
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_bigNetworkGetShortest() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
        }

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(SIZE - 1).getId());

        Integer length = null;
        for (List<Integer> path : allPaths) {
            if (null == length) {
                length = path.size();
            }
            assertThat(path.size(), is(length));
            assertThat(path.get(path.size() - 1), is(shrines.get(SIZE - 1).getId()));
            for (int i = 0; i < path.size() - 1; i++) {
                List<Integer> connections = world.getConnections(path.get(i));
                assertThat(connections.contains(path.get(i + 1)), is(true));
            }
        }
    }

    @Test
    public void getPathsTo_bigNetworkGetAll() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
        }

        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(SIZE - 1).getId(), World.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        for (List<Integer> path : allPaths) {
            assertThat(path.get(path.size() - 1), is(shrines.get(SIZE - 1).getId()));
            for (int i = 0; i < path.size() - 1; i++) {
                List<Integer> connections = world.getConnections(path.get(i));
                assertThat(connections.contains(path.get(i + 1)), is(true));
            }
        }
    }

    @Test
    public void getPathsTo_findPathWithUnknowns() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        Set<Integer> knownShrines = world.getShrineIds();
        knownShrines.remove(shrines.get(2).getId());

        Set<List<Integer>> allPaths = world.getPaths(knownShrines, shrines.get(0).getId(), shrines.get(3).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_handleUnknownsNoPath() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        Set<Integer> knownShrines = world.getShrineIds();
        knownShrines.remove(shrines.get(1).getId());
        knownShrines.remove(shrines.get(2).getId());

        Set<List<Integer>> allPaths = world.getPaths(knownShrines, shrines.get(0).getId(), shrines.get(3).getId());

        assertThat(allPaths.size(), is(0));
    }

    @Test
    public void sortPaths_defaultToSortShortestFirst() {
        List<Shrine> shrines = Utils.generateShrines(4);
        Set<List<Integer>> allPaths = new HashSet<>();
        List<Integer> path0 = makePath(shrines, new int[] { 0, 1, 2, 3 });
        List<Integer> path1 = makePath(shrines, new int[] { 0, 1 });
        allPaths.add(path0);
        allPaths.add(path1);

        for (Shrine shrine : shrines) {
            world.addShrine(shrine);
        }
        List<List<Integer>> sortedPaths = world.sortPaths(allPaths);

        assertThat(sortedPaths.size(), is(2));
        assertThat(sortedPaths.get(0), is(path1));
        assertThat(sortedPaths.get(1), is(path0));
    }

    @Test
    public void sortPaths_sortShortestFirst() {
        List<Shrine> shrines = Utils.generateShrines(4);
        Set<List<Integer>> allPaths = new HashSet<>();
        List<Integer> path0 = makePath(shrines, new int[] { });
        List<Integer> path1 = makePath(shrines, new int[] { 0 });
        List<Integer> path2 = makePath(shrines, new int[] { 0, 1 });
        List<Integer> path3 = makePath(shrines, new int[] { 0, 1, 2 });
        List<Integer> path4 = makePath(shrines, new int[] { 0, 1, 2, 3 });
        allPaths.add(path4);
        allPaths.add(path1);
        allPaths.add(path0);
        allPaths.add(path2);
        allPaths.add(path3);

        for (Shrine shrine : shrines) {
            world.addShrine(shrine);
        }
        List<List<Integer>> sortedPaths = world.sortPaths(allPaths, World.SORT_SHORTEST_FIRST);

        assertThat(sortedPaths.get(0), is(path0));
        assertThat(sortedPaths.get(1), is(path1));
        assertThat(sortedPaths.get(2), is(path2));
        assertThat(sortedPaths.get(3), is(path3));
        assertThat(sortedPaths.get(4), is(path4));
    }

    @Test
    public void sortPaths_bigNetwork() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
        }
        Set<List<Integer>> allPaths = world.getPaths(shrines.get(0).getId(), shrines.get(SIZE - 1).getId(), World.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        List<List<Integer>> sortedPaths = world.sortPaths(allPaths);

        assertThat(sortedPaths.isEmpty(), is(false));
        for (int i = 0; i < sortedPaths.size() - 1; i++) {
            assertThat(sortedPaths.get(i).size() <= sortedPaths.get(i+1).size(), is(true));
        }
    }

    @Test
    public void isCompletelyConnected_triviallyTrue() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId()));

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(true));
    }

    @Test
    public void isCompletelyConnected_triviallyFalse() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getId()));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(1).getId()));

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void isCompletelyConnected_TwoCompletelyDisconnected() {
        List<Shrine> shrines = Utils.generateShrines(4);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId()));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(3).getId()));
        world.addShrine(shrines.get(3), Utils.makeConnections(shrines.get(2).getId()));

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void isCompletelyConnected_bigConnected() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
        }

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(true));
    }

    @Test
    public void isCompletelyConnected_semiConnected() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            if (0 < i) {
                world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
            } else {
                world.addShrine(shrines.get(i));
            }
        }

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void endTurn_processMovesTrivial() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1));

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            shrines.get(0).addDeparture(shrines.get(1).getId(), type, type.ordinal() + 1);
        }

        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        for (int i = 0; i < shrines.size(); i++) {
            Map<Integer, Map<Shrine.MovableType, Integer>> departures = shrines.get(i).getDepartureMapCopy();
            assertThat(departures.size(), is(0));
        }
        for (int i = 0; i < shrines.size(); i++) {
            Map<Integer, Map<Shrine.MovableType, Integer>> arrivals = shrines.get(i).getArrivalMapCopy();
            assertThat(arrivals.size(), is(0));
        }
        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            assertThat(shrines.get(0).getMovableType(type), is(0));
        }
        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            assertThat(shrines.get(1).getMovableType(type), is(type.ordinal() + 1));
        }
    }

    @Test
    public void endTurn_processMovesCatchException() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0));
        world.addShrine(shrines.get(1));

        shrines.get(0).addDeparture(shrines.get(1).getId(), Shrine.MovableType.GOLD, 1);

        boolean thrown = false;
        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            thrown = true;
        }
        assertThat(thrown, is(true));
    }

    @Test
    public void endTurn_processMovesBackAndForth() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId()));

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            shrines.get(0).addDeparture(shrines.get(1).getId(), type, type.ordinal() + 1);
            shrines.get(1).addDeparture(shrines.get(0).getId(), type, type.ordinal() + 10);
        }

        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        for (int i = 0; i < shrines.size(); i++) {
            Map<Integer, Map<Shrine.MovableType, Integer>> departures = shrines.get(i).getDepartureMapCopy();
            assertThat(departures.size(), is(0));
        }
        for (int i = 0; i < shrines.size(); i++) {
            Map<Integer, Map<Shrine.MovableType, Integer>> arrivals = shrines.get(i).getArrivalMapCopy();
            assertThat(arrivals.size(), is(0));
        }

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            assertThat(shrines.get(0).getMovableType(type), is(type.ordinal() + 10));
        }

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            assertThat(shrines.get(1).getMovableType(type), is(type.ordinal() + 1));
        }
    }

    @Test
    public void endTurn_processMovesAddToKnownShrines() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1));

        shrines.get(0).addDeparture(shrines.get(1).getId(), Shrine.MovableType.WORKER, 1);

        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        Set<Integer> knownIds = world.getKnownIds(0);

        assertThat(knownIds.size(), is(2));
        assertThat(knownIds.contains(0), is(true));
        assertThat(knownIds.contains(1), is(true));
    }

    @Test
    public void endTurn_processMovesDontAddToKnownShrinesIfZero() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1));

        shrines.get(0).addDeparture(shrines.get(1).getId(), Shrine.MovableType.WORKER, 0);

        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        Set<Integer> knownIds = world.getKnownIds(0);

        assertThat(knownIds.size(), is(1));
        assertThat(knownIds.contains(0), is(true));
    }

    @Test
    public void endTurn_processMovesUpdateShrineState() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1));

        shrines.get(0).addDeparture(shrines.get(1).getId(), Shrine.MovableType.WORKER, 1);

        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        Map<Integer, Shrine> knownStates = world.getKnownShrineState(0);

        assertThat(knownStates.size(), is(0));
     }

    @Test
    public void endTurn_callShrineEndTurn() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId()));
        world.addShrine(shrines.get(2));
        for (Shrine shrine : shrines) {
            shrine.setNumUsedWorker(1);
            shrine.setNumGold(1);
        }

        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        for (Shrine shrine : shrines) {
            assertThat(shrine.getNumWorker(), is(1));
        }
    }

    @Test
    public void getKnownIds_returnHomeId() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0));
        world.addShrine(shrines.get(1));

        Set<Integer> knownIds = world.getKnownIds(0);

        assertThat(knownIds.size(), is(1));
        assertThat(knownIds.contains(0), is(true));
    }

    @Test
    public void getKnownIds_returnHomeIdAndKnown() {
//        List<Shrine> shrines = Utils.generateShrines(2);
//        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
//        world.addShrine(shrines.get(1));
//
//        shrines.get(0).addDeparture(shrines.get(1).getId(), Shrine.MovableType.WORKER, 1);
//
//        try {
//            world.endTurn();
//        } catch (InvalidObjectException e) {
//            assertThat(e.getMessage(), true, is(false));
//        }
    }

    /* Helper Functions */

    private void makeDiamond(World world, List<Shrine> shrines, int idx0, int idx1, int idx2, int idx3) {
        world.addShrine(shrines.get(idx0), Utils.makeConnections(shrines.get(idx1).getId(), shrines.get(idx2).getId()));
        world.addShrine(shrines.get(idx1), Utils.makeConnections(shrines.get(idx3).getId()));
        world.addShrine(shrines.get(idx2), Utils.makeConnections(shrines.get(idx3).getId()));
        world.addShrine(shrines.get(idx3));
    }

    private List<Integer> makePath(List<Shrine> shrines, int[] indexes) {
        List<Integer> path = new ArrayList<>();
        for (int idx : indexes) {
            path.add(shrines.get(idx).getId());
        }
        return path;
    }

}