package test;

import main.BankAccount;
import main.BankAccount.Transaction;
import main.Customer;

import static org.junit.Assert.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

    private static final String TEST_SSN = "123-45-6789";

    @Test
    public void testNewCustomerHasOneAccount() {
        Customer customer = new Customer(TEST_SSN);
        assertEquals(1, customer.getNumberOfAccounts());
    }

    @Test
    public void testOpenAccountAddsAccount() {
        Customer customer = new Customer(TEST_SSN);
        customer.openAccount();
        assertEquals(2, customer.getNumberOfAccounts());
    }

    @Test
    public void testNewAccountStartsAtZero() {
        Customer customer = new Customer(TEST_SSN);
        BankAccount newAccount = customer.openAccount();
        assertEquals(0, newAccount.getBalance(), 0.01);
    }

    @Test
    public void testNewAccountIsSeparate() {
        Customer customer = new Customer(TEST_SSN);
        BankAccount first = customer.getAccounts().get(0);
        BankAccount second = customer.openAccount();
        first.deposit(100);
        assertNotSame(first, second);
        assertEquals(0, second.getBalance(), 0.01);
        assertTrue(customer.getAccounts().contains(second));
    }
    

    @Test
    public void testCloseAccountRemovesAccount() {
        Customer customer = new Customer(TEST_SSN);
        BankAccount second = customer.openAccount();
        customer.closeAccount(1);
        assertEquals(1, customer.getNumberOfAccounts());
        assertFalse(customer.getAccounts().contains(second));
    }

    @Test
    public void testCloseAccountReturnsBalance() {
        Customer customer = new Customer(TEST_SSN);
        BankAccount second = customer.openAccount();
        second.deposit(75);
        assertEquals(75, customer.closeAccount(1), 0.01);
    }

    @Test
    public void testCannotCloseOnlyAccount() {
        Customer customer = new Customer(TEST_SSN);
        assertThrows(IllegalStateException.class, () -> customer.closeAccount(0));
        assertEquals(1, customer.getNumberOfAccounts());
    }

    @Test
    public void testCannotCloseNonexistentAccount() {
        Customer customer = new Customer(TEST_SSN);
        customer.openAccount();
        assertThrows(IllegalArgumentException.class, () -> customer.closeAccount(5));
        assertThrows(IllegalArgumentException.class, () -> customer.closeAccount(-1));
    }

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
        @Test
        void testCollectFee() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        testAccount.collectFee(15);
        assertEquals(85, testAccount.getBalance(), 0.01);
    }

        @Test
        void testCollectFeeEntireBalance() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(20);
        testAccount.collectFee(20);
        assertEquals(0, testAccount.getBalance(), 0.01);
    }

        @Test
        void testInvalidFeeNegative() {
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


        @Test
        void testInvalidFeeZero() {
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
    public void testWithdraw() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        testAccount.withdraw(40);
        assertEquals(60, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testWithdrawEntireBalance() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.withdraw(50);
        assertEquals(0, testAccount.getBalance(), 0.01);
    }

     @Test
    public void testInvalidWithdrawNegative() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        try {
            testAccount.withdraw(-50);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }

     @Test
    public void testInvalidWithdrawZero() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        try {
            testAccount.withdraw(0);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }

     @Test
    public void testWithdrawInsufficientFunds() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(30);
        try {
            testAccount.withdraw(100);
            fail();
        } catch (IllegalArgumentException e) {
            //balance should stay the same
            assertEquals(30, testAccount.getBalance(), 0.01);
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
 

        @Test
        void testFeeExceedsBalance() {
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

    @Test
    public void testNormalizeSsn() {
        assertEquals("123-45-6789", BankAccount.normalizeSsn("123-45-6789"));
        assertEquals("123-45-6789", BankAccount.normalizeSsn("123456789"));
    }

    @Test
    public void testInvalidSsnFormatThrows() {
        assertThrows(IllegalArgumentException.class, () -> BankAccount.normalizeSsn("12-34"));
        assertThrows(IllegalArgumentException.class, () -> BankAccount.normalizeSsn("abc-de-fghi"));
        assertThrows(IllegalArgumentException.class, () -> BankAccount.normalizeSsn(null));
    }

    @Test
    public void testInvalidSsnNumbersThrow() {
        assertThrows(IllegalArgumentException.class, () -> BankAccount.normalizeSsn("000-45-6789"));
        assertThrows(IllegalArgumentException.class, () -> BankAccount.normalizeSsn("123-00-6789"));
        assertThrows(IllegalArgumentException.class, () -> BankAccount.normalizeSsn("123-45-0000"));
    }

    @Test
    public void testIsValidSsn() {
        assertTrue(BankAccount.isValidSsn(TEST_SSN));
        assertFalse(BankAccount.isValidSsn("abc"));
    }

    @Test
    public void testMaskSsn() {
        assertEquals("***-**-6789", BankAccount.maskSsn(TEST_SSN));
    }

    @Test
    public void testAccountNumberIsEightDigits() {
        BankAccount account = new BankAccount(TEST_SSN);
        assertTrue(account.getAccountNumber().matches("\\d{8}"));
    }

    @Test
    public void testAccountNumbersAreUnique() {
        BankAccount first = new BankAccount(TEST_SSN);
        BankAccount second = new BankAccount(TEST_SSN);
        assertNotEquals(first.getAccountNumber(), second.getAccountNumber());
    }

    @Test
    public void testAccountStoresSsn() {
        BankAccount account = new BankAccount(TEST_SSN);
        assertEquals(TEST_SSN, account.getSsn());
        assertEquals("***-**-6789", account.getMaskedSsn());
    }

    @Test
    public void testCustomerStoresSsn() {
        Customer customer = new Customer(TEST_SSN);
        assertEquals(TEST_SSN, customer.getSsn());
        assertEquals("***-**-6789", customer.getMaskedSsn());
    }

    @Test
    public void testOpenedAccountHasCustomerSsn() {
        Customer customer = new Customer(TEST_SSN);
        BankAccount opened = customer.openAccount();
        assertEquals(TEST_SSN, opened.getSsn());
    }

    @Test
    public void testGetAccountByNumber() {
        Customer customer = new Customer(TEST_SSN);
        BankAccount opened = customer.openAccount();
        assertSame(opened, customer.getAccountByNumber(opened.getAccountNumber()));
    }

    @Test
    public void testGetAccountByUnknownNumberThrows() {
        Customer customer = new Customer(TEST_SSN);
        assertThrows(IllegalArgumentException.class,
                () -> customer.getAccountByNumber("00000000"));
    }

    @Test
    public void testMergeMovesBalanceIntoTarget() {
        Customer customer = new Customer(TEST_SSN);
        BankAccount first = customer.getAccount(0);
        BankAccount second = customer.openAccount();
        first.deposit(100);
        second.deposit(50);

        BankAccount merged = customer.mergeAccounts(
                second.getAccountNumber(), first.getAccountNumber());

        assertSame(first, merged);
        assertEquals(150, merged.getBalance(), 0.01);
    }

    @Test
    public void testMergeClosesSourceAccount() {
        Customer customer = new Customer(TEST_SSN);
        BankAccount first = customer.getAccount(0);
        BankAccount second = customer.openAccount();

        customer.mergeAccounts(second.getAccountNumber(), first.getAccountNumber());

        assertEquals(1, customer.getNumberOfAccounts());
        assertFalse(customer.getAccounts().contains(second));
    }

    @Test
    public void testMergeEmptyAccount() {
        Customer customer = new Customer(TEST_SSN);
        BankAccount first = customer.getAccount(0);
        BankAccount second = customer.openAccount();
        first.deposit(100);

        customer.mergeAccounts(second.getAccountNumber(), first.getAccountNumber());

        assertEquals(100, first.getBalance(), 0.01);
        assertEquals(1, customer.getNumberOfAccounts());
    }

    @Test
    public void testMergeAccountIntoItselfThrows() {
        Customer customer = new Customer(TEST_SSN);
        String number = customer.getAccount(0).getAccountNumber();

        assertThrows(IllegalArgumentException.class,
                () -> customer.mergeAccounts(number, number));
        assertEquals(1, customer.getNumberOfAccounts());
    }

    @Test
    public void testMergeWithAnotherCustomersAccountThrows() {
        Customer customer = new Customer(TEST_SSN);
        Customer otherCustomer = new Customer("234-56-7890");
        BankAccount mine = customer.getAccount(0);
        BankAccount theirs = otherCustomer.getAccount(0);
        theirs.deposit(500);

        assertThrows(IllegalArgumentException.class,
                () -> customer.mergeAccounts(theirs.getAccountNumber(), mine.getAccountNumber()));
        assertEquals(0, mine.getBalance(), 0.01);
        assertEquals(500, theirs.getBalance(), 0.01);
    }
}
