package com.garthskidstuff.shrines.Game;

import org.junit.Before;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by garthupshaw1 on 6/3/17.
 * Test Board logic with paths etc.
 */
public class BoardEngineTest extends BaseTest {
    private BoardEngine board;

    @Before
    public void setUp() {
        board = new BoardEngine(new Roller(1));
        List<Integer> homeIds = new ArrayList<>();
        homeIds.add(0);
        homeIds.add(1);
        board.initForHomeIds(homeIds);
    }

    @Test
    public void isCompletelyConnected_triviallyTrue() {
        List<Shrine> shrines = Utils.generateShrines(2);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        board.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId()));

        boolean result = board.isCompletelyConnected();

        assertThat(result, is(true));
    }

    @Test
    public void isCompletelyConnected_triviallyFalse() {
        List<Shrine> shrines = Utils.generateShrines(3);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        board.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getId()));
        board.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(1).getId()));

        boolean result = board.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void isCompletelyConnected_TwoCompletelyDisconnected() {
        List<Shrine> shrines = Utils.generateShrines(4);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        board.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId()));
        board.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(3).getId()));
        board.addShrine(shrines.get(3), Utils.makeConnections(shrines.get(2).getId()));

        boolean result = board.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void isCompletelyConnected_bigConnected() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            board.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
        }

        boolean result = board.isCompletelyConnected();

        assertThat(result, is(true));
    }

    @Test
    public void isCompletelyConnected_semiConnected() {
        int SIZE = 10;
        List<Shrine> shrines = Utils.generateShrines(SIZE);
        for (int i = 0; i < SIZE; i++) {
            int[] idx = new int[]{(i + 1) % SIZE, (i + 2) % SIZE, (i + 3) % SIZE, (i + 4) % SIZE};
            if (0 < i) {
                board.addShrine(shrines.get(i), Utils.makeConnections(shrines.get(idx[0]).getId(), shrines.get(idx[1]).getId(), shrines.get(idx[2]).getId(), shrines.get(idx[3]).getId()));
            } else {
                board.addShrine(shrines.get(i));
            }
        }

        boolean result = board.isCompletelyConnected();

        assertThat(result, is(false));
    }

    @Test
    public void endTurn_processMovesTrivial() {
        List<Shrine> shrines = Utils.generateShrines(2);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        board.addShrine(shrines.get(1));

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            shrines.get(0).addDeparture(shrines.get(1).getId(), type, type.ordinal() + 1);
        }

        try {
            board.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        for (int i = 0; i < shrines.size(); i++) {
            Map<Integer, Map<Shrine.MovableType, Integer>> departures = shrines.get(i).getDepartureMapCopy();
            assertThat(departures.size(), is(0));
        }
        for (int i = 0; i < shrines.size(); i++) {
            Map<Integer, Map<Shrine.MovableType, Integer>> arrivals = shrines.get(i).getArrivalMapCopy();
            assertThat(arrivals.size(), is(0));
        }
        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            assertThat(shrines.get(0).getMovableType(type), is(0));
        }
        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            assertThat(shrines.get(1).getMovableType(type), is(type.ordinal() + 1));
        }
    }

    @Test
    public void endTurn_processMovesCatchException() {
        List<Shrine> shrines = Utils.generateShrines(2);
        board.addShrine(shrines.get(0));
        board.addShrine(shrines.get(1));

        shrines.get(0).addDeparture(shrines.get(1).getId(), Shrine.MovableType.GOLD, 1);

        boolean thrown = false;
        try {
            board.endTurn();
        } catch (InvalidObjectException e) {
            thrown = true;
        }
        assertThat(thrown, is(true));
    }

    @Test
    public void endTurn_processMovesBackAndForth() {
        List<Shrine> shrines = Utils.generateShrines(2);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        board.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId()));

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            shrines.get(0).addDeparture(shrines.get(1).getId(), type, type.ordinal() + 1);
            shrines.get(1).addDeparture(shrines.get(0).getId(), type, type.ordinal() + 10);
        }

        try {
            board.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        for (int i = 0; i < shrines.size(); i++) {
            Map<Integer, Map<Shrine.MovableType, Integer>> departures = shrines.get(i).getDepartureMapCopy();
            assertThat(departures.size(), is(0));
        }
        for (int i = 0; i < shrines.size(); i++) {
            Map<Integer, Map<Shrine.MovableType, Integer>> arrivals = shrines.get(i).getArrivalMapCopy();
            assertThat(arrivals.size(), is(0));
        }

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            assertThat(shrines.get(0).getMovableType(type), is(type.ordinal() + 10));
        }

        for (Shrine.MovableType type : Shrine.MovableType.values()) {
            assertThat(shrines.get(1).getMovableType(type), is(type.ordinal() + 1));
        }
    }

    @Test
    public void endTurn_processMovesAddToKnownShrines() {
        List<Shrine> shrines = Utils.generateShrines(2);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        board.addShrine(shrines.get(1));

        shrines.get(0).addDeparture(shrines.get(1).getId(), Shrine.MovableType.WORKER, 1);

        try {
            board.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        Board playerBoard = board.getBoardForPlayer(0);

        assertThat(playerBoard.getShrineIds().size(), is(2));
        assertThat(playerBoard.getShrineIds().contains(0), is(true));
        assertThat(playerBoard.getShrineIds().contains(1), is(true));
    }

    @Test
    public void endTurn_processMovesDontAddToKnownShrinesIfZero() {
        List<Shrine> shrines = Utils.generateShrines(2);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        board.addShrine(shrines.get(1));

        shrines.get(0).addDeparture(shrines.get(1).getId(), Shrine.MovableType.WORKER, 0);

        try {
            board.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        Board playerBoard = board.getBoardForPlayer(0);

        assertThat(playerBoard.getShrineIds().size(), is(1));
        assertThat(playerBoard.getShrineIds().contains(0), is(true));
    }

    @Test
    public void endTurn_processMovesSavesShrineState() {
        List<Shrine> shrines = Utils.generateShrines(3);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(2).getId()));
        board.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(2).getId()));
        board.addShrine(shrines.get(2), Utils.makeConnections(shrines.get(0).getId()));

        shrines.get(0).addDeparture(shrines.get(2).getId(), Shrine.MovableType.WORKER, 1);

        try {
            board.endTurn();

            shrines.get(2).addDeparture(shrines.get(0).getId(), Shrine.MovableType.WORKER, 1);
            shrines.get(1).addDeparture(shrines.get(2).getId(), Shrine.MovableType.WORKER, 1);

            board.endTurn();
            board.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        Board playerBoard = board.getBoardForPlayer(0);

        assertThat(playerBoard.getShrineIds().size(), is(2));
        assertThat(playerBoard.getShrineIds().contains(0), is(true));
        assertThat(playerBoard.getShrineIds().contains(2), is(true));
        Shrine shrine = playerBoard.getShrine(2);
        assertThat(shrine.getLastSeenTurnNum(), is(2));
        shrine = playerBoard.getShrine(0);
        assertThat(shrine.getLastSeenTurnNum(), is(3));
    }

    @Test
    public void endTurn_returnSuicideScout() {
        List<Shrine> shrines = Utils.generateShrines(2);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        board.addShrine(shrines.get(1));

        shrines.get(1).setMovableType(Shrine.MovableType.FIGHTER, 100);
        shrines.get(0).addDeparture(shrines.get(1).getId(), Shrine.MovableType.WORKER, 1);

        try {
            board.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        Board playerBoard = board.getBoardForPlayer(0);

        assertThat(playerBoard.getShrineIds().size(), is(2));
        assertThat(playerBoard.getShrineIds().contains(0), is(true));
        assertThat(playerBoard.getShrineIds().contains(1), is(true));
        assertThat(shrines.get(1).getOwnerId(), is(1));
    }

    @Test
    public void endTurn_callShrineEndTurn() {
        List<Shrine> shrines = Utils.generateShrines(3);
        board.addShrine(shrines.get(0), Utils.makeConnections(shrines.get(1).getId()));
        board.addShrine(shrines.get(1), Utils.makeConnections(shrines.get(0).getId()));
        board.addShrine(shrines.get(2));
        for (Shrine shrine : shrines) {
            shrine.setNumUsedWorker(1);
            shrine.setNumGold(1);
        }

        try {
            board.endTurn();
        } catch (InvalidObjectException e) {
            assertThat(e.getMessage(), true, is(false));
        }

        for (Shrine shrine : shrines) {
            assertThat(shrine.getNumWorker(), is(1));
        }
    }

    @Test
    public void getBoardForPlayer_returnHomeId() {
        List<Shrine> shrines = Utils.generateShrines(2);
        board.addShrine(shrines.get(0));
        board.addShrine(shrines.get(1));

        Board playerBoard = board.getBoardForPlayer(0);

        assertThat(playerBoard.getShrineIds().size(), is(1));
        assertThat(playerBoard.getShrineIds().contains(0), is(true));
    }

}