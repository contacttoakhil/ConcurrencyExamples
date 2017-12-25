package main.locks.reentrant.basic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mittaa3 on 2/18/2015.
 */
public class Incrementer {
    int count = 0;
    Lock lock = new ReentrantLock(); // Non-fair lock.

    public void incrementByFirstThread() {
        lock.lock();
        try {
            increment();
        } finally {
            lock.unlock();  // In any case lock must be released.
        }
    }

    public void incrementBySecondThread() {
        lock.lock();
        try{
            increment();
        } finally {
            lock.unlock();
        }
    }

    private void increment() {
        for(int i=0; i<1_000; i++) {
            count++;
        }
    }

    public void finished() {
        System.out.println("Finished with value: " + count);
    }
}
