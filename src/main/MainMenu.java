package main;

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
        System.out.println("2. Check account balance");
        System.out.println("3. Add interest payment (Administrator)");
        System.out.println("4. Exit the app");
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
                displayBalance();
                break;
            case 3:
                performAddInterest();
                break;
            case 4:
                System.out.println("Thank you for using the 237 Bank App!");
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }
    }
    public void displayBalance() {
        System.out.printf("Your current balance is: $%.2f%n", userAccount.getBalance());
    }

    public void performAddInterest() {
        System.out.print("Please enter the interest rate (as a percentage): ");

        try {
            double interestRate = Double.parseDouble(keyboardInput.nextLine().trim());

            userAccount.addInterest(interestRate);

            System.out.printf("Interest added. New balance is: $%.2f%n",userAccount.getBalance());
        } 
        catch (NumberFormatException e) {
            System.out.println("Invalid interest rate. Please enter a number.");
        } 
        catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void performDeposit() {
        System.out.print("Please enter the amount to deposit: ");

        try {
            double amount = Double.parseDouble(keyboardInput.next());

            userAccount.deposit(amount);

            System.out.printf("Deposit successful. New balance is: $%.2f%n",userAccount.getBalance());
        } 
        catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        } 
        catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
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
