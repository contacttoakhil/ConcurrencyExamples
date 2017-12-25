package main.locks.reentrant.deadlock;

public class DeadlockExampleMain {
    public static void main(String[] args) {
        TransferScenario scenario = new TransferScenario();
        //TransferScenarioUsingTryLock scenario = new TransferScenarioUsingTryLock();

        Thread thread1 = new Thread(() -> {
            scenario.transferFromFirstToSecond();
        });

        Thread thread2 = new Thread(() -> {
            scenario.transferFromSecondToFirst();
        });

        thread1.start(); thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scenario.finished();
    }
}
