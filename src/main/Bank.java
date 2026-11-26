package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {

    private final String ssn;
    private List<BankAccount> accounts;
    private Map<String, BankAccount> accountsByNumber = new HashMap<>();

    public Bank() {
        this.ssn = null;
        this.accounts = new ArrayList<>();
        this.accounts.add(new BankAccount());
        registerAccount(this.accounts.get(0));
    }

    public Bank(String ssn) {
        this.ssn = BankAccount.normalizeSsn(ssn);
        this.accounts = new ArrayList<>();
        this.accounts.add(new BankAccount(this.ssn));
        registerAccount(this.accounts.get(0));
    }

    public BankAccount openAccount() {
        BankAccount newAccount = (ssn == null) ? new BankAccount() : new BankAccount(ssn);
        this.accounts.add(newAccount);
        registerAccount(newAccount);
        return newAccount;
    }

    public BankAccount openAccountForOtherCustomer(String otherSsn) {
        BankAccount newAccount = new BankAccount(otherSsn);
        registerAccount(newAccount);
        return newAccount;
    }

    public BankAccount getAccount(int accountIndex) {
        if (accountIndex < 0 || accountIndex >= accounts.size()) {
            throw new IllegalArgumentException("Account does not exist.");
        }
        return accounts.get(accountIndex);
    }

    public BankAccount getAccountByNumber(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        throw new IllegalArgumentException("No account with number " + accountNumber + ".");
    }

    public BankAccount findAccountByNumber(String accountNumber) {
        BankAccount account = accountsByNumber.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException(
                    "No account with number " + accountNumber + " exists.");
        }
        return account;
    }

    public void transferToAccountNumber(BankAccount source,
            String destinationAccountNumber, double amount) {
        BankAccount destination = findAccountByNumber(destinationAccountNumber);
        source.transfer(destination, amount);
    }

    public double closeAccount(int index) {
        if (index < 0 || index >= this.accounts.size()) {
            throw new IllegalArgumentException("Account does not exist");
        }
        if (this.accounts.size() == 1) {
            throw new IllegalStateException("Cannot close your only account");
        }
        BankAccount closedAccount = this.accounts.remove(index);
        accountsByNumber.remove(closedAccount.getAccountNumber());
        return closedAccount.getBalance();
    }

    public List<BankAccount> getAccounts() {
        return Collections.unmodifiableList(this.accounts);
    }

    public int getNumberOfAccounts() {
        return this.accounts.size();
    }

    public String getSsn() {
        return ssn;
    }

    public String getMaskedSsn() {
        return ssn == null ? "none on file" : BankAccount.maskSsn(ssn);
    }

    private void registerAccount(BankAccount account) {
        accountsByNumber.put(account.getAccountNumber(), account);
    }
}
