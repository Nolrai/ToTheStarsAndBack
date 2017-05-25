package com.garthskidstuff.shrines.Game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by garthupshaw1 on 5/24/17.
 */

public class Tree<T> {
    Set<Tree<T>> children = new HashSet<>();
    T here;

    public Tree(T t) {
        here = t;
    }

    public void add(Tree tree) {
        children.add(tree);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean contains(T t) {
        if (t == here) {
            return true;
        }
        for (Tree child : children) {
            if (child.contains(t)) {
                return true;
            }
        }
        return false;
    }

    public void addAll(List<T> leaves) {
        for (T t : leaves) {
            Tree<T> tree = new Tree<>(t);
            children.add(tree);
        }
    }
}
