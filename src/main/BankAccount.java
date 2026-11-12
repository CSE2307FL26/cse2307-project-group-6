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
        validatePositiveAmount(amount);

        if (!Double.isFinite(balance + amount)) {
            throw new IllegalArgumentException("Deposit exceeds account limits.");
        }

        balance += amount;
        transactionHistory.add(new Transaction(amount));
    }

    public void withdraw(double amount) {
        validatePositiveAmount(amount);
        validateSufficientFunds(amount);

        balance -= amount;
    }

    public void addInterest(double interestRate) {
        validateInterestRate(interestRate);

        double interestPayment = balance * (interestRate / 100);

        if (!Double.isFinite(interestPayment)
                || !Double.isFinite(balance + interestPayment)) {
            throw new IllegalArgumentException("Interest payment is too large.");
        }

        balance += interestPayment;
    }

    public void transfer(BankAccount destination, double amount) {
        validatePositiveAmount(amount);
        validateSufficientFunds(amount);
        validateDestination(destination, amount);

        balance -= amount;
        destination.balance += amount;
    }

    public void collectFee(double feeAmount) {
        validatePositiveAmount(feeAmount);
        validateSufficientFunds(feeAmount);

        balance -= feeAmount;
    }

    private void validatePositiveAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                "Amount must be positive and finite."
            );
        }
    }

    private void validateSufficientFunds(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
    }

    private void validateInterestRate(double interestRate) {
        if (!Double.isFinite(interestRate) || interestRate < 0) {
            throw new IllegalArgumentException(
                "Interest rate must be a non-negative number."
            );
        }
    }

    private void validateDestination(BankAccount destination, double amount) {
        if (destination == null || destination == this) {
            throw new IllegalArgumentException(
                "Invalid destination account."
            );
        }

        if (!Double.isFinite(destination.balance + amount)) {
            throw new IllegalArgumentException(
                "Transfer exceeds account limits."
            );
        }
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public static class Transaction {

        private final double amount;

        public Transaction(double amount) {
            this.amount = amount;
        }

        public double getAmount() {
            return amount;
        }
    }
}