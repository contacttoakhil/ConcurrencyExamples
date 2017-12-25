package main.locks.stampedLock.bankAccount.pessimistic;

import java.util.concurrent.locks.StampedLock;

public class BankAccount {
    private final StampedLock stampedLock = new StampedLock();
    private int balance = 1000;

    public void deposit(int amount) {
        long stamp = stampedLock.writeLock();
        try {
            balance += amount;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    public int getBalance() {
        long stamp = stampedLock.tryOptimisticRead();


        try {
            return balance;
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
}
