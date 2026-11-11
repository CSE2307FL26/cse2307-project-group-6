package test;

import main.BankAccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
    public void testAddInterest() {
        BankAccount account = new BankAccount();

        account.deposit(1000);
        account.addInterest(5);

        assertEquals(1050, account.getBalance(), 0.01);
    }

    @Test
    public void testZeroInterest() {
        BankAccount account = new BankAccount();

        account.deposit(1000);
        account.addInterest(0);

        assertEquals(1000, account.getBalance(), 0.01);
    }

    @Test
    public void testInterestOnEmptyAccount() {
        BankAccount account = new BankAccount();
        account.addInterest(5);
        assertEquals(0, account.getBalance(), 0.01);
    }

    @Test
    public void testInvalidInterestRate() {
        BankAccount account = new BankAccount();

        account.deposit(1000);

    assertThrows(IllegalArgumentException.class,() -> account.addInterest(-5));
    assertEquals(1000, account.getBalance(), 0.01);
    }

    @Test
    public void testNonFiniteInterestRate() {
        BankAccount account = new BankAccount();
        account.deposit(1000);

        assertThrows(IllegalArgumentException.class,() -> account.addInterest(Double.NaN));

        assertThrows(IllegalArgumentException.class,() -> account.addInterest(Double.POSITIVE_INFINITY));

        assertEquals(1000, account.getBalance(), 0.01);
    }
    @Test
    public void testInterestPaymentOverflow() {
        BankAccount account = new BankAccount();

        account.deposit(Double.MAX_VALUE);

        assertThrows(IllegalArgumentException.class,() -> account.addInterest(100));

        assertEquals(Double.MAX_VALUE, account.getBalance(), 0.0);
    }
}   
