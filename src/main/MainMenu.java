
package main;

import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 8;
    private static final int MAX_SELECTION = 8;

    private BankAccount userAccount;
    private BankAccount otherAccount;
    private Scanner keyboardInput;

    public MainMenu() {
        this.userAccount = new BankAccount();
        this.otherAccount = new BankAccount();
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("1. Make a deposit");
        System.out.println("2. View transaction history");
        System.out.println("3. Collect a fee (Administrator)");
        System.out.println("4. Transfer to another account");
        System.out.println("5. Check account balance");
        System.out.println("6. Add interest payment (Administrator)");
        System.out.println("7. Make a withdrawal");
        System.out.println("8. Exit the app");
    }

    public int getUserSelection(int max) {
        while (true) {
            System.out.print("Please make a selection: ");

            String input = keyboardInput.nextLine().trim();

            try {
                int selection = Integer.parseInt(input);

                if (selection >= 1 && selection <= max) {
                    return selection;
                }

                System.out.println("Invalid selection. Please try again.");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1:
                performDeposit();
                break;
            case 2:
                viewTransactionHistory();
                break;
            case 3:
                performCollectFee();
                break;
            case 4:
                performTransfer();
                break;
            case 5:
                displayBalance();
                break;
            case 6:
                performAddInterest();
                break;
            case 7:
                performWithdraw();
                break;
            case 8:
                System.out.println("Thank you for using the 237 Bank App!");
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
        }
    }

    public void displayBalance() {
        System.out.printf(
            "Current account balance: $%.2f%n",
            userAccount.getBalance()
        );
    }

    public void performDeposit() {
        System.out.print("How much would you like to deposit: ");

        try {
            double amount = Double.parseDouble(
                keyboardInput.nextLine().trim()
            );

            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Deposit must be a positive amount.");
                return;
            }

            userAccount.deposit(amount);

            System.out.printf(
                "Deposit successful. New balance: $%.2f%n",
                userAccount.getBalance()
            );

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        } catch (IllegalArgumentException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    public void performTransfer() {
        System.out.print("How much would you like to transfer: ");

        try {
            double amount = Double.parseDouble(
                keyboardInput.nextLine().trim()
            );

            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Transfer amount must be positive.");
                return;
            }

            userAccount.transfer(otherAccount, amount);

            System.out.println("Transfer complete.");

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }

        System.out.printf(
            "Your balance: $%.2f%n",
            userAccount.getBalance()
        );

        System.out.printf(
            "Other account balance: $%.2f%n",
            otherAccount.getBalance()
        );
    }

    public void viewTransactionHistory() {
        List<BankAccount.Transaction> history =
            userAccount.getTransactionHistory();

        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        System.out.println("Transaction history:");

        for (int i = 0; i < history.size(); i++) {
            System.out.printf(
                "%d. Deposit: $%.2f%n",
                i + 1,
                history.get(i).getAmount()
            );
        }
    }

    public void performCollectFee() {
        System.out.print("Enter fee amount to collect: ");

        try {
            double amount = Double.parseDouble(
                keyboardInput.nextLine().trim()
            );

            if (!Double.isFinite(amount)) {
                System.out.println("Invalid fee amount.");
                return;
            }

            userAccount.collectFee(amount);

            System.out.printf(
                "Fee collected. New balance: $%.2f%n",
                userAccount.getBalance()
            );

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        } catch (IllegalArgumentException e) {
            System.out.println("Fee collection failed: " + e.getMessage());
        }
    }

    public void performAddInterest() {
        System.out.print("Enter interest rate (%): ");

        try {
            double interestRate = Double.parseDouble(
                keyboardInput.nextLine().trim()
            );

            userAccount.addInterest(interestRate);

            System.out.printf(
                "Interest added. New balance: $%.2f%n",
                userAccount.getBalance()
            );

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid interest rate.");
        } catch (IllegalArgumentException e) {
            System.out.println("Interest payment failed: " + e.getMessage());
        }
    }

    public void performWithdraw() {
        System.out.print("How much would you like to withdraw: ");

        try {
            double amount = Double.parseDouble(
                keyboardInput.nextLine().trim()
            );

            if (!Double.isFinite(amount)) {
                System.out.println("Invalid withdrawal amount.");
                return;
            }

            userAccount.withdraw(amount);

            System.out.printf(
                "Withdrawal successful. New balance: $%.2f%n",
                userAccount.getBalance()
            );

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        } catch (IllegalArgumentException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    public void run() {
        int selection = -1;

        while (selection != EXIT_SELECTION) {
            displayOptions();
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}
