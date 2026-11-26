package test;

import main.Bank;
import main.BankAccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class BankTransferTest {

    private static final String OTHER_SSN = "234-56-7890";

    @Test
    public void testTransferToOtherCustomerMovesMoney() {
        Bank bank = new Bank();
        BankAccount myAccount = bank.getAccount(0);
        BankAccount otherAccount = bank.openAccountForOtherCustomer(OTHER_SSN);
        myAccount.deposit(100);

        bank.transferToAccountNumber(myAccount, otherAccount.getAccountNumber(), 40);

        assertEquals(60, myAccount.getBalance(), 0.01);
        assertEquals(40, otherAccount.getBalance(), 0.01);
    }

    @Test
    public void testTransferToOtherCustomerRecordsBothSides() {
        Bank bank = new Bank();
        BankAccount myAccount = bank.getAccount(0);
        BankAccount otherAccount = bank.openAccountForOtherCustomer(OTHER_SSN);
        myAccount.deposit(100);

        bank.transferToAccountNumber(myAccount, otherAccount.getAccountNumber(), 40);

        assertEquals(BankAccount.TransactionType.TRANSFER_OUT,
                myAccount.getTransactionHistory().get(1).getType());
        assertEquals(BankAccount.TransactionType.TRANSFER_IN,
                otherAccount.getTransactionHistory().get(0).getType());
    }

    @Test
    public void testOtherCustomerAccountIsNotListedAsMine() {
        Bank bank = new Bank();
        bank.openAccountForOtherCustomer(OTHER_SSN);

        assertEquals(1, bank.getNumberOfAccounts());
    }

    @Test
    public void testFindAccountByNumberFindsMyAccount() {
        Bank bank = new Bank();
        BankAccount myAccount = bank.getAccount(0);

        assertSame(myAccount, bank.findAccountByNumber(myAccount.getAccountNumber()));
    }

    @Test
    public void testTransferToNonexistentAccountFails() {
        Bank bank = new Bank();
        BankAccount myAccount = bank.getAccount(0);
        myAccount.deposit(100);

        assertThrows(IllegalArgumentException.class,
                () -> bank.transferToAccountNumber(myAccount, "99999999", 40));
        assertEquals(100, myAccount.getBalance(), 0.01);
    }

    @Test
    public void testTransferToClosedAccountFails() {
        Bank bank = new Bank();
        BankAccount myAccount = bank.getAccount(0);
        BankAccount closedAccount = bank.openAccount();
        String closedNumber = closedAccount.getAccountNumber();
        myAccount.deposit(100);
        bank.closeAccount(1);

        assertThrows(IllegalArgumentException.class,
                () -> bank.transferToAccountNumber(myAccount, closedNumber, 40));
        assertEquals(100, myAccount.getBalance(), 0.01);
    }

    @Test
    public void testTransferToOtherCustomerWithInsufficientFundsFails() {
        Bank bank = new Bank();
        BankAccount myAccount = bank.getAccount(0);
        BankAccount otherAccount = bank.openAccountForOtherCustomer(OTHER_SSN);
        myAccount.deposit(10);

        assertThrows(IllegalArgumentException.class,
                () -> bank.transferToAccountNumber(
                        myAccount, otherAccount.getAccountNumber(), 40));
        assertEquals(10, myAccount.getBalance(), 0.01);
        assertEquals(0, otherAccount.getBalance(), 0.01);
    }

    @Test
    public void testTransferToOtherCustomerWithNegativeAmountFails() {
        Bank bank = new Bank();
        BankAccount myAccount = bank.getAccount(0);
        BankAccount otherAccount = bank.openAccountForOtherCustomer(OTHER_SSN);
        myAccount.deposit(100);

        assertThrows(IllegalArgumentException.class,
                () -> bank.transferToAccountNumber(
                        myAccount, otherAccount.getAccountNumber(), -5));
    }

    @Test
    public void testTransferToOwnAccountByNumberFails() {
        Bank bank = new Bank();
        BankAccount myAccount = bank.getAccount(0);
        myAccount.deposit(100);

        assertThrows(IllegalArgumentException.class,
                () -> bank.transferToAccountNumber(
                        myAccount, myAccount.getAccountNumber(), 40));
    }
}
