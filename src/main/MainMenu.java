package main;

import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 11;

    private final Customer customer;
    private final BankAccount otherAccount;
    private final Scanner keyboardInput;
    private BankAccount userAccount;

    public MainMenu() {
        keyboardInput = new Scanner(System.in);
        customer = new Customer(readSsn());
        userAccount = customer.getAccount(0);
        otherAccount = new BankAccount();
        
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
        System.out.println("10. Merge two accounts");
        System.out.println("11. Exit the app");
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
            case 10 -> mergeTwoAccounts();
            case 11 -> System.out.println("Thank you for using the 237 Bank App!");
            default -> System.out.println("Invalid selection.");
        }
    }

    public void performDeposit() {
        Double amount = readNumber("How much would you like to deposit: ");
        if (amount == null) return;

        try {
            userAccount.deposit(amount);
            displayBalance();
        } catch (IllegalArgumentException exception) {
            printFailure("Deposit", exception);
        }
    }

    public void performWithdraw() {
        Double amount = readNumber("How much would you like to withdraw: ");
        if (amount == null) return;

        try {
            userAccount.withdraw(amount);
            displayBalance();
        } catch (IllegalArgumentException exception) {
            printFailure("Withdrawal", exception);
        }
    }

    public void performTransfer() {
        Double amount = readNumber("How much would you like to transfer: ");
        if (amount == null) return;

        try {
            userAccount.transfer(otherAccount, amount);
            System.out.println("Transfer complete.");
        } catch (IllegalArgumentException exception) {
            printFailure("Transfer", exception);
        }
        displayBalance();
        System.out.printf("Other account balance: $%.2f%n",
                otherAccount.getBalance());
    }

    public void performCollectFee() {
        Double amount = readNumber("Enter fee amount to collect: ");
        if (amount == null) return;

        try {
            userAccount.collectFee(amount);
            displayBalance();
        } catch (IllegalArgumentException exception) {
            printFailure("Fee collection", exception);
        }
    }

    public void performAddInterest() {
        Double interestRate = readNumber("Enter interest rate (%): ");
        if (interestRate == null) return;

        try {
            userAccount.addInterest(interestRate);
            displayBalance();
        } catch (IllegalArgumentException exception) {
            printFailure("Interest payment", exception);
        }
    }

    public void mergeTwoAccounts() {
        int accountCount = customer.getNumberOfAccounts();

        if (accountCount < 2) {
            System.out.println("You need at least two accounts to merge.");
            return;
        }

        displayAccounts();
        int sourceIndex = readSelection(accountCount,
                "Select the account to merge FROM (it will be closed): ") - 1;
        int targetIndex = readSelection(accountCount,
                "Select the account to merge INTO: ") - 1;

        BankAccount source = customer.getAccount(sourceIndex);
        BankAccount target = customer.getAccount(targetIndex);

        try {
            customer.mergeAccounts(source.getAccountNumber(), target.getAccountNumber());
            if (userAccount == source) {
                userAccount = target;
            }
            System.out.printf("Merged account #%s into #%s. New balance: $%.2f%n",
                    source.getAccountNumber(), target.getAccountNumber(),
                    target.getBalance());
        } catch (IllegalArgumentException exception) {
            printFailure("Merge", exception);
        }
    }

    public void displayBalance() {
        System.out.printf("Account #%s balance: $%.2f%n",
                userAccount.getAccountNumber(), userAccount.getBalance());
    }

    public void viewTransactionHistory() {
        List<BankAccount.Transaction> history = userAccount.getTransactionHistory();

        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        System.out.println("Transaction history:");
        for (BankAccount.Transaction transaction : history) {
            System.out.printf("Deposit: $%.2f%n", transaction.getAmount());
        }
    }

    public void openNewAccount() {
        BankAccount newAccount = customer.openAccount();
        System.out.printf("New account opened! Account #%s (SSN %s)%n",
            newAccount.getAccountNumber(), newAccount.getMaskedSsn());
        System.out.printf("You now have %d accounts.%n",
            customer.getNumberOfAccounts());
    }

    public void closeExistingAccount() {
        int accountCount = customer.getNumberOfAccounts();

        if (accountCount == 1) {
            System.out.println("You cannot close your only account.");
            return;
        }

        displayAccounts();
        int accountNumber = readSelection(
                accountCount, "Select which account to close (by list number): ");

        closeSelectedAccount(accountNumber - 1);
    }

    private void displayAccounts() {
        for (int index = 0; index < customer.getNumberOfAccounts(); index++) {
            BankAccount account = customer.getAccount(index);
            System.out.printf("%d. Account #%s  Balance: $%.2f%n",
                    index + 1, account.getAccountNumber(), account.getBalance());
        }
    }

    private void closeSelectedAccount(int accountIndex) {
        try {
            double payout = customer.closeAccount(accountIndex);
            userAccount = customer.getAccount(0);
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