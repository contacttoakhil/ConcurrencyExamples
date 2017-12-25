package main.locks.reentrant.basic;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IncrementerWithCondition {
    int count = 0;
    Lock lock = new ReentrantLock(); // Non-fair lock.
    Condition condition = lock.newCondition();

    public void incrementByFirstThread() throws InterruptedException {
        lock.lock();
        System.out.println("Waiting for condition in incrementByFirstThread....");
        condition.await();

        try{
            increment();
        } finally {
            lock.unlock();
        }

        lock.unlock();
    }

    public void incrementBySecondThread() {
        lock.lock();
        System.out.println("Waiting for return key ....");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        scanner.close();
        System.out.println("Got return key, signalling condition now..");
        condition.signal();

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
