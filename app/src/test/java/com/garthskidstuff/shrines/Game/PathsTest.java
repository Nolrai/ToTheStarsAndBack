package com.garthskidstuff.shrines.Game;

import org.junit.Test;

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
        List<Shrine> shrines = Utils.generateShrines(2);
        Paths path = new Paths(shrines.get(0), shrines.get(1));
        List<Shrine> connections = Utils.makeConnections(shrines.get(1));

        path.put(shrines.get(0), connections);
        List<Shrine> testConnections = path.get(shrines.get(0));

        assertThat(testConnections, is(connections));
    }

    @Test
    public void putAndGet_itemNotInPath() {
        List<Shrine> shrines = Utils.generateShrines(2);
        Paths path = new Paths(shrines.get(0), shrines.get(1));
        List<Shrine> connections = Utils.makeConnections(shrines.get(1));

        path.put(shrines.get(0), connections);
        List<Shrine> testConnections = path.get(shrines.get(1));

        assertThat((null == testConnections), is(true));
    }

}