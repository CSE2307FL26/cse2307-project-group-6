package main;

import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 4;
	  private static final int MAX_SELECTION = 4;

    private BankAccount userAccount;
    private Scanner keyboardInput;

    public MainMenu() {
        this.userAccount = new BankAccount();
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");

        System.out.println("1. Make a deposit");
        System.out.println("2. View transaction history");
        System.out.println("3. Collect a fee (admin)");
        System.out.println("4. Exit the app");

    }

    public int getUserSelection(int max) {
        int selection = -1;
        while(selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
        }
        return selection;
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1:
                performDeposit();
                break;
            case 2:
                viewTransactionHistory();
            case 3
                performCollectFee();
                break;
        }
    }

    public void performDeposit() {
        double depositAmount = -1;
        while(depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextInt();
        }
        userAccount.deposit(depositAmount);
    }

    public void viewTransactionHistory() {
        List<BankAccount.Transaction> history = userAccount.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        System.out.println("Transaction history:");
        for (int i = 0; i < history.size(); i++) {
            System.out.printf("%d. Deposit: $%.2f%n", i + 1, history.get(i).getAmount());
        }
    }

    public void performCollectFee() {
        System.out.print("Enter fee amount to collect: ");
        double feeAmount = keyboardInput.nextDouble();
        try {
            userAccount.collectFee(feeAmount);
            System.out.println("Collected $" + feeAmount + " fee. New balance: $" + userAccount.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Fee collection failed: " + e.getMessage());
        }
    }

    public void run() {
        int selection = -1;
        while(selection != EXIT_SELECTION) {
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
