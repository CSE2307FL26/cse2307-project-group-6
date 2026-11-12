package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {

    private List<BankAccount> accounts;

    public Customer() {
        this.accounts = new ArrayList<>();
        this.accounts.add(new BankAccount());
    }

    public BankAccount openAccount() {
        BankAccount newAccount = new BankAccount();
        this.accounts.add(newAccount);
        return newAccount;
    }

    public BankAccount getAccount(int accountIndex) {
    if (accountIndex < 0 || accountIndex >= accounts.size()) {
        throw new IllegalArgumentException("Account does not exist.");
    }

    return accounts.get(accountIndex);
    }

    public double closeAccount(int index) {
        if (index < 0 || index >= this.accounts.size()) {
            throw new IllegalArgumentException("Account does not exist");
        }
        if (this.accounts.size() == 1) {
            throw new IllegalStateException("Cannot close your only account");
        }
        BankAccount closedAccount = this.accounts.remove(index);
        return closedAccount.getBalance();
    }
    public List<BankAccount> getAccounts() {
        return Collections.unmodifiableList(this.accounts);
    }

    public int getNumberOfAccounts() {
        return this.accounts.size();
    }
}