package com.garthskidstuff.shrines.Game;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Queue;
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

    public boolean contains(final T t) {
        //Using it as a stack instead of a queue means it's a depth-first search (rather than breadth-first)
        Deque<Tree<T>> stack = new ArrayDeque<>();
        stack.add(this);
        for (Tree<T> now = stack.poll(); null != now ; now = stack.poll()) {
            if (Util.equals(t, now.here)) {
                return true;
            }
            for (Tree<T> child : now.children) {
                if (!stack.contains(child)) {
                    stack.addFirst(child);
                }
            }
        }
        return false;
    }

    public void addAll(Collection<T> leaves) {
        for (T t : leaves) {
            Tree<T> tree = new Tree<>(t);
            children.add(tree);
        }
    }

    public int size() {
        int size = 0;
        Queue<Tree<T>> queue = new ArrayDeque<>();
        queue.add(this);
        for (Tree<T> now = queue.poll(); null != now ; now = queue.poll()) {
            queue.addAll(now.children);
            size++;
        }
        return size;
    }
}
