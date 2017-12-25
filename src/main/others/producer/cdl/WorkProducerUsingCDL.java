package others.producer.cdl;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Akhil on 31-03-2015.
 */
public class WorkProducerUsingCDL implements Runnable{
    private final AtomicReference<CountDownLatch> latchAtomicReference;

    public WorkProducerUsingCDL() {
        latchAtomicReference = new AtomicReference<>();
    }

    @Override
    public void run() {
        produceRandomWorkers();
    }

    private void produceRandomWorkers() {
        Random random = new Random();
        int numberOfWorkers = random.nextInt(20) + 1;
        System.out.println("Workers count: " + numberOfWorkers);
        for (int i=0; i<numberOfWorkers; i++){
            try {
                createWorkerTask().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Add some delay to simulate some processing
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //By now all workers have been added. Some of them may be over and some may be processing.
        latchAtomicReference.set(new CountDownLatch(numberOfWorkers));

        // Now producer will wait for main.latch to be over.
        try {
            latchAtomicReference.get().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Now all workers are definitely over.
        try {
            processAfterWorkIsOver();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Thread createWorkerTask() {
        WorkerTask workerTask = new WorkerTask(new WorkerThread(), latchAtomicReference);
        Thread thread = new Thread(workerTask);
        return  thread;
    }

    private void processAfterWorkIsOver() throws InterruptedException, ExecutionException {
        System.out.println("Work is over by all workers.");
    }

}
