package main.nonBlocking.concurrentLinkedDeque;

import main.nonBlocking.concurrentLinkedDeque.tasks.AddTask;
import main.nonBlocking.concurrentLinkedDeque.tasks.PollTask;

import java.util.concurrent.ConcurrentLinkedDeque;

public class MainApp {
    public static void main(String[] args) {
        ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
        Thread threads[] = new Thread[10];  // 100 thread objects.

        // Initiate 100 AddTask objects
        for (int i = 0; i < threads.length; i++) {
            AddTask task = new AddTask(list);
            threads[i] = new Thread(task);
            threads[i].start();
        }

        // Wait for all these threads to populate.
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Check list size
        System.out.println("List size: " + list.size());

        // Initiate 100 PollTask objects
        for (int i = 0; i < threads.length; i++) {
            PollTask task = new PollTask(list);
            threads[i] = new Thread(task);
            threads[i].start();
        }

        // Wait for all these threads to populate.
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Check list size
        System.out.println("List size: " + list.size());
    }
}
