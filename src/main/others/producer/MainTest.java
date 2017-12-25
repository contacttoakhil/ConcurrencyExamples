package others.producer;

import org.junit.Test;
import others.producer.cdl.WorkProducerUsingCDL;
import others.producer.ecs.WorkProducerUsingECS;
import others.producer.phaser.WorkProducerUsingPhaser;
import java.util.concurrent.ExecutionException;

/**
 * Created by Akhil on 31-03-2015.
 */
public class MainTest {
    @Test
    public void workProducerUsingECS() throws InterruptedException{
        WorkProducerUsingECS producer =  new WorkProducerUsingECS();
        Thread thread = new Thread(producer);
        thread.start();

        thread.join();

        // We had to notify producer explicitly.
        System.out.println("Back");
        try {
            producer.getResultAfterWorkIsOver();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // How can producer be notified when all workers are done?
    }

    @Test
    public void workProducerUsingCDL() throws InterruptedException {
        WorkProducerUsingCDL producerUsingCDL = new WorkProducerUsingCDL();
        Thread thread = new Thread(producerUsingCDL);
        thread.start();

        // Main must wait for the above producer to be over.
        thread.join();
        System.out.println("Successfully executed..");
    }

    @Test
    public void workProducerUsingPhaser() throws InterruptedException {
        WorkProducerUsingPhaser workProducerUsingPhaser = new WorkProducerUsingPhaser();
        Thread thread = new Thread(workProducerUsingPhaser);
        thread.start();

        // Main must wait for the above producer to be over.
        thread.join();
        System.out.println("Successfully executed..");

    }

}
