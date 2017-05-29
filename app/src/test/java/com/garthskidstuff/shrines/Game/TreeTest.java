package com.garthskidstuff.shrines.Game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by garthupshaw1 on 5/28/17.
 * Test tree
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
    public void getDescendent_self() {
        String s = "String";
        Tree<String> tree = new Tree<>(s);

        assertThat(tree.getDescendent(s), is(tree));
    }

    @Test
    public void getDescendent_true() {
        String s1 = "1";
        String s2 = "2";
        Tree<String> tree1 = new Tree<>(s1);
        Tree<String> tree2 = new Tree<>(s2);

        tree1.add(tree2);

        assertThat(tree1.getDescendent(s2), is(tree2));
    }

    @Test
    public void getDescendent_false() {
        String s1 = "1";
        String s2 = "2";
        Tree<String> tree1 = new Tree<>(s1);
        Tree<String> tree2 = new Tree<>(s2);

        tree1.add(tree2);

        assertThat((null == tree2.getDescendent(s1)), is(true));
    }

    @Test
    public void getDescendent_loopOneTwo() {
        String s1 = "1";
        String s2 = "2";
        Tree<String> tree1 = new Tree<>(s1);
        Tree<String> tree2 = new Tree<>(s2);

        tree1.add(tree2);
        tree2.add(tree1);

        assertThat((tree1.getDescendent(s2) == tree2), is(true));
    }

    @Test
    public void contains_loopOneTwoThree() {
        Tree<String> tree = makeDeepLinearStringTree("", 3);

        assertThat((null != tree.getDescendent("2")), is(true));
    }

    @Test
    public void contains_deepSimple() {
        String sLeft= "Left";
        String sRight= "Right";
        final int size = 50;

        Tree<String> treeLeft = makeDeepLinearStringTree(sLeft, size);
        Tree<String> treeRight = makeDeepLinearStringTree(sRight, size);

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
            root.add(new Tree<>("" + i));
        }
        assertThat(root.contains("0"), is(true));
        assertThat(root.contains("" + (size-1)), is(true));
    }

    @Test
    public void contains_deepConnected() {
        final int size = 50;

        Tree<String> tree = makeAwfulStringTree(size);

        for (int i = 0; i < size; i++) {
            assertThat(tree.contains("" + i), is(true));
        }
    }

    @Test
    public void equals_selfNoChildren() {
        Tree<String> tree = new Tree<>("0");
        //noinspection EqualsWithItself
        assertThat(tree.equals(tree), is(true));
    }

    @Test
    public void equals_sameHereNoChildren() {
        Tree<String> tree1 = new Tree<>("tree");
        Tree<String> tree2 = new Tree<>("tree");
        //noinspection EqualsWithItself
        assertThat(tree1.equals(tree2), is(true));
    }

    @Test
    public void equals_false() {
        Tree<String> tree1 = new Tree<>("1");
        Tree<String> tree2 = new Tree<>("2");
        //noinspection EqualsWithItself
        assertThat(tree1.equals(tree2), is(false));
    }

    @Test
    public void equals_compareTwoDeepTrees() {
        Tree<String> tree1 = makeDeepLinearStringTree("", 50);
        Tree<String> tree2 = makeDeepLinearStringTree("", 50);

        assertThat(tree1.equals(tree2), is(true));
    }

    @Test
    public void equals_compareTwoUnequalDeepTrees() {
        Tree<String> tree1 = makeDeepLinearStringTree("", 50);
        Tree<String> tree2 = makeDeepLinearStringTree("", 49);

        assertThat(tree1.equals(tree2), is(false));
    }

    @Test
    public void equals_compareLoopOneTwo() {
        Tree<String> tree1 = new Tree<>("0");
        Tree<String> tree2 = new Tree<>("1");

        tree1.add(tree2);
        tree2.add(tree1);

        Tree<String> otherTree1 = new Tree<>("0");
        Tree<String> otherTree2 = new Tree<>("1");

        otherTree1.add(otherTree2);
        otherTree2.add(otherTree1);

        assertThat(tree1, is(otherTree1));
    }

    @Test
    public void equals_compareLoopOneTwoFalse() {
        Tree<String> tree1 = new Tree<>("0");
        Tree<String> tree2 = new Tree<>("1");

        tree1.add(tree2);
        tree2.add(tree1);

        Tree<String> otherTree1 = new Tree<>("0");
        Tree<String> otherTree2 = new Tree<>("NOT-1");

        otherTree1.add(otherTree2);
        otherTree2.add(otherTree1);

        assertNotEquals(tree1, otherTree1);
    }

    @Test
    public void equals_compareTwoAwfulTrees() {
        Tree<String> tree1 = makeAwfulStringTree(5);
        Tree<String> tree2 = makeAwfulStringTree(5);

        assertThat(tree1.toString(), is(tree2.toString()));
        assertThat(tree1, is(tree2));
    }

    @Test
    public void equals_symanticallyEqual() {
        Tree<String> tree1 = new Tree<>("0");
        Tree<String> tree2 = new Tree<>("1");

        tree1.add(tree2);
        tree2.add(tree1);

        Tree<String> otherTree1 = new Tree<>("0");
        Tree<String> otherTree2 = new Tree<>("1");
        Tree<String> otherTree3 = new Tree<>("0"); // same name but different node

        otherTree1.add(otherTree2);
        otherTree2.add(otherTree3);
        otherTree3.add(otherTree1);

        assertThat(tree1, is(otherTree1));
    }

    @Test
    public void toString_oneNode() {
        String name = "someName";
        Tree<String> tree = new Tree<>(name);

        assertThat(tree.toString(), is(name + ": \n"));
    }

    @Test
    public void toString_loopOneTwo() {
        Tree<String> tree1 = new Tree<>("0");
        Tree<String> tree2 = new Tree<>("1");

        tree1.add(tree2);
        tree2.add(tree1);

        assertThat(tree1.toString(), is("0: 1 \n 1: 0 \n  0: *\n"));
    }

    @Test
    public void toString_threeNodes() {
        Tree<String> tree = makeDeepLinearStringTree("", 3);

        assertThat(tree.toString(), is("0: 1 \n 1: 2 \n  2: \n"));
    }

    /* helper functions */

    private Tree<String> makeDeepLinearStringTree(String sPrefix, int size) {
        Tree<String> previous = null;
        Tree<String> root = null;
        for (int i = 0; i < size; i++) {
            Tree<String> tree = new Tree<>(sPrefix + i);
            if (null != previous) {
                previous.add(tree);
            } else {
                root = tree;
            }
            previous = tree;
        }
        return root;
    }

    private Tree<String> makeAwfulStringTree(int size) {
        List<Tree<String>> trees = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            trees.add(new Tree<>("" + i));
        }
        for (int i = 0; i < size; i++) {
            for (int j = 1; j <= 4; j++) {
                trees.get(i).add(trees.get((i + j) % size));
            }
        }
        return trees.get(0);
    }

}