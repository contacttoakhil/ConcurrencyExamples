package others.accumulation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AccumulatorTwo {

	private static final String[] NAMES = { "A", "B" };
	private static final int NB_THREADS = 1000;
	private final Map<String, AtomicInteger> countsMap = new ConcurrentHashMap<>();

	public void testIt() {
		ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
		for (int i = 0; i < NB_THREADS; i++) {
			Runnable task = new WorkerThread();
			executor.submit(task);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		System.out.println(countsMap);
	}

	private void accumulate(String name) {
		countsMap.computeIfAbsent(name, k -> new AtomicInteger()).incrementAndGet();
	}

	private class WorkerThread implements Runnable {
		@Override
		public void run() {
			accumulate(NAMES[ThreadLocalRandom.current().nextInt(0, NAMES.length)]);
		}
	}
}
