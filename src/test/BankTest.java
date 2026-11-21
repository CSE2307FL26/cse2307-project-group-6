package test;

import main.BankAccount;
import main.Bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class BankTest {

    @Test
    public void testNewBankHasOneAccount() {
        Bank bank = new Bank();
        assertEquals(1, bank.getNumberOfAccounts());
    }

    @Test
    public void testNewBankStartingAccountIsEmpty() {
        Bank bank = new Bank();
        assertEquals(0, bank.getAccount(0).getBalance(), 0.01);
    }

    @Test
    public void testNumberOfAccountsTracksOpenAndClose() {
        Bank bank = new Bank();
        bank.openAccount();
        bank.openAccount();
        assertEquals(3, bank.getNumberOfAccounts());

        bank.closeAccount(2);
        assertEquals(2, bank.getNumberOfAccounts());
    }

    @Test
    public void testOpenAccountReturnsNewAccount() {
        Bank bank = new Bank();
        BankAccount opened = bank.openAccount();
        assertNotNull(opened);
        assertEquals(0, opened.getBalance(), 0.01);
    }

    @Test
    public void testOpenAccountAddsAccount() {
        Bank bank = new Bank();
        bank.openAccount();
        assertEquals(2, bank.getNumberOfAccounts());
    }

    @Test
    public void testOpenedAccountIsStoredAtNextIndex() {
        Bank bank = new Bank();
        BankAccount opened = bank.openAccount();
        assertSame(opened, bank.getAccount(1));
    }

    @Test
    public void testOpenedAccountCanReceiveMoney() {
        Bank bank = new Bank();
        BankAccount opened = bank.openAccount();
        opened.deposit(40);
        assertEquals(40, bank.getAccount(1).getBalance(), 0.01);
    }

    @Test
    public void testOpenedAccountsAreSeparate() {
        Bank bank = new Bank();
        BankAccount first = bank.getAccount(0);
        BankAccount second = bank.openAccount();
        first.deposit(100);
        assertNotSame(first, second);
        assertEquals(0, second.getBalance(), 0.01);
    }

    @Test
    public void testGetAccountReturnsSameAccountEachTime() {
        Bank bank = new Bank();
        assertSame(bank.getAccount(0), bank.getAccount(0));
    }

    @Test
    public void testGetAccountNegativeIndexThrows() {
        Bank bank = new Bank();
        assertThrows(IllegalArgumentException.class, () -> bank.getAccount(-1));
    }

    @Test
    public void testGetAccountIndexTooLargeThrows() {
        Bank bank = new Bank();
        assertThrows(IllegalArgumentException.class, () -> bank.getAccount(1));
    }

    @Test
    public void testCloseAccountReturnsBalance() {
        Bank bank = new Bank();
        BankAccount second = bank.openAccount();
        second.deposit(75);
        assertEquals(75, bank.closeAccount(1), 0.01);
    }

    @Test
    public void testCloseEmptyAccountReturnsZero() {
        Bank bank = new Bank();
        bank.openAccount();
        assertEquals(0, bank.closeAccount(1), 0.01);
    }

    @Test
    public void testCloseAccountRemovesAccount() {
        Bank bank = new Bank();
        BankAccount second = bank.openAccount();
        bank.closeAccount(1);
        assertEquals(1, bank.getNumberOfAccounts());
        assertFalse(bank.getAccounts().contains(second));
    }

    @Test
    public void testCloseAccountShiftsLaterAccountsDown() {
        Bank bank = new Bank();
        bank.openAccount();
        BankAccount third = bank.openAccount();
        bank.closeAccount(1);
        assertSame(third, bank.getAccount(1));
    }

    @Test
    public void testCannotCloseOnlyAccount() {
        Bank bank = new Bank();
        assertThrows(IllegalStateException.class, () -> bank.closeAccount(0));
        assertEquals(1, bank.getNumberOfAccounts());
    }

    @Test
    public void testCannotCloseNonexistentAccount() {
        Bank bank = new Bank();
        bank.openAccount();
        assertThrows(IllegalArgumentException.class, () -> bank.closeAccount(5));
        assertThrows(IllegalArgumentException.class, () -> bank.closeAccount(-1));
        assertEquals(2, bank.getNumberOfAccounts());
    }

    @Test
    public void testGetAccountsReturnsAllAccountsInOrder() {
        Bank bank = new Bank();
        BankAccount first = bank.getAccount(0);
        BankAccount second = bank.openAccount();

        List<BankAccount> accounts = bank.getAccounts();
        assertEquals(2, accounts.size());
        assertSame(first, accounts.get(0));
        assertSame(second, accounts.get(1));
    }

    @Test
    public void testGetAccountsIsUnmodifiable() {
        Bank bank = new Bank();
        List<BankAccount> accounts = bank.getAccounts();
        assertThrows(UnsupportedOperationException.class,
                () -> accounts.add(new BankAccount()));
        assertEquals(1, bank.getNumberOfAccounts());
    }

    @Test
    public void testGetAccountsReflectsNewlyOpenedAccount() {
        Bank bank = new Bank();
        BankAccount opened = bank.openAccount();
        assertTrue(bank.getAccounts().contains(opened));
    }
}