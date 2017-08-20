package com.garthskidstuff.shrines.Game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Shuffled is an iterator (with remove) that accesses the elements of the passed in list in a
 * random order.
 * "Shuffled" is a bit of misnomer because it only actually "shuffles" on demand.
 * (By choosing a random remaining element.)
 *
 * @param <T> The type of the item returned by next/contained in oldList.
 */
class Shuffled<T> implements Iterator {
    final List<T> old;
    final List<Integer> innerList = Utils.makeList();
    private final Roller roller;
    private int now;

    /**
     * This just prepares a list of indexes, and keeps an reference to the passed in list.
     *
     * @param old the Collection we want to get random elements out of.
     */
    public Shuffled(Roller roller, List<T> old) {
        this.roller = roller;
        this.old = old;
        int ix = 0;
        for (@SuppressWarnings("unused") T elm : old) {
            innerList.add(ix++);
        }

    }

    /**
     * This creates a shuffled iterator, but note that original set is not affected by remove.
     *
     * @param roller
     * @param oldSet
     * @param <T>
     * @return
     */
    public static <T> Shuffled<T> fromSet(Roller roller, Set<T> oldSet) {
        List<T> old = new ArrayList<>();
        for (T elem : oldSet) {
            old.add(elem);
        }
        return new Shuffled<>(roller, old);
    }

    /**
     * As long as the innerList isn't empty then we can run Next.
     * (_Which_ item is next is decided in next().)
     *
     * @return can we run next safely.
     */
    @Override
    public boolean hasNext() {
        return !innerList.isEmpty();
    }

    /**
     * Randomly fetches a remaining item.
     *
     * @return the randomly selected element.
     */
    @Override
    public T next() {
        now = roller.roll(0, innerList.size() - 1);
        innerList.remove(now);
        return old.get(now);
    }

    /**
     * Not yet used. This removes the item at now (i.e the item returned by the most recent next)
     * from the original list.
     */
    @Override
    public void remove() {
        for (int ix = 0; ix < innerList.size(); ix++) {
            int item = innerList.get(ix);
            if (item > now) {
                innerList.set(ix, 1 + item);
            }
        }
        old.remove(now);
    }
}
