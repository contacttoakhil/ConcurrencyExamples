package others.producer.cdl;

/**
 * Created by Akhil on 31-03-2015.
 */
public class WorkerThread implements Runnable {

    @Override
    public void run() {
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Work is over.");
    }
}
