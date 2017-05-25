package com.garthskidstuff.shrines.Game;

import org.junit.Test;

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

        assertThat(connections.size(), is(numConnections + 1 /* for the original shrine*/));

    }
}