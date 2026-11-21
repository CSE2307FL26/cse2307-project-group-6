package test;

import main.BankAccount;
import main.Customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    public void testNewCustomerHasOneAccount() {
        Customer customer = new Customer();
        assertEquals(1, customer.getNumberOfAccounts());
    }

    @Test
    public void testNewCustomerStartingAccountIsEmpty() {
        Customer customer = new Customer();
        assertEquals(0, customer.getAccount(0).getBalance(), 0.01);
    }

    @Test
    public void testNumberOfAccountsTracksOpenAndClose() {
        Customer customer = new Customer();
        customer.openAccount();
        customer.openAccount();
        assertEquals(3, customer.getNumberOfAccounts());

        customer.closeAccount(2);
        assertEquals(2, customer.getNumberOfAccounts());
    }

    @Test
    public void testOpenAccountReturnsNewAccount() {
        Customer customer = new Customer();
        BankAccount opened = customer.openAccount();
        assertNotNull(opened);
        assertEquals(0, opened.getBalance(), 0.01);
    }

    @Test
    public void testOpenAccountAddsAccount() {
        Customer customer = new Customer();
        customer.openAccount();
        assertEquals(2, customer.getNumberOfAccounts());
    }

    @Test
    public void testOpenedAccountIsStoredAtNextIndex() {
        Customer customer = new Customer();
        BankAccount opened = customer.openAccount();
        assertSame(opened, customer.getAccount(1));
    }

    @Test
    public void testOpenedAccountCanReceiveMoney() {
        Customer customer = new Customer();
        BankAccount opened = customer.openAccount();
        opened.deposit(40);
        assertEquals(40, customer.getAccount(1).getBalance(), 0.01);
    }

    @Test
    public void testOpenedAccountsAreSeparate() {
        Customer customer = new Customer();
        BankAccount first = customer.getAccount(0);
        BankAccount second = customer.openAccount();
        first.deposit(100);
        assertNotSame(first, second);
        assertEquals(0, second.getBalance(), 0.01);
    }

    @Test
    public void testGetAccountReturnsSameAccountEachTime() {
        Customer customer = new Customer();
        assertSame(customer.getAccount(0), customer.getAccount(0));
    }

    @Test
    public void testGetAccountNegativeIndexThrows() {
        Customer customer = new Customer();
        assertThrows(IllegalArgumentException.class, () -> customer.getAccount(-1));
    }

    @Test
    public void testGetAccountIndexTooLargeThrows() {
        Customer customer = new Customer();
        assertThrows(IllegalArgumentException.class, () -> customer.getAccount(1));
    }

    @Test
    public void testCloseAccountReturnsBalance() {
        Customer customer = new Customer();
        BankAccount second = customer.openAccount();
        second.deposit(75);
        assertEquals(75, customer.closeAccount(1), 0.01);
    }

    @Test
    public void testCloseEmptyAccountReturnsZero() {
        Customer customer = new Customer();
        customer.openAccount();
        assertEquals(0, customer.closeAccount(1), 0.01);
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
    public void testCloseAccountShiftsLaterAccountsDown() {
        Customer customer = new Customer();
        customer.openAccount();
        BankAccount third = customer.openAccount();
        customer.closeAccount(1);
        assertSame(third, customer.getAccount(1));
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
        assertEquals(2, customer.getNumberOfAccounts());
    }

    @Test
    public void testGetAccountsReturnsAllAccountsInOrder() {
        Customer customer = new Customer();
        BankAccount first = customer.getAccount(0);
        BankAccount second = customer.openAccount();

        List<BankAccount> accounts = customer.getAccounts();
        assertEquals(2, accounts.size());
        assertSame(first, accounts.get(0));
        assertSame(second, accounts.get(1));
    }

    @Test
    public void testGetAccountsIsUnmodifiable() {
        Customer customer = new Customer();
        List<BankAccount> accounts = customer.getAccounts();
        assertThrows(UnsupportedOperationException.class,
                () -> accounts.add(new BankAccount()));
        assertEquals(1, customer.getNumberOfAccounts());
    }

    @Test
    public void testGetAccountsReflectsNewlyOpenedAccount() {
        Customer customer = new Customer();
        BankAccount opened = customer.openAccount();
        assertTrue(customer.getAccounts().contains(opened));
    }
}