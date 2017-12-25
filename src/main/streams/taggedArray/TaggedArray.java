package main.streams.taggedArray;

import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

/**
 * Here is a class that maintains an array in which the actual data are held in even locations, and unrelated tag data are held in odd locations. Its Spliterator ignores the tags.
 * A Spliterator is a way of operating over the elements of a collection in a way that it's easy to split off part of the collection, e.g. because you're parallelling and want one thread
 * to work on one part of the collection, another thread to work on another part, etc.
 */
public class TaggedArray {
    private final Object[] elements;

    public TaggedArray(Object[] data, Object[] tags) {
        int size = data.length;
        if(tags.length != data.length) throw new IllegalArgumentException("Data and Tags length should match");
        this.elements = new Object[2 * size];
        for(int i=0, j=0; i<size; i++) {
            elements[j++] = data[i];
            elements[j++] = tags[i];
        }
    }

    public Spliterator spliterator() {
        return new TaggedArraySpliterator(elements, 0, elements.length);
    }

    static class TaggedArraySpliterator implements Spliterator<Object> {
        private final Object[] elements;
        private int origin; // current index advance on split or traversal
        private final int fence;  // one past the greatest index (boundary).

        public TaggedArraySpliterator(Object[] elements, int origin, int fence) {
            this.elements = elements;
            this.origin = origin;
            this.fence = fence;
        }

        public void forEach(Consumer<Object> action) {
            for(; origin<fence; origin += 2){
                action.accept(elements[origin]);
            }
        }

        @Override
        public boolean tryAdvance(Consumer<? super Object> action) {
            if(origin < fence) {
                action.accept(elements[origin]);
                origin += 2;
                return true;
            }
            else return false; //cannot advance
        }

        @Override
        public Spliterator<Object> trySplit() {
            int low = origin;
            int mid = ((low + fence) >>> 1) & ~1; // force midpoint to be even
            if(low < mid) {
                origin = mid;
                return new TaggedArraySpliterator(elements, low, mid);
            }
            else return null;
        }

        @Override
        public long estimateSize() {
            return (long)((fence - origin)/2);
        }

        @Override
        public int characteristics() {
            return ORDERED | SIZED | IMMUTABLE | SUBSIZED;
        }
    }

    // parallel equivalent of forEach.
    public static void parEach(TaggedArray array, Consumer<Object> action) {
        TaggedArraySpliterator spliterator = (TaggedArraySpliterator) array.spliterator();
        long targetBatchSize = spliterator.estimateSize() / (ForkJoinPool.getCommonPoolParallelism() * 8);
        new ParEach(null, spliterator, action, targetBatchSize).invoke();
    }

    static class ParEach extends CountedCompleter<Void> {
        final TaggedArraySpliterator spliterator;
        final Consumer<Object> action;
        final long targetBatchSize;

        ParEach(ParEach parent, TaggedArraySpliterator spliterator, Consumer<Object> action, long targetBatchSize) {
            super(parent);
            this.spliterator = spliterator; this.action = action;
            this.targetBatchSize = targetBatchSize;
        }
        public void compute() {
            TaggedArraySpliterator sub;
            while (spliterator.estimateSize() > targetBatchSize && (sub = (TaggedArraySpliterator) spliterator.trySplit()) != null) {
                addToPendingCount(1);
                new ParEach(this, sub, action, targetBatchSize).fork();
            }
            spliterator.forEach(action);
            propagateCompletion();
        }
    }
}
