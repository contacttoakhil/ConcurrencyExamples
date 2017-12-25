package main.locks.reentrant.deadlock;

/**
 * Created by mittaa3 on 2/18/2015.
 */
public class Account {
    int balance = 10000;

    public void deposit (int amount) {
        balance = balance + amount;
    }

    public void withdraw(int amount) {
        balance = balance - amount;
    }

    public int getBalance() {
        return  balance;
    }

    public static void transfer (Account account1, Account account2, int amount) {
        account1.withdraw(amount);
        account2.deposit(amount);
    }
}
