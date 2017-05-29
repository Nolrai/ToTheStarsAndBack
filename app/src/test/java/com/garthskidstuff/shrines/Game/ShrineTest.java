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
    public void getPathTo_findTrivialPath() {
        List<Shrine> shrines = generateShrines(2);

        shrines.get(0).getConnections().add(shrines.get(1));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).getPathTo(knownShrines, shrines.get(1));

        assertThat(tree.children.size(), is(1));
        //noinspection unchecked
        for (Tree<Shrine> t : tree.children) {
            assertThat(t.here, is(shrines.get(1)));
        }
    }

    @Test
    public void getPathTo_findEasyPath() {
        List<Shrine> shrines = generateShrines(3);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(1).getConnections().add(shrines.get(2));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).getPathTo(knownShrines, shrines.get(2));

        assertThat(tree.children.size(), is(1));
        //noinspection unchecked
        for (Tree<Shrine> t : tree.children) {
            assertThat(t.here, is(shrines.get(1)));
            assertThat(t.children.size(), is(1));
            //noinspection unchecked
            for (Tree<Shrine> t1 : t.children) {
                assertThat(t1.here, is(shrines.get(2)));
            }
        }
    }

    @Test
    public void getPathTo_findPathWithPruning() {
        List<Shrine> shrines = generateShrines(4);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(1).getConnections().add(shrines.get(2));
        shrines.get(1).getConnections().add(shrines.get(3));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).getPathTo(knownShrines, shrines.get(2));

        assertThat(tree.children.size(), is(1));
        //noinspection unchecked
        for (Tree<Shrine> t : tree.children) {
            assertThat(t.here, is(shrines.get(1)));
            assertThat(t.children.size(), is(1));
            //noinspection unchecked
            for (Tree<Shrine> t1 : t.children) {
                assertThat(t1.here, is(shrines.get(2)));
            }
        }
    }

    @Test
    public void getPathTo_findPathWithSimpleDiamond() {
        List<Shrine> shrines = generateShrines(4);
        makeDiamond(shrines, 0);

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).getPathTo(knownShrines, shrines.get(3));

        List<Tree<Shrine>> trees = generateListOfTrees(shrines);
        trees.get(0).children.add(trees.get(1));
        trees.get(0).children.add(trees.get(2));
        trees.get(1).children.add(trees.get(3));
        trees.get(2).children.add(trees.get(3));

        assertThat(trees.get(0), is(tree));
    }

    @Test
    public void getPathTo_findPathWithDoubleDiamond() {
        List<Shrine> shrines = generateShrines(7);
        makeDiamond(shrines, 0);
        makeDiamond(shrines, 3);

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).getPathTo(knownShrines, shrines.get(6));

        assertThat(tree.children.size(), is(2));
        Set<Shrine> children = new HashSet<>();
        children.add(shrines.get(1));
        children.add(shrines.get(2));
        //noinspection unchecked
        for (Tree<Shrine> t : tree.children) {
            assertThat(children.contains(t.here), is(true));
            assertThat(t.children.size(), is(1));
            //noinspection unchecked
            for (Tree<Shrine> t1 : t.children) {
                assertThat(t1.here, is(shrines.get(3)));
            }
        }
    }

    @Test
    public void getPathTo_noConnection() {
        List<Shrine> shrines = generateShrines(2);

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).getPathTo(knownShrines, shrines.get(1));

        assertThat(tree.children.size(), is(0));
    }

    @Test
    public void getPathTo_findPathWithUnknownShrines() {
        List<Shrine> shrines = generateShrines(4);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(0).getConnections().add(shrines.get(2));
        shrines.get(1).getConnections().add(shrines.get(3));
        shrines.get(2).getConnections().add(shrines.get(3));

        Set<Shrine> knownShrines = new HashSet<>(shrines);
        knownShrines.remove(shrines.get(2));

        Tree<Shrine> tree = shrines.get(0).getPathTo(knownShrines, shrines.get(3));

        assertThat(tree.children.size(), is(1));
        //noinspection unchecked
        for (Tree<Shrine> t : tree.children) {
            assertThat(t.here, is(shrines.get(1)));
            assertThat(t.children.size(), is(1));
            //noinspection unchecked
            for (Tree<Shrine> t1 : t.children) {
                assertThat(t1.here, is(shrines.get(3)));
            }
        }
    }

    @Test
    public void getPathTo_findPathWithLoop() {
        List<Shrine> shrines = generateShrines(3);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(1).getConnections().add(shrines.get(2));
        shrines.get(1).getConnections().add(shrines.get(0));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).getPathTo(knownShrines, shrines.get(2));

        assertThat(tree.children.size(), is(1));
        //noinspection unchecked
        for (Tree<Shrine> t : tree.children) {
            assertThat(t.here, is(shrines.get(1)));
            assertThat(t.children.size(), is(1));
            //noinspection unchecked
            for (Tree<Shrine> t1 : t.children) {
                assertThat(t1.here, is(shrines.get(2)));
            }
        }
    }

    @Test
    public void getPathTo_findPathInBigSet() {

        final int SIZE = 10;
        
        List<Shrine> shrines = generateShrines(SIZE);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 4; j++) {
                int idx = (i + j + 1)%SIZE;
                shrines.get(i).getConnections().add(shrines.get((i + 1) % SIZE));
            }
        }
        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> t = shrines.get(0).getPathTo(knownShrines, shrines.get(SIZE-1));
        
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

    private void makeDiamond(List<Shrine> shrines, int idx) {
        shrines.get(idx).getConnections().add(shrines.get(idx + 1));
        shrines.get(idx).getConnections().add(shrines.get(idx + 2));
        shrines.get(idx + 1).getConnections().add(shrines.get(idx + 3));
        shrines.get(idx + 2).getConnections().add(shrines.get(idx + 3));
    }

}
