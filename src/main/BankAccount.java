package main;

public class BankAccount {

    private double balance;

    public BankAccount() {
        this.balance = 0;
    }

    public void deposit(double amount) {
        if(amount > 0) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void collectFee(double fee) {
        if (fee <= 0) {
            throw new IllegalArgumentException("Fee must be positive");
        }
        if (fee > this.balance) {
            throw new IllegalArgumentException("Insufficient funds to collect fee");
        }
        this.balance -= fee;
    }

    public double getBalance() {
        return this.balance;
    }
}