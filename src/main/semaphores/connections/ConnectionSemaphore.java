package semaphores.connections;

import java.util.concurrent.Semaphore;

public class ConnectionSemaphore {

    private Semaphore semaphore = new Semaphore(10); //Semaphore with 10 permits

    //Thread-safe Singleton
    private ConnectionSemaphore() {}

    //Non-lazy
    //private static final ConnectionSemaphore INSTANCE = new ConnectionSemaphore();
    //public static ConnectionSemaphore getInstance() { return INSTANCE; }

    //lazy-loaded: put instance in static class.
    private static class Holder {
        private static final ConnectionSemaphore INSTANCE = new ConnectionSemaphore();
    }
    public static ConnectionSemaphore getInstance() { return Holder.INSTANCE; }

    private int connections = 0;

    public void connect() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            doConnect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private void doConnect() throws InterruptedException {
        synchronized (this) {
            connections++;
            System.out.println("Connections: " + connections);
        }
        Thread.sleep(5000);
        synchronized (this) {
            connections--;
        }
    }
}
