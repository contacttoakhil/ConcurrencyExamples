package main.forkJoin.listNums;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * This time parent waits for children (tasks) to complete. But problem here is that task is waiting for sub-tasks to complete and rather than waiting it should be stealing work from others.
 */
public class NumberListerTwo {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentLinkedQueue<String> collector = new ConcurrentLinkedQueue<String>();
        RecursiveNumberLister lister = new RecursiveNumberLister(collector, 20, 150);

        //Sub-tasks are just fired off asynchronously and tasks are not programmed to wait until the sub-tasks finish. So, the root of the tree will exit before the entire tree of tasks completes.
        ForkJoinPool.commonPool().invoke(lister);

        ArrayList<String> list = new ArrayList<>(collector);
        //Collections.sort(list);

        System.out.printf("Listed %d items%n", list.size());
        for (String s : list) {
            System.out.println("  " + s);
        }
    }

    static class RecursiveNumberLister extends RecursiveAction {
        final ConcurrentLinkedQueue<String> collector;
        final int start;
        final int end;

        public RecursiveNumberLister(ConcurrentLinkedQueue<String> collector, int start, int end) {
            System.out.println("Thread: " + Thread.currentThread().getName() + "");

            this.collector = collector;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if( end- start < 5 ) {
                String threadName = Thread.currentThread().getName();
                for(int i=start; i<end; i++) {
                    collector.add(String.format("%5d_%s",i,threadName));
                }
            }
            else
            {
                int middle = (start + end)/2;
                RecursiveNumberLister left = new RecursiveNumberLister(collector, start, middle);
                RecursiveNumberLister right = new RecursiveNumberLister(collector, middle, end);

                left.fork();
                left.join();

                // We can fork and join right part or we can compute right synchronously.
                right.fork();
                right.join();
                //right.compute();
            }
        }
    }
}
