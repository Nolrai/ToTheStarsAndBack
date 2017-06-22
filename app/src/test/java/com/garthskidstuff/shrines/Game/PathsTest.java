package com.garthskidstuff.shrines.Game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Test Paths
 */
public class PathsTest {

    @Test
    public void putAndGet_singleItem() {
        List<Integer> shrineIds = generateShrineIds(2);
        Paths path = new Paths(shrineIds.get(0), shrineIds.get(1));
        List<Integer> connections = Utils.makeConnections(shrineIds.get(1));

        path.put(shrineIds.get(0), connections);
        List<Integer> testConnections = path.get(shrineIds.get(0));

        assertThat(testConnections, is(connections));
    }

    @Test
    public void putAndGet_itemNotInPath() {
        List<Integer> shrineIds = generateShrineIds(2);
        Paths path = new Paths(shrineIds.get(0), shrineIds.get(1));
        List<Integer> connections = Utils.makeConnections(shrineIds.get(1));

        path.put(shrineIds.get(0), connections);
        List<Integer> testConnections = path.get(shrineIds.get(1));

        assertThat((null == testConnections), is(true));
    }


    private List<Integer> generateShrineIds(int num) {
        List<Integer> shrineIds = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            shrineIds.add(i);
        }

        return shrineIds;
    }
}