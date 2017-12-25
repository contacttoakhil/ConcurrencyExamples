package others.accumulation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccumulatorOne {
	
	private static final String[] NAMES = { "A", "B" };
	private static final int NB_THREADS = 1_000;
	private final Map<String, Integer> countsMap = new HashMap<>();
	private static final Lock readWriteLock = new ReentrantLock(true);

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
		readWriteLock.lock();
		try {
			Integer cnt = countsMap.get(name);
			if (cnt == null) {
				countsMap.put(name, 1);
			} else {
				countsMap.put(name, cnt + 1);
			}
		} finally {
			readWriteLock.unlock();
		}
	}

	private class WorkerThread implements Runnable {
		@Override
		public void run() {
			accumulate(NAMES[ThreadLocalRandom.current().nextInt(0, NAMES.length)]);
		}
	}
}
