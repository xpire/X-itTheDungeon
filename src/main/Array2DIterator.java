package main;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class Array2DIterator<T> implements Iterator<T> {

    private T[][] items;
    private Iterator<T[]> rowIterator;
    private Iterator<T> itemIterator;

    public Array2DIterator (T[][] items) {
        this.items = items;
        rowIterator = Arrays.stream(items).iterator();
    }

    @Override
    public boolean hasNext() {
        if (itemIterator != null && itemIterator.hasNext()) return true;

        if (rowIterator.hasNext()) {
            itemIterator = nextItemIterator();
            return hasNext();
        }

        return false;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        return itemIterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {

        // use up the current iterator
        if (itemIterator != null)
            forEachRemainingIgnoreNull(action);

        // use up the remaining
        while (rowIterator.hasNext()) {
            itemIterator = nextItemIterator();
            forEachRemainingIgnoreNull(action);
        }
    }

    private void forEachRemainingIgnoreNull(Consumer<? super T> action) {
        while (hasNext()) {
            T obj = next();
            if (obj != null)
                action.accept(obj);
        }
    }

    private Iterator<T> nextItemIterator() {
        return itemIterator = Arrays.stream(rowIterator.next()).iterator();
    }
}
