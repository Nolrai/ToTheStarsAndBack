package com.garthskidstuff.shrines.Game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by garthupshaw1 on 5/22/17.
 */
public class ShrineTest {

    @Test
    public void getConnectedComponent_getsAllImmediateConnections() {
        Shrine shrine = new Shrine("someName", "someImageId", 5);

        int numConnections = 5;
        for (int i = 0; i < numConnections; i++) {
            Shrine connection = new Shrine("someName" + i, "someImageId" + i, 5);
            shrine.getConnections().add(connection);
        }

        Set<Shrine> connections = shrine.getConnectedComponent();

        assertThat(connections.size(), is(numConnections + 1));
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
    public void getPathTo_findPathWithDiamond() {
        List<Shrine> shrines = generateShrines(4);

        shrines.get(0).getConnections().add(shrines.get(1));
        shrines.get(0).getConnections().add(shrines.get(2));
        shrines.get(1).getConnections().add(shrines.get(3));
        shrines.get(2).getConnections().add(shrines.get(3));

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> tree = shrines.get(0).getPathTo(knownShrines, shrines.get(3));

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
        List<Shrine> shrines = generateShrines(100);

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 4; j++) {
                int idx = (i + j + 1) % 100;
                shrines.get(i).getConnections().add(shrines.get(j));
            }
        }

        Set<Shrine> knownShrines = new HashSet<>(shrines);

        Tree<Shrine> t = shrines.get(0).getPathTo(knownShrines, shrines.get(99));
        do {
            assertThat(t.children.size(), is(1));
            //noinspection unchecked
            for (Tree<Shrine> t1 : t.children) {
                t = t1;
            }
        } while(!t.children.isEmpty());
        assertThat(t.here, is(shrines.get(99)));
    }

    private List<Shrine> generateShrines(int num) {
        List<Shrine> shrines = new ArrayList<>();
        
        for (int i = 0; i < num; i++) {
            Shrine s = new Shrine("" + i, "" + i, i);
            shrines.add(s);
        }
        
        return shrines;
    }
}