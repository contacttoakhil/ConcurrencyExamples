package others.producer.ecs;

/**
 * Created by Akhil on 31-03-2015.
 */
public class WorkerThread implements Runnable {
    @Override
    public void run() {
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}