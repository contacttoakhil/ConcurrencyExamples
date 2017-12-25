package main.blocking;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Example taken from http://stackoverflow.com/questions/32985941/when-communicating-with-two-threads-do-i-have-to-use-pipes/32986151#32986151
 * I most commonly use ArrayBlockingQueue as it allows me to limit the memory footprint of the solution. A LinkedBlockingDeque can be used for an unlimited size but be certain you cannot overload memory.
 */
public class ProducerConsumerExample {

    public static void main(String[] args) throws InterruptedException{
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        Thread pt = new Thread(new Producer(queue));
        Thread ct = new Thread(new Consumer(queue));
        // Start it all going.
        pt.start();
        ct.start();
        // Wait for it to finish.
        pt.join();
        ct.join();
    }

    private static final Integer End = -1; // end of list.

    static class Producer implements Runnable {

        final BlockingQueue<Integer> queue;

        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 1000; i++) {
                    queue.add(i);
                    Thread.sleep(1);
                }
                // Finish the queue.
                queue.add(End);
            } catch (InterruptedException ex) {
                // Just exit.
            }
        }

    }

    static class Consumer implements Runnable {

        final BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            boolean ended = false;
            while (!ended) {
                try {
                    Integer i = queue.take();
                    ended = i == End;
                    System.out.println(i);
                } catch (InterruptedException ex) {
                    ended = true;
                }
            }
        }

    }

    public void test() throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        Thread pt = new Thread(new Producer(queue));
        Thread ct = new Thread(new Consumer(queue));
        // Start it all going.
        pt.start();
        ct.start();
        // Wait for it to finish.
        pt.join();
        ct.join();
    }
}
