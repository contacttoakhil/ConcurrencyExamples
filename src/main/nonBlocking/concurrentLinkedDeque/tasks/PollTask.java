package main.nonBlocking.concurrentLinkedDeque.tasks;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by mittaa3 on 2/18/2015.
 */
public class PollTask implements Runnable {
    private ConcurrentLinkedDeque list;

    public PollTask(ConcurrentLinkedDeque list) {
        this.list = list;
    }

    @Override
    public void run() {
        for ( int i=0; i<5_000; i++ ){
            list.pollFirst();
            list.pollLast();
        }
    }
}
