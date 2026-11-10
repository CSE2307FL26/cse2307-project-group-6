package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void transferMovesMoneyBetweenAccounts() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        account1.deposit(100);

        account1.transfer(account2, 40);

        assertEquals(60, account1.getBalance(), 0.0001);
        assertEquals(40, account2.getBalance(), 0.0001);
    }

    @Test
    void transferOfFullBalanceLeavesAccountEmpty() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        account1.deposit(100);

        account1.transfer(account2, 100);

        assertEquals(0, account1.getBalance(), 0.0001);
        assertEquals(100, account2.getBalance(), 0.0001);
    }

    @Test
    void transferDoesNotChangeTotalMoney() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        account1.deposit(100);
        account2.deposit(50);

        account1.transfer(account2, 30);

        double totalBalance = account1.getBalance() + account2.getBalance();

        assertEquals(150, totalBalance, 0.0001);
    }

    @Test
    void transferMoreThanBalanceIsRejected() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        account1.deposit(50);

        assertThrows(IllegalArgumentException.class, () -> {
            account1.transfer(account2, 100);
        });

        assertEquals(50, account1.getBalance(), 0.0001);
        assertEquals(0, account2.getBalance(), 0.0001);
    }

    @Test
    void transferOfNegativeAmountIsRejected() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        account1.deposit(100);

        assertThrows(IllegalArgumentException.class, () -> {
            account1.transfer(account2, -20);
        });

        assertEquals(100, account1.getBalance(), 0.0001);
        assertEquals(0, account2.getBalance(), 0.0001);
    }

    @Test
    void transferOfZeroIsRejected() {
        BankAccount account1 = new BankAccount();
        BankAccount account2 = new BankAccount();

        account1.deposit(100);

        assertThrows(IllegalArgumentException.class, () -> {
            account1.transfer(account2, 0);
        });

        assertEquals(100, account1.getBalance(), 0.0001);
        assertEquals(0, account2.getBalance(), 0.0001);
    }
}
