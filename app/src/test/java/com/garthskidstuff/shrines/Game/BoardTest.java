package com.garthskidstuff.shrines.Game;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by garthupshaw1 on 6/3/17.
 * Test Board logic with paths etc.
 */
public class BoardTest extends BaseTest {
    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void getPathsTo_trivialPath() {
        List<Shrine> shrines = Utils.generateShrines(2);
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId()));
        board.addShrine(shrines.get(1));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simplePath() {
        List<Shrine> shrines = Utils.generateShrines(3);
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId()));
        board.addShrine(shrines.get(1), Utils.makeList(shrines.get(2).getId()));
        board.addShrine(shrines.get(2));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(2).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_diamond() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(board, shrines, 0, 1, 2, 3);
        board.addShrine(shrines.get(3));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(3).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));
        testAllPaths.add(makePath(shrines, new int[]{0, 2, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_doubleDiamond() {
        List<Shrine> shrines = Utils.generateShrines(7);
        makeDiamond(board, shrines, 0, 1, 2, 3);
        makeDiamond(board, shrines, 3, 4, 5, 6);
        board.addShrine(shrines.get(6));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(6).getId());

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
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId()));
        board.addShrine(shrines.get(1), Utils.makeList(shrines.get(0).getId()));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleLoop() {
        List<Shrine> shrines = Utils.generateShrines(3);
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId()));
        board.addShrine(shrines.get(1), Utils.makeList(shrines.get(2).getId()));
        board.addShrine(shrines.get(2), Utils.makeList(shrines.get(0).getId()));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(2).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialLoopWithDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(3);
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId()));
        board.addShrine(shrines.get(1), Utils.makeList(shrines.get(0).getId(), shrines.get(2).getId()));
        board.addShrine(shrines.get(2));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(2).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 2}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialDeadEndLoop() {
        List<Shrine> shrines = Utils.generateShrines(3);
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId(),shrines.get(2).getId()));
        board.addShrine(shrines.get(1));
        board.addShrine(shrines.get(2), Utils.makeList(shrines.get(0).getId()));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_trivialDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(3);
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId(), shrines.get(2).getId()));
        board.addShrine(shrines.get(1));
        board.addShrine(shrines.get(2));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleDeadEnd() {
        List<Shrine> shrines = Utils.generateShrines(4);
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId(), shrines.get(2).getId()));
        board.addShrine(shrines.get(1));
        board.addShrine(shrines.get(2), Utils.makeList(shrines.get(3).getId()));
        board.addShrine(shrines.get(3));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(1).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_simpleDeadEndTwo() {
        List<Shrine> shrines = Utils.generateShrines(4);
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId(), shrines.get(2).getId()));
        board.addShrine(shrines.get(1), Utils.makeList(shrines.get(3).getId()));
        board.addShrine(shrines.get(2));
        board.addShrine(shrines.get(3));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(3).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_oneLongOneShort() {
        List<Shrine> shrines = Utils.generateShrines(3);
        board.addShrine(shrines.get(0), Utils.makeList(shrines.get(1).getId(), shrines.get(2).getId()));
        board.addShrine(shrines.get(1), Utils.makeList(shrines.get(2).getId()));
        board.addShrine(shrines.get(2));

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(2).getId(), BoardEngine.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

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
            board.addShrine(shrines.get(i), Utils.makeList(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
        }

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(SIZE - 1).getId());

        Integer length = null;
        for (List<Integer> path : allPaths) {
            if (null == length) {
                length = path.size();
            }
            assertThat(path.size(), is(length));
            assertThat(path.get(path.size() - 1), is(shrines.get(SIZE - 1).getId()));
            for (int i = 0; i < path.size() - 1; i++) {
                List<Integer> connections = board.getConnections(path.get(i));
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
            board.addShrine(shrines.get(i), Utils.makeList(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
        }

        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(SIZE - 1).getId(), BoardEngine.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        for (List<Integer> path : allPaths) {
            assertThat(path.get(path.size() - 1), is(shrines.get(SIZE - 1).getId()));
            for (int i = 0; i < path.size() - 1; i++) {
                List<Integer> connections = board.getConnections(path.get(i));
                assertThat(connections.contains(path.get(i + 1)), is(true));
            }
        }
    }

    @Test
    public void getPathsTo_findPathWithUnknowns() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(board, shrines, 0, 1, 2, 3);
        Set<Integer> knownShrines = board.getShrineIds();
        knownShrines.remove(shrines.get(2).getId());

        Set<List<Integer>> allPaths = board.getPaths(knownShrines, shrines.get(0).getId(), shrines.get(3).getId());

        Set<List<Integer>> testAllPaths = new HashSet<>();
        testAllPaths.add(makePath(shrines, new int[]{0, 1, 3}));

        assertThat(allPaths, is(testAllPaths));
    }

    @Test
    public void getPathsTo_handleUnknownsNoPath() {
        List<Shrine> shrines = Utils.generateShrines(4);
        makeDiamond(board, shrines, 0, 1, 2, 3);
        Set<Integer> knownShrines = board.getShrineIds();
        knownShrines.remove(shrines.get(1).getId());
        knownShrines.remove(shrines.get(2).getId());

        Set<List<Integer>> allPaths = board.getPaths(knownShrines, shrines.get(0).getId(), shrines.get(3).getId());

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
            board.addShrine(shrine);
        }
        List<List<Integer>> sortedPaths = board.sortPaths(allPaths);

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
            board.addShrine(shrine);
        }
        List<List<Integer>> sortedPaths = board.sortPaths(allPaths, Board.SORT_SHORTEST_FIRST);

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
            board.addShrine(shrines.get(i), Utils.makeList(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
        }
        Set<List<Integer>> allPaths = board.getPaths(shrines.get(0).getId(), shrines.get(SIZE - 1).getId(), BoardEngine.FindPathSettings.useMaxDepth(Integer.MAX_VALUE));

        List<List<Integer>> sortedPaths = board.sortPaths(allPaths);

        assertThat(sortedPaths.isEmpty(), is(false));
        for (int i = 0; i < sortedPaths.size() - 1; i++) {
            assertThat(sortedPaths.get(i).size() <= sortedPaths.get(i+1).size(), is(true));
        }
    }

    /* Helper Functions */

    private void makeDiamond(Board board, List<Shrine> shrines, int idx0, int idx1, int idx2, int idx3) {
        board.addShrine(shrines.get(idx0), Utils.makeList(shrines.get(idx1).getId(), shrines.get(idx2).getId()));
        board.addShrine(shrines.get(idx1), Utils.makeList(shrines.get(idx3).getId()));
        board.addShrine(shrines.get(idx2), Utils.makeList(shrines.get(idx3).getId()));
        board.addShrine(shrines.get(idx3));
    }

    private List<Integer> makePath(List<Shrine> shrines, int[] indexes) {
        List<Integer> path = Utils.makeList();
        for (int idx : indexes) {
            path.add(shrines.get(idx).getId());
        }
        return path;
    }

}