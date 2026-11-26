package main;

import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 10;
    private static final String SAMPLE_CUSTOMER_SSN = "234-56-7890";

    private final Bank bank;
    private final Scanner keyboardInput;

    public MainMenu() {
        keyboardInput = new Scanner(System.in);
        bank = new Bank(readSsn());
        BankAccount sampleAccount = bank.openAccountForOtherCustomer(SAMPLE_CUSTOMER_SSN);
        System.out.printf("(Sample customer account for testing transfers: #%s)%n",
                sampleAccount.getAccountNumber());
    }

    private String readSsn() {
        while (true) {
            System.out.print("Enter your SSN (123-45-6789): ");
            String input = keyboardInput.nextLine();
            if (BankAccount.isValidSsn(input)) {
                return input;
            }
            System.out.println("Invalid SSN. Please try again.");
        }
    }

    public void displayOptions() {
        System.out.println("\nWelcome to the 237 Bank App!");
        System.out.println("1. Make a deposit");
        System.out.println("2. View transaction history");
        System.out.println("3. Collect a fee (Administrator)");
        System.out.println("4. Transfer to another account");
        System.out.println("5. Check account balance");
        System.out.println("6. Add interest payment (Administrator)");
        System.out.println("7. Make a withdrawal");
        System.out.println("8. Open a new account");
        System.out.println("9. Close an account");
        System.out.println("10. Exit the app");
    }

    public int getUserSelection(int maxSelection) {
        return readSelection(maxSelection, "Please make a selection: ");
    }

    private int readSelection(int maxSelection, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int selection = Integer.parseInt(keyboardInput.nextLine().trim());
                if (selection >= 1 && selection <= maxSelection) {
                    return selection;
                }
            } catch (NumberFormatException exception) {
                // Invalid input is handled below.
            }
            System.out.println("Invalid selection. Please try again.");
        }
    }

    private Double readNumber(String prompt) {
        System.out.print(prompt);
        try {
            double number = Double.parseDouble(keyboardInput.nextLine().trim());
            if (Double.isFinite(number)) {
                return number;
            }
        } catch (NumberFormatException exception) {
            // Invalid input is handled below.
        }
        System.out.println("Please enter a valid number.");
        return null;
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1 -> performDeposit();
            case 2 -> viewTransactionHistory();
            case 3 -> performCollectFee();
            case 4 -> performTransfer();
            case 5 -> displayBalance();
            case 6 -> performAddInterest();
            case 7 -> performWithdraw();
            case 8 -> openNewAccount();
            case 9 -> closeExistingAccount();
            case 10 -> System.out.println("Thank you for using the 237 Bank App!");
            default -> System.out.println("Invalid selection.");
        }
    }

    public void performDeposit() {
        BankAccount account = selectAccount("Which account would you like to deposit into?");
        Double amount = readNumber("How much would you like to deposit: ");
        if (amount == null) return;

        try {
            account.deposit(amount);
            printBalance(account);
        } catch (IllegalArgumentException exception) {
            printFailure("Deposit", exception);
        }
    }

    public void performWithdraw() {
        BankAccount account = selectAccount("Which account would you like to withdraw from?");
        Double amount = readNumber("How much would you like to withdraw: ");
        if (amount == null) return;

        try {
            account.withdraw(amount);
            printBalance(account);
        } catch (IllegalArgumentException exception) {
            printFailure("Withdrawal", exception);
        }
    }

    public void performTransfer() {
        System.out.println("Where would you like to transfer to?");
        System.out.println("1. Another one of your accounts");
        System.out.println("2. Someone else's account");
        int choice = readSelection(2, "Please make a selection: ");

        if (choice == 1) {
            transferBetweenOwnAccounts();
        } else {
            transferToSomeoneElse();
        }
    }

    private void transferBetweenOwnAccounts() {
        if (bank.getNumberOfAccounts() < 2) {
            System.out.println("You need at least two accounts to make a transfer.");
            return;
        }

        BankAccount source = selectAccount("Which account would you like to transfer from?");
        BankAccount destination = selectAccount("Which account would you like to transfer to?");
        if (source == destination) {
            System.out.println("Transfer failed: cannot transfer to the same account.");
            return;
        }

        Double amount = readNumber("How much would you like to transfer: ");
        if (amount == null) return;

        try {
            source.transfer(destination, amount);
            System.out.println("Transfer complete.");
        } catch (IllegalArgumentException exception) {
            printFailure("Transfer", exception);
        }
        System.out.printf("From account balance: $%.2f%n", source.getBalance());
        System.out.printf("To account balance: $%.2f%n", destination.getBalance());
    }

    private void transferToSomeoneElse() {
        BankAccount source = selectAccount("Which account would you like to transfer from?");
        System.out.print("Enter the recipient's account number: ");
        String destinationNumber = keyboardInput.nextLine().trim();

        Double amount = readNumber("How much would you like to transfer: ");
        if (amount == null) return;

        try {
            bank.transferToAccountNumber(source, destinationNumber, amount);
            System.out.printf("Transfer to account #%s complete.%n", destinationNumber);
        } catch (IllegalArgumentException exception) {
            printFailure("Transfer", exception);
        }
        printBalance(source);
    }

    public void performCollectFee() {
        BankAccount account = selectAccount("Which account should the fee be collected from?");
        Double amount = readNumber("Enter fee amount to collect: ");
        if (amount == null) return;

        try {
            account.collectFee(amount);
            printBalance(account);
        } catch (IllegalArgumentException exception) {
            printFailure("Fee collection", exception);
        }
    }

    public void performAddInterest() {
        BankAccount account = selectAccount("Which account should receive the interest payment?");
        Double interestRate = readNumber("Enter interest rate (%): ");
        if (interestRate == null) return;

        try {
            account.addInterest(interestRate);
            printBalance(account);
        } catch (IllegalArgumentException exception) {
            printFailure("Interest payment", exception);
        }
    }

    public void displayBalance() {
        BankAccount account = selectAccount("Which account's balance would you like to check?");
        printBalance(account);
    }

    private void printBalance(BankAccount account) {
        System.out.printf("Account #%s balance: $%.2f%n",
                account.getAccountNumber(), account.getBalance());
    }

    public void viewTransactionHistory() {
        BankAccount account = selectAccount("Which account's history would you like to view?");
        List<BankAccount.Transaction> history = account.getTransactionHistory();

        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        System.out.println("Transaction history:");
        System.out.printf("%-14s %12s %14s%n", "Type", "Amount", "Balance");
        for (BankAccount.Transaction transaction : history) {
            String sign = transaction.getType().isCredit() ? "+" : "-";
            System.out.printf("%-14s %12s %14s%n",
                transaction.getType().getLabel(),
                String.format("%s$%.2f", sign, transaction.getAmount()),
                String.format("$%.2f", transaction.getBalanceAfter()));
        }
    }

    public void openNewAccount() {
        BankAccount newAccount = bank.openAccount();
        System.out.printf("New account opened! Account #%s (SSN %s)%n",
                newAccount.getAccountNumber(), newAccount.getMaskedSsn());
        System.out.printf("You now have %d accounts.%n",
                bank.getNumberOfAccounts());
    }

    public void closeExistingAccount() {
        int accountCount = bank.getNumberOfAccounts();

        if (accountCount == 1) {
            System.out.println("You cannot close your only account.");
            return;
        }

        displayAccounts();
        int accountNumber = readSelection(
                accountCount, "Select the account number to close: ");

        closeSelectedAccount(accountNumber - 1);
    }

    private BankAccount selectAccount(String prompt) {
        int accountCount = bank.getNumberOfAccounts();
        if (accountCount == 1) {
            return bank.getAccount(0);
        }

        System.out.println(prompt);
        displayAccounts();
        int accountNumber = readSelection(accountCount, "Select an account number: ");
        return bank.getAccount(accountNumber - 1);
    }

    private void displayAccounts() {
        for (int index = 0; index < bank.getNumberOfAccounts(); index++) {
            BankAccount account = bank.getAccount(index);
            System.out.printf("%d. Account #%s  Balance: $%.2f%n",
                    index + 1, account.getAccountNumber(), account.getBalance());
        }
    }

    private void closeSelectedAccount(int accountIndex) {
        try {
            double payout = bank.closeAccount(accountIndex);
            System.out.printf("Account closed. Payout: $%.2f%n", payout);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            printFailure("Account closure", exception);
        }
    }

    private void printFailure(String operation, Exception exception) {
        System.out.println(operation + " failed: " + exception.getMessage());
    }

    public void run() {
        int selection;
        do {
            displayOptions();
            selection = getUserSelection(EXIT_SELECTION);
            processInput(selection);
        } while (selection != EXIT_SELECTION);
    }

    public static void main(String[] args) {
        new MainMenu().run();
    }
}