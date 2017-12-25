package main.nonBlocking.concurrentLinkedDeque.tasks;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by mittaa3 on 2/18/2015.
 */
public class AddTask implements Runnable{
    private ConcurrentLinkedDeque<String> list;

    public AddTask(ConcurrentLinkedDeque<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        for (int i=0; i<10_000; i++) {
            list.add(name + ": Element " + i);
        }
    }
}
