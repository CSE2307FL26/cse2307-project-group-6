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
        recordTransaction(TransactionType.DEPOSIT, amount);
    }

    public void withdraw(double amount) {
        validatePositiveAmount(amount);
        validateSufficientFunds(amount);

        balance -= amount;
        recordTransaction(TransactionType.WITHDRAWAL, amount);

    }

    public void addInterest(double interestRate) {
        validateInterestRate(interestRate);

        double interestPayment = balance * (interestRate / 100);

        if (!Double.isFinite(interestPayment)
                || !Double.isFinite(balance + interestPayment)) {
            throw new IllegalArgumentException("Interest payment is too large.");
        }

        balance += interestPayment;

        if (interestPayment > 0) {
            recordTransaction(TransactionType.INTEREST, interestPayment);
        }
    }

    public void transfer(BankAccount destination, double amount) {
        validatePositiveAmount(amount);
        validateSufficientFunds(amount);
        validateDestination(destination, amount);

        balance -= amount;
        recordTransaction(TransactionType.TRANSFER_OUT, amount);


        destination.balance += amount;
        destination.recordTransaction(TransactionType.TRANSFER_IN, amount);

    }

    public void collectFee(double feeAmount) {
        validatePositiveAmount(feeAmount);
        validateSufficientFunds(feeAmount);

        balance -= feeAmount;
        recordTransaction(TransactionType.FEE, feeAmount);

    }

    private void recordTransaction(TransactionType type, double amount) {
        transactionHistory.add(new Transaction(type, amount, balance));
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

    public enum TransactionType {
        DEPOSIT("Deposit", true),
        WITHDRAWAL("Withdrawal", false),
        TRANSFER_IN("Transfer in", true),
        TRANSFER_OUT("Transfer out", false),
        FEE("Fee", false),
        INTEREST("Interest", true);

        private final String label;
        private final boolean credit;

        TransactionType(String label, boolean credit) {
            this.label = label;
            this.credit = credit;
        }

        public String getLabel() {
            return label;
        }

        public boolean isCredit() {
            return credit;
        }
    }

    public static class Transaction {

        private final TransactionType type;
        private final double amount;
        private final double balanceAfter;

        public Transaction(TransactionType type, double amount, double balanceAfter) {
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
        }

        public TransactionType getType() {
            return type;
        }

        public double getAmount() {
            return amount;
        }

        public double getSignedAmount() {
            return type.isCredit() ? amount : -amount;
        }

        public double getBalanceAfter() {
            return balanceAfter;
        }
    }
}