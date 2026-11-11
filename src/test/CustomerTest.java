package test;

import main.BankAccount;
import main.Customer;

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
}