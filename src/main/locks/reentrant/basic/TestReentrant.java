package main.locks.reentrant.basic;

import org.junit.Test;

/**
 * Created by Akhil on 01-05-2015.
 */
public class TestReentrant {

    @Test
    public void testIncrementer() {
        Incrementer incrementer = new Incrementer();

        Thread thread1 = new Thread(() -> {
            incrementer.incrementByFirstThread();
        });

        Thread thread2 = new Thread(() -> {
            incrementer.incrementBySecondThread();
        });

        thread1.start(); thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        incrementer.finished();
    }

    @Test
    public void testIncrementerWithCondition() {
        IncrementerWithCondition incrementer = new IncrementerWithCondition();

        Thread thread1 = new Thread(() -> {
            try {
                incrementer.incrementByFirstThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            incrementer.incrementBySecondThread();
        });

        thread1.start(); thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        incrementer.finished();
    }

}
