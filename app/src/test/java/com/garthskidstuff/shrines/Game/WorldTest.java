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
public class WorldTest {
    private World world;

    @Before
    public void setUp() {
        world = new World(new Roller(1));
    }

    @Test
    public void getPathsTo_trivialPath() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(1).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simplePath() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getName()));
        world.addShrine(shrines.get(2));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(2).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_diamond() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        world.addShrine(shrines.get(3));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(3).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
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

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(6).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3, 4, 6}));
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3, 5, 6}));
        testAllPaths.add(makePath(shrines, new int[]{0, 2, 3, 4, 6}));
        testAllPaths.add(makePath(shrines, new int[]{0, 2, 3, 5, 6}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialLoop() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getName()));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(1).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleLoop() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getName()));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(0).getName()));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(2).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialLoopWithDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getName(), shrines.get(2).getName()));
        world.addShrine(shrines.get(2));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(2).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialDeadEndLoop() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName(),shrines.get(2).getName()));
        world.addShrine(shrines.get(1));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(0).getName()));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(1).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName(), shrines.get(2).getName()));
        world.addShrine(shrines.get(1));
        world.addShrine(shrines.get(2));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(1).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(4);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName(), shrines.get(2).getName()));
        world.addShrine(shrines.get(1));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(3).getName()));
        world.addShrine(shrines.get(3));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(1).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleDeadEndTwo() {
        List<Shrine> shrines = Utils.generateShrines(4);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName(), shrines.get(2).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(3).getName()));
        world.addShrine(shrines.get(2));
        world.addShrine(shrines.get(3));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(3).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_oneLongOneShort() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName(), shrines.get(2).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getName()));
        world.addShrine(shrines.get(2));

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(2).getName(), World.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        Set<List<String>> testAllPaths = new HashSet<>();
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
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getName(), shrines.get(idx[1]).getName(), shrines.get(idx[2]).getName(), shrines.get(idx[3]).getName()));
        }

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(SIZE - 1).getName());

        Integer length = null;
        for (List<String> path : allPaths) {
            if (null == length) {
                length = path.size();
            }
            assertThat(path.size(), is(length));
            assertThat(path.get(path.size() - 1), is(shrines.get(SIZE - 1).getName()));
            for (int i = 0; i < path.size() - 1; i++) {
                List<String> connections = world.getConnections(path.get(i));
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
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getName(), shrines.get(idx[1]).getName(), shrines.get(idx[2]).getName(), shrines.get(idx[3]).getName()));
        }

        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(SIZE - 1).getName(), World.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        for (List<String> path : allPaths) {
            assertThat(path.get(path.size() - 1), is(shrines.get(SIZE - 1).getName()));
            for (int i = 0; i < path.size() - 1; i++) {
                List<String> connections = world.getConnections(path.get(i));
                assertThat(connections.contains(path.get(i + 1)), is(true));
            }
        }
    }

    @Test
    public void getPathsTo_findPathWithUnknowns() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        Set<String> knownShrines = world.getShrineNames();
        knownShrines.remove(shrines.get(2).getName());

        Set<List<String>> allPaths = world.getPaths(knownShrines, shrines.get(0).getName(), shrines.get(3).getName());

        Set<List<String>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_handleUnknownsNoPath() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        Set<String> knownShrines = world.getShrineNames();
        knownShrines.remove(shrines.get(1).getName());
        knownShrines.remove(shrines.get(2).getName());

        Set<List<String>> allPaths = world.getPaths(knownShrines, shrines.get(0).getName(), shrines.get(3).getName());

        assertThat(allPaths.size(), is(0));
    }

    @Test
    public void sortPaths_defaultToSortShortestFirst() {
        List<Shrine> shrines = Utils.generateShrines(4);
        Set<List<String>> allPaths = new HashSet<>();
        List<String> path0 = makePath(shrines, new int[] { 0, 1, 2, 3 });
        List<String> path1 = makePath(shrines, new int[] { 0, 1 });
        allPaths.add(path0);
        allPaths.add(path1);

        for (Shrine shrine : shrines) {
            world.addShrine(shrine);
        }
        List<List<String>> sortedPaths = world.sortPaths(allPaths);

        assertThat(sortedPaths.size(), is(2));
        assertThat(sortedPaths.get(0), is(path1));
        assertThat(sortedPaths.get(1), is(path0));
    }

    @Test
    public void sortPaths_sortShortestFirst() {
        List<Shrine> shrines = Utils.generateShrines(4);
        Set<List<String>> allPaths = new HashSet<>();
        List<String> path0 = makePath(shrines, new int[] { });
        List<String> path1 = makePath(shrines, new int[] { 0 });
        List<String> path2 = makePath(shrines, new int[] { 0, 1 });
        List<String> path3 = makePath(shrines, new int[] { 0, 1, 2 });
        List<String> path4 = makePath(shrines, new int[] { 0, 1, 2, 3 });
        allPaths.add(path4);
        allPaths.add(path1);
        allPaths.add(path0);
        allPaths.add(path2);
        allPaths.add(path3);

        for (Shrine shrine : shrines) {
            world.addShrine(shrine);
        }
        List<List<String>> sortedPaths = world.sortPaths(allPaths, World.SORT_SHORTEST_FIRST);

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
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getName(), shrines.get(idx[1]).getName(), shrines.get(idx[2]).getName(), shrines.get(idx[3]).getName()));
        }
        Set<List<String>> allPaths = world.getPaths(shrines.get(0).getName(), shrines.get(SIZE - 1).getName(), World.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        List<List<String>> sortedPaths = world.sortPaths(allPaths);

        assertThat(sortedPaths.isEmpty(), is(false));
        for (int i = 0; i < sortedPaths.size() - 1; i++) {
            assertThat(sortedPaths.get(i).size() <= sortedPaths.get(i+1).size(), is(true));
        }
    }

    @Test
    public void isCompletelyConnected_triviallyTrue() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getName()));

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(true));
    }

    @Test
    public void isCompletelyConnected_triviallyFalse() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getName()));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(1).getName()));

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void isCompletelyConnected_TwoCompletelyDisconnected() {
        List<Shrine> shrines = Utils.generateShrines(4);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getName()));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(3).getName()));
        world.addShrine(shrines.get(3), Utils.makeConnections(shrines.get(2).getName()));

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void isCompletelyConnected_bigConnected() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getName(), shrines.get(idx[1]).getName(), shrines.get(idx[2]).getName(), shrines.get(idx[3]).getName()));
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
                world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getName(), shrines.get(idx[1]).getName(), shrines.get(idx[2]).getName(), shrines.get(idx[3]).getName()));
            } else {
                world.addShrine(shrines.get(i));
            }
        }

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void endTurn_processMovestrivial() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1));

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            shrines.get(0).addDeparture(shrines.get(1).getName(), type, type.ordinal() + 1);
        }

        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        for (int i = 0; i < shrines.size(); i++) {
            Map<String, Map<Shrine.MovableType, Integer>> departures = shrines.get(i).getDepartureMapCopy();
            assertThat(departures.size(), is(0));
        }
        for (int i = 0; i < shrines.size(); i++) {
            Map<String, Map<Shrine.MovableType, Integer>> arrivals = shrines.get(i).getArrivalMapCopy();
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
    public void endTurn_processMovescatchException() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0));
        world.addShrine(shrines.get(1));

        shrines.get(0).addDeparture(shrines.get(1).getName(), Shrine.MovableType.GOLD, 1);

        boolean thrown = false;
        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            thrown = true;
        }
        assertThat(thrown, is(true));
    }

    @Test
    public void endTurn_processMovesbackAndForth() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getName()));

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            shrines.get(0).addDeparture(shrines.get(1).getName(), type, type.ordinal() + 1);
            shrines.get(1).addDeparture(shrines.get(0).getName(), type, type.ordinal() + 10);
        }

        try {
            world.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        for (int i = 0; i < shrines.size(); i++) {
            Map<String, Map<Shrine.MovableType, Integer>> departures = shrines.get(i).getDepartureMapCopy();
            assertThat(departures.size(), is(0));
        }
        for (int i = 0; i < shrines.size(); i++) {
            Map<String, Map<Shrine.MovableType, Integer>> arrivals = shrines.get(i).getArrivalMapCopy();
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
    public void endTurn_callShrineEndTurn() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getName()));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getName()));
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

    /* Helper Functions */

    private void makeDiamond(World world, List<Shrine> shrines, int idx0, int idx1, int idx2, int idx3) {
        world.addShrine(shrines.get(idx0), Utils.makeConnections(shrines.get(idx1).getName(), shrines.get(idx2).getName()));
        world.addShrine(shrines.get(idx1), Utils.makeConnections(shrines.get(idx3).getName()));
        world.addShrine(shrines.get(idx2), Utils.makeConnections(shrines.get(idx3).getName()));
        world.addShrine(shrines.get(idx3));
    }

    private List<String> makePath(List<Shrine> shrines, int[] indexes) {
        List<String> path = new ArrayList<>();
        for (int idx : indexes) {
            path.add(shrines.get(idx).getName());
        }
        return path;
    }

}