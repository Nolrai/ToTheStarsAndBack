package com.garthskidstuff.shrines.Game;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by garthupshaw1 on 6/3/17.
 * Test Worl logic with paths etc.
 */
public class WorldTest {
    private World world;

    @Before
    public void setUp() {
        world = new World();
    }

    @Test
    public void getPathsTo_trivialPath() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1)));
        world.addShrine(shrines.get(1));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(1));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simplePath() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1)));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2)));
        world.addShrine(shrines.get(2));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(2));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_diamond() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        world.addShrine(shrines.get(3));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(3));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
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

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(6));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3, 4, 6}));
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3, 5, 6}));
        testAllPaths.add(makePath(shrines, new int[]{0, 2, 3, 4, 6}));
        testAllPaths.add(makePath(shrines, new int[]{0, 2, 3, 5, 6}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialLoop() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1)));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0)));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(1));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleLoop() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1)));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2)));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(0)));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(2));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialLoopWithDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1)));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0), shrines.get(2)));
        world.addShrine(shrines.get(2));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(2));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialDeadEndLoop() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1),shrines.get(2)));
        world.addShrine(shrines.get(1));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(0)));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(1));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1), shrines.get(2)));
        world.addShrine(shrines.get(1));
        world.addShrine(shrines.get(2));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(1));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(4);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1), shrines.get(2)));
        world.addShrine(shrines.get(1));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(3)));
        world.addShrine(shrines.get(3));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(1));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleDeadEndTwo() {
        List<Shrine> shrines = Utils.generateShrines(4);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1), shrines.get(2)));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(3)));
        world.addShrine(shrines.get(2));
        world.addShrine(shrines.get(3));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(3));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_oneLongOneShort() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1), shrines.get(2)));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2)));
        world.addShrine(shrines.get(2));

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(2), World.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 2}));
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_bigNetworkGetShortest() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(10);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]), shrines.get(idx[1]), shrines.get(idx[2]), shrines.get(idx[3])));
        }

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(SIZE - 1));

        Integer length = null;
        for (List<Shrine> path : allPaths) {
            if (null == length) {
                length = path.size();
            }
            assertThat(path.size(), is(length));
            assertThat(path.get(path.size() - 1), is(shrines.get(SIZE - 1)));
            for (int i = 0; i < path.size() - 1; i++) {
                List<Shrine> connections = world.get(path.get(i));
                assertThat(connections.contains(path.get(i + 1)), is(true));
            }
        }
    }

    @Test
    public void getPathsTo_bigNetworkGetAll() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(10);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]), shrines.get(idx[1]), shrines.get(idx[2]), shrines.get(idx[3])));
        }

        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(SIZE - 1), World.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        for (List<Shrine> path : allPaths) {
            assertThat(path.get(path.size() - 1), is(shrines.get(SIZE - 1)));
            for (int i = 0; i < path.size() - 1; i++) {
                List<Shrine> connections = world.get(path.get(i));
                assertThat(connections.contains(path.get(i + 1)), is(true));
            }
        }
    }

    @Test
    public void getPathsTo_findPathWithUnknowns() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        Set<Shrine> knownShrines = new HashSet<>(shrines);
        knownShrines.remove(shrines.get(2));

        Set<List<Shrine>> allPaths = world.getPaths(knownShrines, shrines.get(0), shrines.get(3));

        Set<List<Shrine>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_handleUnknownsNoPath() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(world, shrines, 0, 1, 2, 3);
        Set<Shrine> knownShrines = new HashSet<>(shrines);
        knownShrines.remove(shrines.get(1));
        knownShrines.remove(shrines.get(2));

        Set<List<Shrine>> allPaths = world.getPaths(knownShrines, shrines.get(0), shrines.get(3));

        assertThat(allPaths.size(), is(0));
    }

    @Test
    public void sortPaths_defaultToSortShortestFirst() {
        List<Shrine> shrines = Utils.generateShrines(4);
        Set<List<Shrine>> allPaths = new HashSet<>();
        List<Shrine> path0 = makePath(shrines, new int[] { 0, 1, 2, 3 });
        List<Shrine> path1 = makePath(shrines, new int[] { 0, 1 });
        allPaths.add(path0);
        allPaths.add(path1);

        List<List<Shrine>> sortedPaths = World.sortPaths(allPaths);

        assertThat(sortedPaths.size(), is(2));
        assertThat(sortedPaths.get(0), is(path1));
        assertThat(sortedPaths.get(1), is(path0));
    }

    @Test
    public void sortPaths_sortShortestFirst() {
        List<Shrine> shrines = Utils.generateShrines(4);
        Set<List<Shrine>> allPaths = new HashSet<>();
        List<Shrine> path0 = makePath(shrines, new int[] { });
        List<Shrine> path1 = makePath(shrines, new int[] { 0 });
        List<Shrine> path2 = makePath(shrines, new int[] { 0, 1 });
        List<Shrine> path3 = makePath(shrines, new int[] { 0, 1, 2 });
        List<Shrine> path4 = makePath(shrines, new int[] { 0, 1, 2, 3 });
        allPaths.add(path4);
        allPaths.add(path1);
        allPaths.add(path0);
        allPaths.add(path2);
        allPaths.add(path3);

        List<List<Shrine>> sortedPaths = World.sortPaths(allPaths, World.SORT_SHORTEST_FIRST);

        assertThat(sortedPaths.get(0), is(path0));
        assertThat(sortedPaths.get(1), is(path1));
        assertThat(sortedPaths.get(2), is(path2));
        assertThat(sortedPaths.get(3), is(path3));
        assertThat(sortedPaths.get(4), is(path4));
    }

    @Test
    public void sortPaths_bigNetwork() {
        World world = new World();
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(10);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            world.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]), shrines.get(idx[1]), shrines.get(idx[2]), shrines.get(idx[3])));
        }
        Set<List<Shrine>> allPaths = world.getPaths(shrines.get(0), shrines.get(SIZE - 1), World.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        List<List<Shrine>> sortedPaths = World.sortPaths(allPaths);

        assertThat(sortedPaths.isEmpty(), is(false));
        for (int i = 0; i < sortedPaths.size() - 1; i++) {
            assertThat(sortedPaths.get(i).size() <= sortedPaths.get(i+1).size(), is(true));
        }
    }

    @Test
    public void sortPaths_sortByPopulation() {
        List<Shrine> shrines = Utils.generateShrines(4);
        shrines.get(0).setNumWorkers(0);
        shrines.get(1).setNumWorkers(1);
        shrines.get(2).setNumWorkers(2);
        shrines.get(3).setNumWorkers(3);
        Set<List<Shrine>> allPaths = new HashSet<>();
        List<Shrine> path0 = makePath(shrines, new int[] { 0, 1 });
        List<Shrine> path1 = makePath(shrines, new int[] { 0, 2 });
        List<Shrine> path2 = makePath(shrines, new int[] { 0, 3 });
        List<Shrine> path3 = makePath(shrines, new int[] { 0, 1, 3 });
        allPaths.add(path0);
        allPaths.add(path1);
        allPaths.add(path2);
        allPaths.add(path3);

        List<List<Shrine>> sortedPaths = World.sortPaths(allPaths, World.SORT_BY_POPULATION);

        assertThat(sortedPaths.get(0), is(path0));
        assertThat(sortedPaths.get(1), is(path1));
        assertThat(sortedPaths.get(2), is(path2));
        assertThat(sortedPaths.get(3), is(path3));
    }

    @Test
    public void isCompletelyConnected_triviallyTrue() {
        List<Shrine> shrines = Utils.generateShrines(2);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1)));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0)));

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(true));
    }

    @Test
    public void isCompletelyConnected_triviallyFalse() {
        List<Shrine> shrines = Utils.generateShrines(3);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1)));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2)));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(1)));

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void isCompletelyConnected_TwoCompletelyDisconnected() {
        List<Shrine> shrines = Utils.generateShrines(4);
        world.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1)));
        world.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0)));
        world.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(3)));
        world.addShrine(shrines.get(3), Utils.makeConnections(shrines.get(2)));

        boolean result = world.isCompletelyConnected();

        assertThat(result, is(false));
    }

    /* Helper Functions */

    private void makeDiamond(World world, List<Shrine> shrines, int idx0, int idx1, int idx2, int idx3) {
        world.addShrine(shrines.get(idx0), Utils.makeConnections(shrines.get(idx1), shrines.get(idx2)));
        world.addShrine(shrines.get(idx1), Utils.makeConnections(shrines.get(idx3)));
        world.addShrine(shrines.get(idx2), Utils.makeConnections(shrines.get(idx3)));
    }

    private List<Shrine> makePath(List<Shrine> shrines, int[] indexes) {
        List<Shrine> path = new ArrayList<>();
        for (int idx : indexes) {
            path.add(shrines.get(idx));
        }
        return path;
    }

}