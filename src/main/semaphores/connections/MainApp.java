package semaphores.connections;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0; i<200; i++) {
            executorService.submit(() -> {
                //Connection.INSTANCE.connect();
                ConnectionSemaphore.getInstance().connect();
            });
        }
    }
}
