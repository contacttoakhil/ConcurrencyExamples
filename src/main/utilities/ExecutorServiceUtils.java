package main.utilities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceUtils {
    /**
     * shutdows the executor service correctly. Reference: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html
     * @param pool
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) { // The method awaitTermination returns true if all tasks are done successfully.
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    public static void taskCompletionStatus(ExecutorService pool) {
        ThreadPoolExecutor threadPoolExecutor = ((ThreadPoolExecutor)pool);
        long submitted = threadPoolExecutor.getTaskCount();
        long completed = threadPoolExecutor.getCompletedTaskCount();
        long notCompleted = submitted - completed;
        System.out.println("Submitted: " + submitted + " completed: " + completed + " and not yet completed: " + notCompleted);

        //int queued = threadPoolExecutor.getQueue().size();
        //int active = threadPoolExecutor.getActiveCount();
        //int notCompleted2 = queued + active; // approximate
        //System.out.println("queued: " + queued + ": " + completed+ ":" + notCompleted2);
    }
}
