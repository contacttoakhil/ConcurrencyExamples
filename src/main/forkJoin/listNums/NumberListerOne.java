package main.forkJoin.listNums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * This approach keeps forking sub-tasks till the leaf levels. So thread that created the root level task attempts to wait for the whole tree of computations to complete. Since each level
 * spawns the next level of 2 sub-tasks asynchronously and does not wait for the children to complete, the whole tree completes asynchronously. This way the caller thread in the "main()"
 * method comes out of "invoke()" prematurely. As a result this recursive task is almost what we wanted but not entirely.
 */
public class NumberListerOne {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentLinkedQueue<String> collector = new ConcurrentLinkedQueue<String>();
        RecursiveNumberLister lister = new RecursiveNumberLister(collector, 20, 150);

        //Sub-tasks are just fired off asynchronously and tasks are not programmed to wait until the sub-tasks finish. So, the root of the tree will exit before the entire tree of tasks completes.
        ForkJoinPool.commonPool().invoke(lister);

        //This is why we have to resort to a crude mechanism - thread sleep.
        System.out.println("Going to sleep and hoping that children (tasks) would complete in 5s. May God help us!!");
        Thread.sleep(5_000);
        System.out.println("Thread is now awake");

        ArrayList<String> list = new ArrayList<>(collector);
        Collections.sort(list);

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
                //Spawn both of them synchronously
                left.fork();
                right.fork();
            }
        }
    }
}
