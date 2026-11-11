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
    public void testCollectFee() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        testAccount.collectFee(15);
        assertEquals(85, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testCollectFeeEntireBalance() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(20);
        testAccount.collectFee(20);
        assertEquals(0, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidFeeNegative() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        try {
            testAccount.collectFee(-10);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }

    @Test
    public void testInvalidFeeZero() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        try {
            testAccount.collectFee(0);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }

    @Test
    public void testFeeExceedsBalance() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(10);
        try {
            testAccount.collectFee(50);
            fail();
        } catch (IllegalArgumentException e) {
            //balance should stay the same
            assertEquals(10, testAccount.getBalance(), 0.01);
        }
    }
}