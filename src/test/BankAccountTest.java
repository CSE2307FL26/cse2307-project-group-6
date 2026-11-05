package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
            //do nothing, test passes
        }
    }
    @Test
    public void testInitialBalance() {
        BankAccount account = new BankAccount();
        assertEquals(0, account.getBalance(), 0.01);
    }

    @Test
    public void testBalanceAfterDeposits() {
        BankAccount account = new BankAccount();
        account.deposit(100);
        account.deposit(50);
        assertEquals(150, account.getBalance(), 0.01);
    }

}
