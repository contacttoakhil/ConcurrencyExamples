/*
	When we have concurrent tasks divided into steps, we can use Phaser. The Phaser class also provides us the mechanism to syncrhonize
	the threads at the end of each step. A Phaser is mix oc CyclicBarrier and CountDownLatch
	
	CountdownLatch: Fixed number of parties, Not reusable, Advanceable (main.latch.countDown() and must wait at main.latch.await())
	CyclicBarrier: Fixed number of parties, Reusable, Not advanceable
	Phaser: Dynamic number of parties, Reusable, Advanceable   	
	
	In this program we want to find all log files (files with extension .log) modified in last 24 hours in three different folders and
	sub-folders.
*/
package phaser.fileSearch;

import java.util.concurrent.Phaser;

public class Main {

	public static void main(String[] args) {
		
		final Phaser phaser=new Phaser(3); 	// Three parties are registered. Another way is to call register method itself.
		
		// Search at the following locations.
		FileSearchTask system = new FileSearchTask("C:\\Windows", "log",phaser);
		FileSearchTask apps = new FileSearchTask("C:\\Program Files","log",phaser);
		FileSearchTask documents = new FileSearchTask("C:\\Documents And Settings","log",phaser);
		
		Thread systemThread=new Thread(system,"System");
		systemThread.start();
		Thread appsThread=new Thread(apps,"Apps");
		appsThread.start();
		Thread documentsThread=new Thread(documents, "Documents");
		documentsThread.start();
		
		// Wait for the threads to be over.
		try {
			systemThread.join();
			appsThread.join();
			documentsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Terminated: "+ phaser.isTerminated());
	}
}
