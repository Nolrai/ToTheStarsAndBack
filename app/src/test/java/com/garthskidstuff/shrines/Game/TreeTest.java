package com.garthskidstuff.shrines.Game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by garthupshaw1 on 5/28/17.
 */
public class TreeTest {

    @Test
    public void constructor_setHere() {
        String s = "someString";

        Tree<String> tree = new Tree<>(s);

        assertThat(tree.here, is(s));
    }

    @Test
    public void add_addsTree() {
        String s1 = "1";
        String s2 = "2";
        Tree<String> tree1 = new Tree<>(s1);
        Tree<String> tree2 = new Tree<>(s2);

        tree1.add(tree2);

        assertThat(tree2.children.size(), is(0));
        assertThat(tree1.children.size(), is(1));
        for(Tree<String> t : tree1.children) {
            assertThat(t.here, is(s2));
        }
    }

    @Test
    public void isLeaf_true() {
        String s = "someString";

        Tree<String> tree = new Tree<>(s);

        assertThat(tree.isLeaf(), is(true));
    }

    @Test
    public void isLeaf_false() {
        String s1 = "1";
        String s2 = "2";
        Tree<String> tree1 = new Tree<>(s1);
        Tree<String> tree2 = new Tree<>(s2);

        tree1.add(tree2);

        assertThat(tree2.isLeaf(), is(true));
        assertThat(tree1.isLeaf(), is(false));
    }

    @Test
    public void contains_self() {
        String s = "String";
        Tree<String> tree = new Tree<>(s);

        assertThat(tree.contains(s), is(true));
    }

    @Test
    public void contains_true() {
        String s1 = "1";
        String s2 = "2";
        Tree<String> tree1 = new Tree<>(s1);
        Tree<String> tree2 = new Tree<>(s2);

        tree1.add(tree2);

        assertThat(tree1.contains(s2), is(true));
    }

    @Test
    public void contains_false() {
        String s1 = "1";
        String s2 = "2";
        Tree<String> tree1 = new Tree<>(s1);
        Tree<String> tree2 = new Tree<>(s2);

        tree1.add(tree2);

        assertThat(tree2.contains(s1), is(false));
    }

    @Test
    public void contains_deepSimple() {
        String sLeft= "Left";
        String sRight= "Right";
        final int size = 50;

        String[] stringsLeft = new String[size];
        for (int i = 0; i < size; i++) {
            stringsLeft[i] = sLeft + i;
        }
        String[] stringsRight = new String[size];
        for (int i = 0; i < size; i++) {
            stringsRight[i] = sRight + i;
        }

        Tree<String> treeLeft = makeDeepLinearTree(stringsLeft);
        Tree<String> treeRight = makeDeepLinearTree(stringsRight);

        Tree<String> root = new Tree<> ("Root");
        root.add(treeLeft);
        root.add(treeRight);

        assertThat(root.contains(sLeft+(size - 1)), is(true));
        assertThat(root.contains(sRight+(size - 1)), is(true));
    }

    @Test
    public void contains_wideSimple() {
        final int size = 50;

        Tree<String> root = new Tree<> ("Root");

        for (int i = 0; i < size; i++) {
            root.add(new Tree<String>("" + i));
        }
        assertThat(root.contains("0"), is(true));
        assertThat(root.contains("" + (size-1)), is(true));
    }

    @Test
    public void contains_deepConnected() {
        final int size = 50;

        List<Tree<String>> trees = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            trees.add(new Tree<>("" + i));
        }
        for (int i = 0; i < size; i++) {
            for (int j = 1; j <= 4; j++) {
                trees.get(i).add(trees.get((i + j) % size));
            }
        }

        assertThat(trees.get(0).contains("" + (size-1)), is(true));
    }

    /* helper functions */

    private <T> Tree<T> makeDeepLinearTree (T[] values) {
        Tree<T> previous = null;
        Tree<T> root = null;
        for (int i = 0; i < values.length; i++) {
            Tree<T> tree = new Tree<> (values[i]);
            if (null != previous) {
                previous.add(tree);
            } else {
                root = tree;
            }
            previous = tree;
        }
        return root;
    }
}