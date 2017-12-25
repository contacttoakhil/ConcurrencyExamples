package others.producer.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Akhil on 31-03-2015.
 */
public class WorkProducerUsingECS implements Runnable{
    private final CompletionService service;
    private final List<Future<Integer>> listOfFutures;

    public WorkProducerUsingECS() {
        this.listOfFutures = new ArrayList<>();
        service = new ExecutorCompletionService(Executors.newFixedThreadPool(5));
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
            listOfFutures.add(service.submit(new WorkerThread(),1));
        }
    }

    public void getResultAfterWorkIsOver() throws InterruptedException, ExecutionException {
        for(int i=0; i<listOfFutures.size(); i++) {
            Integer result = (Integer) service.take().get();
            System.out.println("Result: "  + result);
        }
    }
}
