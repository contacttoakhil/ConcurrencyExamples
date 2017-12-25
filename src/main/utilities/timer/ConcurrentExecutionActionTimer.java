package main.utilities.timer;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;

public class ConcurrentExecutionActionTimer {
	/***
	 * This method captures the time taken by all worker threads to execute. The main.executor that is passed to the time must allow for the creation of at least as many threads as the
	 * given concurrency level or the test will never complete. This is known as thread starvation.
	 * @param executor to execute the action
	 * @param concurrency level representing the number of actions to be executed concurrently
	 * @param action runnable representing the action.
	 * @return time taken
	 * @throws InterruptedException
	 */
	public static long elapsedTimeUsingCountDownLatch(Executor executor, int concurrency, final Runnable action) throws InterruptedException
	{
		final CountDownLatch ready = new CountDownLatch(concurrency);
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch done = new CountDownLatch(concurrency);
		
		for(int i=0; i<concurrency; i++ ){
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					ready.countDown();	//Tell timer we are ready.
					
					try {
						start.await();	//Wait till peers are ready.
						action.run();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} finally {
						done.countDown();	//Tell timer we are done.
					}
				}
			});
		}
		
		ready.await();		//Wait for all workers to be ready
		long startNanoTime = System.nanoTime();
		start.countDown();		//And here they go!!
		done.await();			// Wait for all workers to finish.
		return System.nanoTime() - startNanoTime;
		
	}
	
	public static long elapsedTimeUsingCyclicBarrier(Executor executor, int concurrency, final Runnable action) throws InterruptedException, BrokenBarrierException
	{
		final Runnable barrierAction = new Runnable() {
			@Override
			public void run() {
				System.out.println("Condition of main.barrier is met.");
			}
		};
		
		final CyclicBarrier barrier = new CyclicBarrier(concurrency + 1, barrierAction);
		
		for(int i=0; i<concurrency; i++ ){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("Waiting at main.barrier.");
						barrier.await();
						action.run();
						//Cyclic main.barrier gets reset automatically. Again wait for them to finish.
						barrier.await();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					} 
				}
			});
		}
		barrier.await();
		long startNanoTime = System.nanoTime();
		barrier.await();
		return System.nanoTime() - startNanoTime;
	}
}
