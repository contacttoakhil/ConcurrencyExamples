package main.executor;

import main.utilities.ExecutorServiceUtils;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

public class ExecutorServiceOne {
    @Test
    public void testExecutorBasic() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Runnable runnable = new RunnableTask();
        Future<?> future = executorService.submit(runnable);

        Callable<Integer> callable = new CallableTask();
        Future<Integer> futureC = executorService.submit(callable);

        ExecutorServiceUtils.shutdownAndAwaitTermination(executorService);

        try {
            System.out.println("Result: " + futureC.get()); //
        } catch (ExecutionException e) {
            Throwable rootCause = e.getCause(); // The ExecutionException wraps the actual exception thrown by the job (thread doing the work Runnable or Callable).
            System.out.println("Execution exception and cause is: " + rootCause); //log it.
        } catch (InterruptedException e) {
            //An InterruptedException is not a sign of anything having gone wrong. It is there to give you a way to let your threads know when it's time to stop so that they can finish up their current work and exit gracefully.
            Throwable rootCause = e.getCause();
            System.out.println("Interrupted exception and cause is: " + rootCause); //log it.
        }

    }
}


class CallableTask implements Callable<Integer> {
    @Override
    public Integer call() {
        int duration = (int) (Math.random()*10000);
        if (duration > 2000) {
            throw new RuntimeException("Sleeping for too long");
        }
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // set interrupt flag, why? check - https://stackoverflow.com/questions/3976344/handling-interruptedexception-in-java
            System.out.println("Failed to compute the work");   // log some message
        }
        System.out.println("Finished with Callable");
        return duration;
    }
}

class RunnableTask implements Runnable {
    @Override
    public void run() {
        Random random = new Random();
        int duration = random.nextInt(4000);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished with Runnable");
    }
}
