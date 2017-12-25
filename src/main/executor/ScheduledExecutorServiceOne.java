package main.executor;

import main.utilities.ExecutorServiceUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduledExecutorServiceOne {
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private ScheduledFuture updateFuture;

    public static void main(String[] args) {
        ScheduledExecutorServiceOne scheduledExecutorServiceOne = new ScheduledExecutorServiceOne();
        scheduledExecutorServiceOne.startTask();
        //scheduledExecutorServiceOne.startTaskNTimes(5, 4);
    }

    public void startTask() {
        System.out.println("Starting ScheduledRunnableTask to run every 2 seconds..............");
        updateFuture = executorService.scheduleAtFixedRate(new ScheduledRunnableTask(this::stopTask), 1, 2, TimeUnit.SECONDS);
    }

    public void stopTask() {
        System.out.println("Stopping ScheduledRunnableTask to run further");
        updateFuture.cancel(false);
        ExecutorServiceUtils.shutdownAndAwaitTermination(executorService);
        ExecutorServiceUtils.taskCompletionStatus(executorService);
    }

    public void startTaskNTimes(long delay, int nTimes) {
        System.out.println("Starting FixedExecutionRunnable to run " + nTimes + " times with delay of " + delay + " seconds");
        final CountDownLatch latch = new CountDownLatch(nTimes);
        executorService.scheduleAtFixedRate(new FixedExecutionRunnable(latch), delay, delay, TimeUnit.SECONDS);
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Interrupted during n times execution" + e.getCause()); // log whatever.
        }
        ExecutorServiceUtils.shutdownAndAwaitTermination(executorService);
        ExecutorServiceUtils.taskCompletionStatus(executorService);
    }
}

/**
 * The scheduled task will be done at regular interval until a condition is met. When condition is met it will stop itself.
 */
class ScheduledRunnableTask implements Runnable {
    private final Runnable stopper;
    private final AtomicInteger runCount = new AtomicInteger(0);

    public ScheduledRunnableTask(Runnable stopper) {
        this.stopper = stopper;
    }

    @Override
    public void run() {
        try {
            int valueNow = runCount.incrementAndGet();
            if (valueNow == 5)   // condition to stop itself from further execution
            {
                stopper.run(); // we can also throw RTE e.g. throw new RuntimeException("chapter closed");
            }
            if(!Thread.currentThread().isInterrupted()) {
                System.out.println("Running with value: " + valueNow + " and going to do work");
                try {
                    Thread.sleep((long) (Math.random() * 10000)); // simulate some work
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Running with value: " + valueNow + " and work over");
            }
        } catch (Exception e) {
            System.out.println(e.getCause());  //log it because when an exception is thrown the further execution stops and we have no clue about that exception. Hence wrapping body in try-catch.
        } 
    }
}

class FixedExecutionRunnable implements Runnable {
    private final CountDownLatch countDownLatch;

    public FixedExecutionRunnable(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("Doing work for: " + countDownLatch.getCount() + " time");
        countDownLatch.countDown();
    }
}


