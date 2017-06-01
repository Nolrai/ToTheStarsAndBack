package com.garthskidstuff.shrines.Game;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/** This is a serries of tests of the Shrine class.
 * Created by garthupshaw1 on 5/22/17.
 */
public class ShrineTest {
    @Test
    public void findPathsTo_findTrivialPath() {
        List<Shrine> shrines = generateShrines(2);

        shrines.get(0).getConnections().add(shrines.get(1));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(1));

        List<Tree<Shrine>> testTree = generateListOfTrees(shrines);
        testTree.get(0).add(testTree.get(1));

        assertThat(testTree.get(0), is(tree));
    }

    @Test
    public void findPathsTo_findEasyPath() {
        List<Shrine> shrines = generateShrines(3);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(1).getConnections().add(shrines.get(2));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(2));

        List<Tree<Shrine>> testTree = generateListOfTrees(shrines);
        testTree.get(0).add(testTree.get(1));
        testTree.get(1).add(testTree.get(2));

        assertThat(testTree.get(0), is(tree));
    }

    @Test
    public void findPathsTo_findPathWithPruning() {
        List<Shrine> shrines = generateShrines(4);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(1).getConnections().add(shrines.get(2));
        shrines.get(1).getConnections().add(shrines.get(3));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(2));

        List<Tree<Shrine>> trees = generateListOfTrees(shrines);
        trees.get(0).add(trees.get(1));
        trees.get(1).add(trees.get(2));
        trees.get(1).add(trees.get(3));

        assertThat(trees.get(0), is(tree));
    }

    @Test
    public void findPathsTo_findPathWithSimpleDiamond() {
        List<Shrine> shrines = generateShrines(4);
        makeDiamond(shrines, 0, 1, 2, 3);

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(3));

        List<Tree<Shrine>> trees = generateListOfTrees(shrines);
        trees.get(0).add(trees.get(1));
        trees.get(0).add(trees.get(2));
        trees.get(1).add(trees.get(3));
        trees.get(2).add(trees.get(3));

        assertThat(trees.get(0), is(tree));
    }

    @Test
    public void findPathsTo_findPathWithDoubleDiamond() {
        List<Shrine> shrines = generateShrines(7);
        makeDiamond(shrines, 0, 1, 2, 3);
        makeDiamond(shrines, 3, 4, 5, 6);

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(6));

        List<Tree<Shrine>> testTree = generateListOfTrees(shrines);
        testTree.get(0).add(testTree.get(1));
        testTree.get(0).add(testTree.get(2));
        testTree.get(1).add(testTree.get(3));
        testTree.get(2).add(testTree.get(3));
        testTree.get(3).add(testTree.get(4));
        testTree.get(3).add(testTree.get(5));
        testTree.get(4).add(testTree.get(6));
        testTree.get(5).add(testTree.get(6));

        assertThat(tree, is(testTree.get(0)));
    }

    @Test
    public void findPathsTo_findPathDiamondTwig() {
        List<Shrine> shrines = generateShrines(5);
        makeDiamond(shrines, 0, 1, 2, 3);
        shrines.get(3).getConnections().add(shrines.get(4));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(4));

        List<Tree<Shrine>> testTree = generateListOfTrees(shrines);
        testTree.get(0).add(testTree.get(1));
        testTree.get(0).add(testTree.get(2));
        testTree.get(1).add(testTree.get(3));
        testTree.get(2).add(testTree.get(3));
        testTree.get(3).add(testTree.get(4));

        assertThat(tree, is(testTree.get(0)));
    }

    @Test
    public void findPathsTo_findPathWithHorizontalDiamond() {
        List<Shrine> shrines = generateShrines(5);
        makeDiamond(shrines, 0, 1, 2, 4);
        makeDiamond(shrines, 0, 1, 3, 4);

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(3));

        List<Tree<Shrine>> trees = generateListOfTrees(shrines);
        trees.get(0).add(trees.get(1));
        trees.get(0).add(trees.get(2));
        trees.get(0).add(trees.get(3));
        trees.get(1).add(trees.get(4));
        trees.get(2).add(trees.get(4));
        trees.get(3).add(trees.get(4));

        assertThat(trees.get(0), is(tree));
    }

    @Test
    public void findPathsTo_pruneNode() {
        List<Shrine> shrines = generateShrines(3);
        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(1).getConnections().add(shrines.get(2));
        shrines.get(0).getConnections().add(shrines.get(2));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(1));

        List<Tree<Shrine>> trees = generateListOfTrees(shrines);
        trees.get(0).add(trees.get(1));

        assertThat(tree, is(trees.get(0)));
    }

    @Test
    public void findPathsTo_pruneNode0z12x1z2x2z3() {
        List<Shrine> shrines = generateShrines(4);
        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(1).getConnections().add(shrines.get(2));
        shrines.get(0).getConnections().add(shrines.get(2));
        shrines.get(2).getConnections().add(shrines.get(3));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(1));

        List<Tree<Shrine>> trees = generateListOfTrees(shrines);
        trees.get(0).add(trees.get(1));

        assertThat(tree, is(trees.get(0)));
    }

    @Test
    public void findPathsTo_pruneNode0z123() {
        List<Shrine> shrines = generateShrines(4);
        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(0).getConnections().add(shrines.get(2));
        shrines.get(0).getConnections().add(shrines.get(3));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(1));

        List<Tree<Shrine>> trees = generateListOfTrees(shrines);
        trees.get(0).add(trees.get(1));

        assertThat(tree, is(trees.get(0)));
    }

    @Test
    public void findPathsTo_pruneNode0z123x1z4x2z4() {
        List<Shrine> shrines = generateShrines(5);
        makeDiamond(shrines, 0, 1, 2, 4);
        shrines.get(0).getConnections().add(shrines.get(3));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(1));

        List<Tree<Shrine>> trees = generateListOfTrees(shrines);
        trees.get(0).add(trees.get(1));
        trees.get(0).add(trees.get(2));
        trees.get(1).add(trees.get(4));
        trees.get(2).add(trees.get(4));

        assertThat(tree, is(trees.get(0)));
    }

    @Test
    public void findPathsTo_noConnection() {
        List<Shrine> shrines = generateShrines(2);

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(1));

        assertThat((null == tree), is(true));
    }

    @Test
    public void findPathsTo_findPathWithUnknownShrines() {
        List<Shrine> shrines = generateShrines(4);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(0).getConnections().add(shrines.get(2));
        shrines.get(1).getConnections().add(shrines.get(3));
        shrines.get(2).getConnections().add(shrines.get(3));

        Set<Shrine> knownShrines = new HashSet<>(shrines);
        knownShrines.remove(shrines.get(2));

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(3));

        List<Tree<Shrine>> testTree = generateListOfTrees(shrines);
        testTree.get(0).add(testTree.get(1));
        testTree.get(1).add(testTree.get(3));

        assertThat(testTree.get(0), is(tree));
    }

    @Test
    public void findPathsTo_noConnectionBecauseOfUnknownShrines() {
        List<Shrine> shrines = generateShrines(3);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(1).getConnections().add(shrines.get(2));

        Set<Shrine> knownShrines = new HashSet<>(shrines);
        knownShrines.remove(shrines.get(1));

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(2));

        assertThat((null == tree), is(true));
    }

    @Test
    public void findPathsTo_findPathWithLoop() {
        List<Shrine> shrines = generateShrines(3);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(1).getConnections().add(shrines.get(2));
        shrines.get(1).getConnections().add(shrines.get(0));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).findPathsTo(knownShrines, shrines.get(2));

        List<Tree<Shrine>> testTree = generateListOfTrees(shrines);
        testTree.get(0).add(testTree.get(1));
        testTree.get(1).add(testTree.get(2));

        assertThat(testTree.get(0), is(tree));
    }

    @Test
    public void findPathsTo_findPathInBigSet() {

        final int SIZE = 10;
        
        List<Shrine> shrines = generateShrines(SIZE);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 4; j++) {
                int idx = (i + j + 1) % SIZE;
                shrines.get(i).getConnections().add(shrines.get(idx));
            }
        }
        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> t = shrines.get(0).findPathsTo(knownShrines, shrines.get(SIZE-1));
        
        do {
            assertThat(t.children.size() <= 4, is(true));
            //noinspection unchecked
            for (Tree<Shrine> t1 : t.children) {
                t = t1;
            }
        } while(!t.children.isEmpty());

        assertThat(t.here, is(shrines.get(SIZE-1)));
    }

    private List<Shrine> generateShrines(int num) {
        List<Shrine> shrines = new ArrayList<>();
        
        for (int i = 0; i < num; i++) {
            Shrine s = new Shrine("" + i, "" + i, i);
            shrines.add(s);
        }
        
        return shrines;
    }

    private List<Tree<Shrine>> generateListOfTrees(List<Shrine> shrines) {
        List<Tree<Shrine>> trees = new ArrayList<>();
        for (Shrine shrine : shrines) {
            trees.add(new Tree<>(shrine));
        }

        return trees;
    }

    private void makeDiamond(List<Shrine> shrines, int idx0, int idx1, int idx2, int idx3) {
        shrines.get(idx0).getConnections().add(shrines.get(idx1));
        shrines.get(idx0).getConnections().add(shrines.get(idx2));
        shrines.get(idx1).getConnections().add(shrines.get(idx3));
        shrines.get(idx2).getConnections().add(shrines.get(idx3));
    }

}
