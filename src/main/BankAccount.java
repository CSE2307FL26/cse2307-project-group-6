package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private static int nextAccountNumber = 10000001;

    private final String accountNumber;
    private final String ssn;
    private double balance;
    private List<Transaction> transactionHistory;

    public BankAccount() {
        this.accountNumber = generateAccountNumber();
        this.ssn = null;
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }
    public BankAccount(String ssn) {
        this.accountNumber = generateAccountNumber();
        this.ssn = normalizeSsn(ssn);
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }

    private static synchronized String generateAccountNumber() {
        return String.valueOf(nextAccountNumber++);
    }

    // Accepts "123-45-6789" or "123456789" and returns "123-45-6789".
    public static String normalizeSsn(String ssn) {
        if (ssn == null) {
            throw new IllegalArgumentException("SSN is required.");
        }

        String trimmed = ssn.trim();
        String digits;

        if (trimmed.matches("\\d{3}-\\d{2}-\\d{4}")) {
            digits = trimmed.replace("-", "");
        } else if (trimmed.matches("\\d{9}")) {
            digits = trimmed;
        } else {
            throw new IllegalArgumentException(
                    "SSN must be 9 digits (123-45-6789 or 123456789).");
        }

        String area = digits.substring(0, 3);
        String group = digits.substring(3, 5);
        String serial = digits.substring(5);

        if (area.equals("000") || area.equals("666") || area.charAt(0) == '9'
                || group.equals("00") || serial.equals("0000")) {
            throw new IllegalArgumentException("SSN is not valid.");
        }

        return area + "-" + group + "-" + serial;
    }

    public static boolean isValidSsn(String ssn) {
        try {
            normalizeSsn(ssn);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    // "123-45-6789" -> "***-**-6789"
    public static String maskSsn(String ssn) {
        return "***-**-" + normalizeSsn(ssn).substring(7);
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

    public String getAccountNumber() {
        return accountNumber;
    }

    // Returns null if the account has no owner on file.
    public String getSsn() {
        return ssn;
    }

    public String getMaskedSsn() {
        return ssn == null ? "none on file" : maskSsn(ssn);
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