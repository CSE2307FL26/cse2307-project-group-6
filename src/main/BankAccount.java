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

    public double getBalance() {
        return this.balance;
    }
}
