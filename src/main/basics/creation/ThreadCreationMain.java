package main.basics.creation;

/**
 * Created by mittaa3 on 2/18/2015.
 */
public class ThreadCreationMain {
    static class Runner implements Runnable {
        @Override
        public void run() {
            System.out.println(" Thread is running");
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new Runner());
        thread.start();
    }
}
