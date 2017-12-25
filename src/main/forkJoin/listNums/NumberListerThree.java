package main.forkJoin.listNums;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * This time parent waits for children (tasks) to complete. But problem here is that task is waiting for sub-tasks to complete and rather than waiting it should be stealing work from others.
 */
public class NumberListerThree {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ConcurrentLinkedQueue<String> collector = new ConcurrentLinkedQueue<>();
        RecursiveNumberLister rnl = new RecursiveNumberLister(collector, 20, 150);

        ForkJoinPool.commonPool().invoke(rnl);

        ArrayList<String> list = new ArrayList<>(collector);
        //Collections.sort(list);

        System.out.printf("Listed %d items%n", list.size());
        for (String s : list) {
            System.out.println("  " + s);
        }
    }

    static class RecursiveNumberLister extends CountedCompleter<Void> {
        final ConcurrentLinkedQueue<String> collector;
        final int start;
        final int end;

        public RecursiveNumberLister(ConcurrentLinkedQueue<String> collector, int start, int end) {
            this(collector, start, end, null);
        }

        public RecursiveNumberLister(ConcurrentLinkedQueue<String> collector, int start, int end, RecursiveNumberLister parent) {
            //Completions will bubble up fom sub-tasks because of this link from parent to child.
            super(parent);
            this.collector = collector;
            this.start = start;
            this.end = end;
        }

        @Override
        public void compute() {
            if( end- start < 5 ) {
                String threadName = Thread.currentThread().getName();
                for(int i=start; i<end; i++) {
                    collector.add(String.format("%5d_%s",i,threadName));
                }
                //Signal that this is now complete. The completions will bubble up automatically.
                propagateCompletion();
            }
            else
            {
                int middle = (start + end)/2;
                RecursiveNumberLister left = new RecursiveNumberLister(collector, start, middle,this);
                RecursiveNumberLister right = new RecursiveNumberLister(collector, middle, end, this);

                //Only the left sub-task is forked, so set the pending count to 1.
                setPendingCount(1);

                left.fork();
                right.compute();
            }
        }
    }
}
