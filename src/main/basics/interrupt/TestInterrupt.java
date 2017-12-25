package main.basics.interrupt;

import org.junit.Test;

/**
 * Created by Akhil on 01-05-2015.
 */
public class TestInterrupt {
    @Test
    public void testInterruptibleJob() throws InterruptedException {
        InterruptibleJob job = new InterruptibleJob();
        Thread thread = new Thread(job);
        thread.start();

        // Let job run for some time.
        Thread.sleep(2000);

        //Terminate the job
        job.setTerminated(true);

        // Wait for thread to terminate.
        thread.join();
    }

    @Test
    public void testInterruptedSleepingThread() throws InterruptedException {
        InterruptedSleepingThread thread = new InterruptedSleepingThread();
        thread.start();
        //Giving 10 seconds to finish the job.
        Thread.sleep(10000);
        //Let me interrupt
        thread.interrupt();
    }
}
