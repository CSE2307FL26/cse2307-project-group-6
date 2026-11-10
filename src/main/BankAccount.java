package main;

public class BankAccount {

    private double balance;

    public BankAccount() {
        this.balance = 0;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void transfer(BankAccount account, double amount) {
        if (amount <= 0 || amount > this.balance || account == null) {
            throw new IllegalArgumentException();
        }

        this.balance -= amount;
        account.balance += amount;
    }

    public double getBalance() {
        return this.balance;
    }
}
