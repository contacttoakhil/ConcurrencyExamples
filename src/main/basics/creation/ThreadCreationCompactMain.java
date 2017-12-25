package main.basics.creation;

/**
 * Created by mittaa3 on 2/18/2015.
 */
public class ThreadCreationCompactMain {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("Thread started in compact mode.");
        });
        thread.start();
    }
}
