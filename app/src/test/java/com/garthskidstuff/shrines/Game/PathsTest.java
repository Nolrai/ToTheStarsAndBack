package com.garthskidstuff.shrineNames.Game;

import com.garthskidstuff.shrines.Game.Paths;
import com.garthskidstuff.shrines.Game.Utils;

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
        List<String> shrineNames = Utils.generateShrineNames(2);
        Paths path = new Paths(shrineNames.get(0), shrineNames.get(1));
        List<String> connections = Utils.makeConnections(shrineNames.get(1));

        path.put(shrineNames.get(0), connections);
        List<String> testConnections = path.get(shrineNames.get(0));

        assertThat(testConnections, is(connections));
    }

    @Test
    public void putAndGet_itemNotInPath() {
        List<String> shrineNames = Utils.generateShrineNames(2);
        Paths path = new Paths(shrineNames.get(0), shrineNames.get(1));
        List<String> connections = Utils.makeConnections(shrineNames.get(1));

        path.put(shrineNames.get(0), connections);
        List<String> testConnections = path.get(shrineNames.get(1));

        assertThat((null == testConnections), is(true));
    }

}