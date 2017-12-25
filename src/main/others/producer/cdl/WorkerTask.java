package others.producer.cdl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Akhil on 31-03-2015.
 */
public class WorkerTask implements Runnable {
    private final Runnable runnable;
    private AtomicReference<CountDownLatch> latchAtomicReference;

    public WorkerTask(Runnable runnable, AtomicReference<CountDownLatch> latchAtomicReference) {
        this.runnable = runnable;
        this.latchAtomicReference = latchAtomicReference;
    }

    @Override
    public void run(){
        runnable.run();
        while (latchAtomicReference.get() == null) {
            try {
                Thread.currentThread().sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        latchAtomicReference.get().countDown();
    }
}
