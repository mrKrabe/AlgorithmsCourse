import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

/**
 *  A double-ended queue
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = null;
    private int size = 0;

    private class Node {
        Node next;
        Node prev;
        Item item;
    }

    // construct an empty deque
    public Deque() {
        //empty for now
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size <= 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkForNull(item);

        Node node = new Node();
        node.item = item;

        if (first == null) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first.prev = node;
            first = node;
        }

        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        checkForNull(item);

        Node node = new Node();
        node.item = item;

        if (last == null) {
            first = node;
            last = node;
        } else {
            node.prev = last;
            last.next = node;
            last = node;
        }

        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkEmpty();

        Node temp = first;
        first = first.next;

        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }

        size--;

        return temp.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        checkEmpty();

        Node temp = last;
        last = last.prev;

        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }

        size--;

        return temp.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {

            private Node currentIndex = first;

            @Override
            public boolean hasNext() {
                return currentIndex != null;
            }

            @Override
            public Item next() {
                checkEmpty();

                Item item = currentIndex.item;

                currentIndex = currentIndex.next;

                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private void checkForNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Dequeue is empty");
        }
    }

    // simple test
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<>();

        d.addFirst(1);
        d.removeLast();

        d.addLast(2);
        d.removeFirst();

        d.addFirst(3);
        d.addFirst(2);
        d.addFirst(1);
        d.addLast(4);
        d.addLast(5);

        for (Integer i : d) {
            StdOut.println(i);
        }
    }
}
