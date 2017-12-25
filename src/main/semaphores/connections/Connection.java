package semaphores.connections;

/**
 * Created by mittaa3 on 2/18/2015.
 */
public enum Connection {
    INSTANCE;

    private int connections = 0;

    // Creation of enum is thread-safe but methods dont have such guarantee.
    public void connect(){
        synchronized (this) {
            connections++ ;
            System.out.println("Connections: " + connections);
        }
        // Connect to the DB.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (this) {
            connections--;
        }
    }
}
