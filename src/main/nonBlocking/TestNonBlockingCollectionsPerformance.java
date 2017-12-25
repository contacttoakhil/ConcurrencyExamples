package main.nonBlocking;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Akhil on 01-05-2015.
 */
public class TestNonBlockingCollectionsPerformance {
    private final static int THREAD_POOL_SIZE = 5;

    @Test
    public void testHashTable() throws InterruptedException {
        final Map<String,Integer> hashTable = new Hashtable<>();
        performTest(hashTable);
    }

    @Test
    public void testHashMap() throws InterruptedException {
        final Map<String,Integer> hashMap =  Collections.synchronizedMap(new HashMap<String, Integer>());
        performTest(hashMap);
    }

    @Test
    public void testConcurrentHashMap() throws InterruptedException {
        final Map<String,Integer> hashMap =  new ConcurrentHashMap<>();
        performTest(hashMap);
    }

    void performTest(final Map<String,Integer> map) throws InterruptedException {
        System.out.println("Test started for: " + map.getClass());
        long averageTime = 0;
        for (int i = 0; i < 5; i++) {

            long startTime = System.nanoTime();
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            for (int j = 0; j < THREAD_POOL_SIZE; j++) {
                executorService.execute(new Runnable() {
                    @SuppressWarnings("unused")
                    @Override
                    public void run() {

                        for (int i = 0; i < 500000; i++) {
                            Integer randomNumber = (int) Math.ceil(Math.random() * 550000);

                            // Retrieve value. We are not using it anywhere
                            Integer value = map.get(String.valueOf(randomNumber));

                            // Put value
                            map.put(String.valueOf(randomNumber), randomNumber);
                        }
                    }
                });
            }

            // Make sure main.executor stops
            executorService.shutdown();

            // Blocks until all tasks have completed execution after a shutdown request
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

            long entTime = System.nanoTime();
            long totalTime = (entTime - startTime) / 1000000L;
            averageTime += totalTime;
            System.out.println("500K entried added/retrieved in " + totalTime + " ms");
        }
        System.out.println("For " + map.getClass() + " the average time is " + averageTime / 5 + " ms\n");
    }
}
