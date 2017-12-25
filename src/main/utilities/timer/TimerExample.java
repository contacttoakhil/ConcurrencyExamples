package main.utilities.timer;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Worker implements Runnable {
	@Override
	public void run() {
		System.out.println("Doing work.");
		for(int i=0; i<20; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Finished.");
	}
}

public class TimerExample {
	public static void main(String[] args) {
		//Executor is replacement for common thread idiom: (new Thread(r)).start() to e.execute(r)
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Worker action = new Worker();
		int concurrency = 5;
		try {
			//long elapsedTime = ConcurrentExecutionActionTimer.elapsedTimeUsingCountDownLatch(main.executor, concurrency, action);
			long elapsedTime = ConcurrentExecutionActionTimer.elapsedTimeUsingCyclicBarrier(executor, concurrency, action);
			double seconds = (double)elapsedTime / 1000000000.0;
			System.out.println("Time Taken approximately: " + seconds + "seconds.");
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}
