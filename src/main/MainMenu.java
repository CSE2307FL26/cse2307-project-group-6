package main;

import java.util.List;
import java.util.Scanner;

public class MainMenu {
    
   
    private static final int EXIT_SELECTION = 10;
    private static final int MAX_SELECTION = 10;

    private BankAccount userAccount;
    private BankAccount otherAccount;
    private Scanner keyboardInput;
    private Customer customer;
    
  
  
    public MainMenu() {
        this.customer = new Customer();
        this.userAccount = customer.getAccounts().get(0);
        this.userAccount = new BankAccount();
        this.otherAccount = new BankAccount();
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("1. Make a deposit");
        System.out.println("5. Check account balance");
        System.out.println("4. Transfer to another account");
        System.out.println("2. View transaction history");
        System.out.println("3. Collect a fee (admin)");
        System.out.println("6. Add interest payment (Administrator)");
        System.out.println("7. Make a withdrawal");
        System.out.println("8. Open a new account");
        System.out.println("9. Close an account");
        System.out.println("10. Exit the app");

    }

    public int getUserSelection(int max) {
    int selection = -1;

    while (selection < 1 || selection > max) {
        System.out.print("Please make a selection: ");
        selection = keyboardInput.nextInt();

        if (selection < 1 || selection > max) {
            System.out.println("Invalid selection. Please try again.");
        }
    }

    return selection;
    }


    public void displayBalance() {
        System.out.printf("Current account balance: $%.2f%n", userAccount.getBalance());
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
              openNewAccount();
              break;
          case 9:
              closeExistingAccount();
              break;
          default:
                System.out.println("Invalid selection. Please try again.");
        }
        for (int i = 0; i < customer.getNumberOfAccounts(); i++) {
            System.out.printf("%d. Account %d - Balance: $%.2f%n", i + 1, i + 1, customer.getAccounts().get(i).getBalance());
        }
        int choice = getUserSelection(customer.getNumberOfAccounts());
        double payout = customer.closeAccount(choice - 1);
        userAccount = customer.getAccounts().get(0);
        System.out.printf("Account closed. $%.2f has been paid out to you.%n", payout);
    }


    public void performDeposit() {
        double depositAmount = -1;
        while(depositAmount <= 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextDouble();
        }
    }

    public void performTransfer() {
        double transferAmount = -1;
        while(transferAmount <= 0) {
            System.out.print("How much would you like to transfer: ");
            transferAmount = keyboardInput.nextDouble();
        }
        try {
            userAccount.transfer(otherAccount, transferAmount);
            System.out.println("Transfer complete.");
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed: insufficient funds.");
        }
        System.out.println("Your balance: " + userAccount.getBalance());
        System.out.println("Other account balance: " + otherAccount.getBalance());
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

    public void performWithdraw() {
        System.out.print("How much would you like to withdraw: ");
        double withdrawAmount = keyboardInput.nextDouble();
        try {
            userAccount.withdraw(withdrawAmount);
            System.out.println("Withdrew $" + withdrawAmount + ". New balance: $" + userAccount.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
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
