package main.executorCompletionService.basic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Akhil on 01-05-2015.
 */
public class TestECSBasic {
    @Test
    public void testCompleteAllTasksService() throws InterruptedException, ExecutionException {
        final List<Callable<String>> callableList = Arrays.asList(new SleepingCallable("super-slow", 40000),
                new SleepingCallable("slow", 10000),
                new SleepingCallable("fast", 2000),
                new SleepingCallable("super-fast", 20)
        );
        final ExecutorService pool = Executors.newFixedThreadPool(callableList.size());
        final CompletionService<String> service = new ExecutorCompletionService<String>(pool);
        callableList.forEach( callable -> service.submit(callable));
        pool.isShutdown();
        int n = callableList.size();
        for(int i=0; i<n; i++) {
            String result = service.take().get();
            if(result != null) {
                System.out.println(result);
            }
        }
    }
    @Test
    public void testCompleteFirstAndExitRestTasksService() throws InterruptedException {
        final List<Callable<String>> callableList = Arrays.asList(new SleepingCallable("super-slow", 40000),
                new SleepingCallable("slow", 10000),
                new SleepingCallable("fast", 2000),
                new SleepingCallable("super-fast", 20)
        );
        int numberOfCallable = callableList.size();
        final ExecutorService pool = Executors.newFixedThreadPool(numberOfCallable);
        final CompletionService<String> service = new ExecutorCompletionService<String>(pool);
        List<Future<String>> futures = new ArrayList<>();
        String result = null;
        try {
            for(Callable<String> callable : callableList) {
                futures.add(service.submit(callable));
            }
            for(int i=0; i<numberOfCallable; i++) {
                try {
                    result = service.take().get();
                    if(result!=null) break;
                } catch (ExecutionException ignore) {}
            }
        } finally {
            for (Future<String> future : futures) {
                future.cancel(true);
            }
        }
        System.out.println(result);
    }
}
