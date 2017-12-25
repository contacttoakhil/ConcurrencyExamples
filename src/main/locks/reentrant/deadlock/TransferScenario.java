package main.locks.reentrant.deadlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferScenario {
    private Account account1 = new Account();
    private Account account2 = new Account();

    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();

    public void transferFromFirstToSecond() {
        Random random = new Random();
        for(int i=0; i<10000; i++) {
            lock1.lock();
            lock2.lock();
            try {
                Account.transfer(account1,account2, random.nextInt(100));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void transferFromSecondToFirst() {
        Random random = new Random();
        for(int i=0; i<10000; i++) {
            lock1.lock();   //What if reverse the locking sequence ?
            lock2.lock();
            try {
                Account.transfer(account2,account1, random.nextInt(100));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void finished() {
        System.out.println("Account 1 balance is: " + account1.getBalance());
        System.out.println("Account 2 balance is: " + account2.getBalance());
        System.out.println("Total balance is: " + (account1.getBalance() + account2.getBalance()) );
    }
}
