package others.producer.phaser;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * Created by Akhil on 31-03-2015.
 */
public class WorkProducerUsingPhaser implements Runnable{
    private final Phaser phaser;
    private final ExecutorService executorService;

    public WorkProducerUsingPhaser() {
        phaser = new Phaser() {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("On advance" + " -> Registered: " + getRegisteredParties() + " - Unarrived: "
                        + getUnarrivedParties() + " - Arrived: " + getArrivedParties() + " - Phase: " + getPhase());
                return (phase >= 1) || (registeredParties == 0);
            }
        };
        executorService = Executors.newFixedThreadPool(5);
    }

    @Override
    public void run() {
        produceRandomWorkers();
    }

    private void produceRandomWorkers() {
        do {
            phaser.register();

            Random random = new Random();
            int numberOfWorkers = random.nextInt(20) + 1;
            System.out.println("Workers count: " + numberOfWorkers);

            for (int i=0; i<numberOfWorkers; i++){
                phaser.register();
                executorService.submit(getWorker());
            }

            phaser.arriveAndAwaitAdvance();

            // Now all workers are definitely over.
            try {
                processAfterWorkIsOver();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } while(!phaser.isTerminated());

        phaser.arriveAndDeregister();
        System.out.println("Phaser:  registered parties: " + phaser.getRegisteredParties());

    }

    private Thread getWorker() {
        WorkerThread workerTask = new WorkerThread(phaser);
        Thread thread = new Thread(workerTask);
        return  thread;
    }

    private void processAfterWorkIsOver() throws InterruptedException, ExecutionException {
        System.out.println("Work is over by all workers.");
    }
}
