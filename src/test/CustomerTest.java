package test;

import main.BankAccount;
import main.Customer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    public void testNewCustomerHasOneAccount() {
        Customer customer = new Customer();
        assertEquals(1, customer.getNumberOfAccounts());
    }

    @Test
    public void testOpenAccountAddsAccount() {
        Customer customer = new Customer();
        customer.openAccount();
        assertEquals(2, customer.getNumberOfAccounts());
    }

    @Test
    public void testNewAccountStartsAtZero() {
        Customer customer = new Customer();
        BankAccount newAccount = customer.openAccount();
        assertEquals(0, newAccount.getBalance(), 0.01);
    }

    @Test
    public void testNewAccountIsSeparate() {
        Customer customer = new Customer();
        BankAccount first = customer.getAccounts().get(0);
        BankAccount second = customer.openAccount();
        first.deposit(100);
        assertNotSame(first, second);
        assertEquals(0, second.getBalance(), 0.01);
        assertTrue(customer.getAccounts().contains(second));
    }

    @Test
    public void testCloseAccountRemovesAccount() {
        Customer customer = new Customer();
        BankAccount second = customer.openAccount();
        customer.closeAccount(1);
        assertEquals(1, customer.getNumberOfAccounts());
        assertFalse(customer.getAccounts().contains(second));
    }

    @Test
    public void testCloseAccountReturnsBalance() {
        Customer customer = new Customer();
        BankAccount second = customer.openAccount();
        second.deposit(75);
        assertEquals(75, customer.closeAccount(1), 0.01);
    }

    @Test
    public void testCannotCloseOnlyAccount() {
        Customer customer = new Customer();
        assertThrows(IllegalStateException.class, () -> customer.closeAccount(0));
        assertEquals(1, customer.getNumberOfAccounts());
    }

    @Test
    public void testCannotCloseNonexistentAccount() {
        Customer customer = new Customer();
        customer.openAccount();
        assertThrows(IllegalArgumentException.class, () -> customer.closeAccount(5));
        assertThrows(IllegalArgumentException.class, () -> customer.closeAccount(-1));
    }
}