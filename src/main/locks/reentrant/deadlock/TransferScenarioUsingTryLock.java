package main.locks.reentrant.deadlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferScenarioUsingTryLock {
    private Account account1 = new Account();
    private Account account2 = new Account();

    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();

    public void transferFromFirstToSecond() throws InterruptedException {
        Random random = new Random();
        for(int i=0; i<10000; i++) {
            acquireLocks();
            try {
                Account.transfer(account1,account2, random.nextInt(100));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void transferFromSecondToFirst() throws InterruptedException {
        Random random = new Random();
        for(int i=0; i<10000; i++) {
            acquireLocks();
            try {
                Account.transfer(account2,account1, random.nextInt(100));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    private void acquireLocks() throws InterruptedException {
        boolean gotFirstLock = false;
        boolean gotSecondLock = false;
        try{
            gotFirstLock = lock1.tryLock();
            gotSecondLock = lock2.tryLock();
        } finally {
            if(gotFirstLock && gotSecondLock) return;
            if(gotFirstLock) lock1.unlock();
            if(gotSecondLock) lock2.unlock();
        }
        //Locks not acquired
        Thread.sleep(1);
    }

    public void finished() {
        System.out.println("Account 1 balance is: " + account1.getBalance());
        System.out.println("Account 2 balance is: " + account2.getBalance());
        System.out.println("Total balance is: " + (account1.getBalance() + account2.getBalance()) );
    }
}
