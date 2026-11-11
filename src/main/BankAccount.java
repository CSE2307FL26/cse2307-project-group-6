package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private double balance;
    private List<Transaction> transactionHistory;

    public BankAccount() {
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            transactionHistory.add(new Transaction(amount));
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance -= amount;
    }

    public void addInterest(double interestRate) {
        if (!Double.isFinite(interestRate) || interestRate < 0) {
            throw new IllegalArgumentException("Interest rate must be a non-negative number.");
            }

        double interestPayment = balance * (interestRate / 100);

        if (!Double.isFinite(interestPayment) || !Double.isFinite(balance + interestPayment)) {
            throw new IllegalArgumentException("Interest payment is too large.");
            }

        balance += interestPayment;
    }   
  
    public void transfer(BankAccount account, double amount) {
        if (amount <= 0 || amount > this.balance || account == null) {
            throw new IllegalArgumentException();
        }

        this.balance -= amount;
        account.balance += amount;
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

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public static class Transaction {
        private double amount;

        public Transaction(double amount) {
            this.amount = amount;
        }

        public double getAmount() {
            return amount;
        }
    }
}

