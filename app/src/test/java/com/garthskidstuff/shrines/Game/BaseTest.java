package com.garthskidstuff.shrines.Game;

import org.junit.After;
import org.junit.Before;

import static org.mockito.Mockito.mock;

/**
 * Created by garthupshaw1 on 6/24/17.
 *
 */
class BaseTest {

    Logs testLogs;

    @Before
    public void setup() {
        testLogs = mock(Logs.class);
        Logger.setLogs(testLogs);
    }

    @After
    public void tearDown() {
        Logger.setLogs(null);
    }
}
