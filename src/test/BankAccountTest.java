package test;

import main.BankAccount;
import main.BankAccount.Transaction;
import main.BankAccount.TransactionType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

    private static final String TEST_SSN = "123-45-6789";

    @Test
    public void depositIsRecordedWithType() {
        BankAccount account = new BankAccount();
        account.deposit(50);

        Transaction t = account.getTransactionHistory().get(0);
        assertEquals(TransactionType.DEPOSIT, t.getType());
        assertEquals(50, t.getAmount(), 0.01);
        assertEquals(50, t.getBalanceAfter(), 0.01);
    }

    @Test
    public void withdrawalIsRecordedInHistory() {
        BankAccount account = new BankAccount();
        account.deposit(100);
        account.withdraw(30);

        List<Transaction> history = account.getTransactionHistory();
        assertEquals(2, history.size());
        Transaction t = history.get(1);
        assertEquals(TransactionType.WITHDRAWAL, t.getType());
        assertEquals(30, t.getAmount(), 0.01);
        assertEquals(-30, t.getSignedAmount(), 0.01);
        assertEquals(70, t.getBalanceAfter(), 0.01);
    }

    @Test
    public void feeIsRecordedInHistory() {
        BankAccount account = new BankAccount();
        account.deposit(100);
        account.collectFee(5);

        Transaction t = account.getTransactionHistory().get(1);
        assertEquals(TransactionType.FEE, t.getType());
        assertEquals(5, t.getAmount(), 0.01);
        assertEquals(95, t.getBalanceAfter(), 0.01);
    }

    @Test
    public void interestIsRecordedInHistory() {
        BankAccount account = new BankAccount();
        account.deposit(1000);
        account.addInterest(5);

        Transaction t = account.getTransactionHistory().get(1);
        assertEquals(TransactionType.INTEREST, t.getType());
        assertEquals(50, t.getAmount(), 0.01);
        assertEquals(1050, t.getBalanceAfter(), 0.01);
    }

    @Test
    public void zeroInterestIsNotRecorded() {
        BankAccount account = new BankAccount();
        account.deposit(1000);
        account.addInterest(0);
        assertEquals(1, account.getTransactionHistory().size());
    }

    @Test
    public void interestOnEmptyAccountIsNotRecorded() {
        BankAccount account = new BankAccount();
        account.addInterest(5);
        assertTrue(account.getTransactionHistory().isEmpty());
    }

    @Test
    public void transferIsRecordedOnBothAccounts() {
        BankAccount sender = new BankAccount();
        BankAccount receiver = new BankAccount();
        sender.deposit(100);
        sender.transfer(receiver, 40);

        Transaction out = sender.getTransactionHistory().get(1);
        assertEquals(TransactionType.TRANSFER_OUT, out.getType());
        assertEquals(40, out.getAmount(), 0.01);
        assertEquals(60, out.getBalanceAfter(), 0.01);

        List<Transaction> receiverHistory = receiver.getTransactionHistory();
        assertEquals(1, receiverHistory.size());
        Transaction in = receiverHistory.get(0);
        assertEquals(TransactionType.TRANSFER_IN, in.getType());
        assertEquals(40, in.getAmount(), 0.01);
        assertEquals(40, in.getBalanceAfter(), 0.01);
    }

    @Test
    public void historyKeepsTransactionsInOrder() {
        BankAccount account = new BankAccount();
        BankAccount other = new BankAccount();
        account.deposit(200);
        account.withdraw(20);
        account.transfer(other, 30);
        account.collectFee(10);
        account.addInterest(10);

        List<Transaction> history = account.getTransactionHistory();
        assertEquals(5, history.size());
        assertEquals(TransactionType.DEPOSIT, history.get(0).getType());
        assertEquals(TransactionType.WITHDRAWAL, history.get(1).getType());
        assertEquals(TransactionType.TRANSFER_OUT, history.get(2).getType());
        assertEquals(TransactionType.FEE, history.get(3).getType());
        assertEquals(TransactionType.INTEREST, history.get(4).getType());
        assertEquals(154, history.get(4).getBalanceAfter(), 0.01);
    }

    @Test
    public void lastBalanceAfterMatchesCurrentBalance() {
        BankAccount account = new BankAccount();
        account.deposit(80);
        account.withdraw(15.25);
        account.collectFee(2.50);

        List<Transaction> history = account.getTransactionHistory();
        assertEquals(account.getBalance(),
                history.get(history.size() - 1).getBalanceAfter(), 0.001);
    }

    @Test
    public void creditAndDebitTypesAreLabeledCorrectly() {
        assertTrue(TransactionType.DEPOSIT.isCredit());
        assertTrue(TransactionType.TRANSFER_IN.isCredit());
        assertTrue(TransactionType.INTEREST.isCredit());
        assertFalse(TransactionType.WITHDRAWAL.isCredit());
        assertFalse(TransactionType.TRANSFER_OUT.isCredit());
        assertFalse(TransactionType.FEE.isCredit());
    }

    @Test
    public void failedWithdrawalIsNotRecorded() {
        BankAccount account = new BankAccount();
        account.deposit(10);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(50));
        assertEquals(1, account.getTransactionHistory().size());
    }

    @Test
    public void failedFeeIsNotRecorded() {
        BankAccount account = new BankAccount();
        account.deposit(10);
        assertThrows(IllegalArgumentException.class, () -> account.collectFee(50));
        assertEquals(1, account.getTransactionHistory().size());
    }

    @Test
    public void failedTransferIsNotRecordedOnEitherAccount() {
        BankAccount sender = new BankAccount();
        BankAccount receiver = new BankAccount();
        sender.deposit(10);
        assertThrows(IllegalArgumentException.class, () -> sender.transfer(receiver, 50));
        assertEquals(1, sender.getTransactionHistory().size());
        assertTrue(receiver.getTransactionHistory().isEmpty());
    }

    @Test
    public void failedInterestIsNotRecorded() {
        BankAccount account = new BankAccount();
        account.deposit(100);
        assertThrows(IllegalArgumentException.class, () -> account.addInterest(-5));
        assertEquals(1, account.getTransactionHistory().size());
    }

    @Test
    public void historyCopyCannotModifyAccount() {
        BankAccount account = new BankAccount();
        account.deposit(10);
        account.getTransactionHistory().clear();
        assertEquals(1, account.getTransactionHistory().size());
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
}
