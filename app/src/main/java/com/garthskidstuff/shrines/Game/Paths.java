package com.garthskidstuff.shrines.Game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by garthtroubleupshaw on 6/1/17.
 * Capture all paths from a startId Integer to an endId Integer
 */

public class Paths {
    public final Integer startId;
    public final Filter<Shrine> endFilter;
    public int maxLength;
    public final Map<Integer, List<Integer>> map = new HashMap<>();
    public final Board board;

    @SuppressWarnings("unused")
    public Paths(int startId, Filter<Shrine> endFilter, Board board) {
        this(startId, endFilter, -1, board);
    }

    public Paths(int startId, Filter<Shrine> endFilter, int maxLength, Board board) {
        this.startId = startId;
        this.endFilter = endFilter;
        this.maxLength = maxLength;
        this.board = board;
    }

    List<Integer> put(Integer shrineId, List<Integer> connections) {
       return map.put(shrineId, connections);
    }

    List<Integer> get(Integer shrineId) {
        return map.get(shrineId);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paths)) return false;

        Paths paths = (Paths) o;

        if (maxLength != paths.maxLength) return false;
        if (!startId.equals(paths.startId)) return false;
        if (!endFilter.equals(paths.endFilter)) return false;
        if (!map.equals(paths.map)) return false;
        return board.equals(paths.board);

    }

    @Override
    public int hashCode() {
        int result = startId.hashCode();
        result = 31 * result + endFilter.hashCode();
        result = 31 * result + maxLength;
        result = 31 * result + map.hashCode();
        result = 31 * result + board.hashCode();
        return result;
    }

    boolean filterShrine(Integer shrineId) {
        return endFilter.test(board.getShrine(shrineId));
    }

    Set<List<Integer>> makeSetOfPathsFrom() {
        Set<List<Integer>> allPaths = new HashSet<>();
        List<Integer> path1 = Utils.makeList(startId);
        allPaths.add(path1);

        boolean keepGoing;
        do {
            keepGoing = false;
            Set<List<Integer>> newAllPaths = new HashSet<>();
            newAllPaths.addAll(allPaths);

            for (List<Integer> path : allPaths) {
                Integer endPath = path.get(path.size() - 1);
                boolean tooLong = (-1 != maxLength) && (maxLength < path.size());
                if (!tooLong && filterShrine(endPath)) { // A.K.A the path is unfinished.
                    List<Integer> connections = get(endPath);
                    newAllPaths.remove(path);
                    if (null != connections) {
                        for (Integer shrineId : connections) {
                            if (!path.contains(shrineId)) { // not a loop
                                List<Integer> newPath = Utils.makeList();
                                newPath.addAll(path);
                                if ((-1 == maxLength) ||
                                        (newPath.size() < maxLength) ||
                                        (filterShrine(shrineId))) {
                                    newPath.add(shrineId);
                                    newAllPaths.add(newPath);
                                    keepGoing = true;
                                }
                            }
                        }
                    }
                }
            }

            allPaths = newAllPaths;
        } while (keepGoing);

        return allPaths;
    }
}
