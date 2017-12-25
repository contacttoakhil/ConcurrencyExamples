package main.basics.join;

import java.util.Random;

public class ThreadJoinMain {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() ->  {
            Random random = new Random();
            for(int i=0; i< 1E8; i++) {
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println("I am interrupted");
                    break;
                }
                double result = Math.sin(random.nextDouble());  //not using result, just simulating some work.
                //System.out.println(result);
            }
        });

        thread.start();
        Thread.sleep(5000); // Main thread sleeps

        //thread.interrupt();
        thread.join();   // Main thread joins thread1.

        System.out.println(" Main finished");
    }
}
