package test;

import main.BankAccount;
import main.BankAccount.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

    @Test
    public void testDeposit() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidDeposit() {
        BankAccount testAccount = new BankAccount();
        try {
            testAccount.deposit(-50);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    void newAccountHasEmptyHistory() {
        BankAccount account = new BankAccount();
        List<Transaction> history = account.getTransactionHistory();

        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    void depositIsRecordedInHistory() {
        BankAccount account = new BankAccount();
        account.deposit(100);

        List<Transaction> history = account.getTransactionHistory();
        Transaction transaction = history.get(0);

        assertEquals(1, history.size());
        assertEquals(100, transaction.getAmount(), 0.0001);
    }

    @Test
    void decimalAmountsArePreservedInHistory() {
        BankAccount account = new BankAccount();
        account.deposit(12.34);

        List<Transaction> history = account.getTransactionHistory();
        Transaction transaction = history.get(0);

        assertEquals(12.34, transaction.getAmount(), 0.0001);
    }

    @Test
    void repeatedIdenticalDepositsAreEachRecordedInHistory() {
        BankAccount account = new BankAccount();
        account.deposit(10);
        account.deposit(10);
        account.deposit(10);

        List<Transaction> history = account.getTransactionHistory();

        assertEquals(3, history.size());
    }

    @Test
    void largeNumberOfDepositsAreAllRecordedInHistory() {
        BankAccount account = new BankAccount();

        for (int i = 0; i < 100; i++) {
            account.deposit(1);
        }

        List<Transaction> history = account.getTransactionHistory();
        assertEquals(100, history.size());
    }
}
