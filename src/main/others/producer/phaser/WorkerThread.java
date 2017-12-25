package others.producer.phaser;

import java.util.concurrent.Phaser;

/**
 * Created by Akhil on 31-03-2015.
 */
public class WorkerThread implements Runnable {
    private final Phaser phaser;

    public WorkerThread(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Work is over for: " + Thread.currentThread().getName());
            phaser.arriveAndDeregister();
        } while(!phaser.isTerminated());
    }
}
