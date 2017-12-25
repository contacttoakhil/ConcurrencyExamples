package main.barrier.basic;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierExample {
    public static void main(String[] args) {
        int threadCount = 5;
        CyclicBarrier barrier = new CyclicBarrier(threadCount,() -> {
            System.out.println("Barrier is met.");
        });
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);//Executor is replacement for common thread idiom: (new Thread(r)).start() to e.execute(r)

        for (int i=0; i<threadCount; i++) {
            executorService.execute(() ->  {
                try {
                    System.out.println("Waiting at main.barrier");
                    barrier.await();
                    System.out.println("Working Now..");
                    Thread.sleep(5000);
                    System.out.println("Work is over..");
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }
}
