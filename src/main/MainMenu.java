package main;

import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 4;
    private static final int MAX_SELECTION = 4;
    
    private Customer customer;
    private BankAccount userAccount;
    private Scanner keyboardInput;
    
    public MainMenu() {
        this.customer = new Customer();
        this.userAccount = customer.getAccounts().get(0);
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        
        System.out.println("1. Make a deposit");
        System.out.println("2. Open a new account");
        System.out.println("3. Close an account");
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
                openNewAccount();
                break;
            case 3:
                closeExistingAccount();
                break;
            default:
                break;
        }
    }

    public void openNewAccount() {
        customer.openAccount();
        System.out.println("New account opened! You now have " + customer.getNumberOfAccounts() + " accounts.");
    }
    
    public void closeExistingAccount() {
        if (customer.getNumberOfAccounts() == 1) {
            System.out.println("You cannot close your only account.");
            return;
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
        while(depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextInt();
        }
        userAccount.deposit(depositAmount);
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
