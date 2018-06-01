import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  A randomized queue, the item removed is chosen uniformly at random
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue = (Item[]) new Object[1];
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        //empty fo now
    }

    // is the randomized queue empty
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null item");
        }

        if (size == queue.length) {
            resize(2 * queue.length);
        }

        queue[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        checkEmpty();

        int index = StdRandom.uniform(size);
        Item item = queue[index];
        queue[index] = queue[--size];
        queue[size] = null;

        if (size > 0 && size == queue.length / 4) {
            resize(queue.length / 2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkEmpty();

        return queue[StdRandom.uniform(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        Item[] items = copy(size, size);
        StdRandom.shuffle(items);

        return new Iterator<Item>() {
            private Item[] array = items;
            private int currIndex = 0;

            @Override
            public boolean hasNext() {
                return currIndex < array.length;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("RandomizedQueue is empty");
                }

                return array[currIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private void resize(int capacity) {
        queue = copy(size, capacity);
    }

    private Item[] copy(int size, int capacity) {
        Item[] newQueue = (Item[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[i];
        }

        return newQueue;
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("RandomizedQueue is empty");
        }
    }

    // simple test
    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();

        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        StdOut.println(q.dequeue());
        StdOut.println(q.dequeue());
        StdOut.println(q.dequeue());

        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);
        q.enqueue(8);

        for (Integer i : q) {
            StdOut.println(i);
        }
    }
}
