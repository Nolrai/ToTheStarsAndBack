package com.garthskidstuff.shrines.Game;

import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Created by garthupshaw1 on 5/24/17.
 * Tree class
 */

public class Tree<T> {
    Set<Tree<T>> children = new HashSet<>();
    T here;

    public Tree(T t) {
        here = t;
    }

    public void add(Tree<T> tree) {
        if(tree.equals(this)) {
            throw new InvalidParameterException();
        }
        children.add(tree);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    /**
     * See if a tree contains a particular value
     * @param t The value to search for
     * @return true iff the tree caontains t
     */
    public boolean contains(final T t) {
        return (null != getDescendent(t));
    }

    /**
     * Get a Tree node with a particular target value
     * @param t The target to search for
     * @return The tree node or null if not found
     */
    public Tree<T> getDescendent(final T t) {
        Deque<Tree<T>> stack = new ArrayDeque<>();
        Set<Tree<T>> visited = new HashSet<>();
        stack.add(this);
        for (Tree<T> now = stack.poll(); null != now ; now = stack.poll()) {
            if (Util.equals(t, now.here)) {
                return now;
            }
            for (Tree<T> child : now.children) {
                if (!visited.contains(child)) {
                    stack.addFirst(child);
                    visited.add(child);
                }
            }
        }
        return null;
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

    @Override
    public int hashCode() {
        int hash = 0x248d0814; // 5240c175
        hash ^= here.hashCode();
        return Integer.rotateLeft(hash, (children.size() % 8));
    }

    @Override
    public boolean equals(Object other) {
        boolean ret = false;
        if (other instanceof Tree<?>) {
            //noinspection unchecked
            Tree<T> otherTree = (Tree<T>) other; //TODO how to test if other is right kind?
            String s = toString();
            String otherS = otherTree.toString();
            return Util.equals(s, otherS);
//            Set<Tree<T>> parents = new HashSet<>();
//            ret = testEquality(otherTree, parents);
        }
        return ret;
    }

    private boolean testEquality(Tree<T> otherTree, Set<Tree<T>> parents) {
        boolean ret = false;
        if (Util.equals(here, otherTree.here)) { // here must be the same on both
            if (children.size() == otherTree.children.size()) {
                ret = true;
                if (!parents.contains(this)) {
                    parents.add(this);
                    for (Tree<T> child : children) {
                        boolean found = false;
                        for (Tree<T> otherChild : otherTree.children) {
                            if (child.testEquality(otherChild, parents)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            ret = false;
                            break;
                        }
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        Set<Tree<T>> parents = new HashSet<>();
        return toStringHelper(parents, 0);
    }

    private String toStringHelper(Set<Tree<T>> parents, int indentation) {
        String s = "";
        for (int i = 0; i < indentation; i++) {
            s += " ";
        }
        s += here.toString();
        s += ": ";
        if (!parents.contains(this)) {
            parents.add(this);
            for (Tree<T> child : children) {
                s += child.here.toString() + " ";
            }
            s += "\n";
            for (Tree<T> child : children) {
                s += child.toStringHelper(parents, indentation + 1);
            }

        } else {
            s += "\n";
        }
        return s;
    }
}
