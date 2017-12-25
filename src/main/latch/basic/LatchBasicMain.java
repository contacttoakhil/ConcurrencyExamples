package main.latch.basic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mittaa3 on 2/18/2015.
 */
public class LatchBasicMain {

    static class Processor implements Runnable {
        CountDownLatch latch;

        public Processor(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("Started some work..");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            latch.countDown();
            System.out.println("Finished.");
        }
    }

    public static void main(String[] args) {
        int threadsCount = 5;
        CountDownLatch latch = new CountDownLatch(threadsCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        for(int i=0; i<threadsCount; i++) {
            executorService.submit(new Processor(latch));
        }

        // Main will wait for all other threads to finish.
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main Finished");
    }
}
