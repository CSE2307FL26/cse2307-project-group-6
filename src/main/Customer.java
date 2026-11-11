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

    public List<BankAccount> getAccounts() {
        return Collections.unmodifiableList(this.accounts);
    }

    public int getNumberOfAccounts() {
        return this.accounts.size();
    }
}