# 237 Bank App

## Project Overview

The 237 Bank App is a Java command-line banking application. Users interact with a unified menu to perform banking operations, manage accounts, and view account information.

The application uses separate classes for banking operations, customer account management, and the user interface. JUnit tests verify the core functionality.

## 1. User Stories Completed in Iteration 1

Our team implemented the following user stories:

1. A customer should be able to deposit money into a bank account.
2. A customer should be able to check their account balance.
3. A customer should be able to view their transaction history.
4. An administrator should be able to collect a fee from a customer's account.
5. A customer should be able to transfer money between bank accounts.
6. An administrator should be able to add an interest payment to an account.
7. A customer should be able to withdraw money from an account.
8. A customer should be able to open a new bank account.
9. A customer should be able to close an existing bank account.

These features are integrated into a single command-line menu. The application includes input validation and error handling for invalid banking operations.

## 2. User Stories Planned for Iteration 2

Our team is considering the following user stories for the next iteration:

1. A customer should be able to select which of their accounts to use for banking operations.
2. A customer should be able to transfer money between their own accounts.
3. A customer should be able to view a complete transaction history, including deposits, withdrawals, transfers, fees, and interest payments.
4. A customer should be able to save and retrieve account information after restarting the application.
5. An administrator should be able to view summaries of customer accounts.
6. A customer should be able to log in to access their banking information.

These proposed features will be finalized and assigned to team members through our GitHub Kanban board.

## 3. Known Issues and Limitations

The implemented banking operations compile and run, and the application has been tested using JUnit and manual testing.

Current limitations include:

- Transaction history currently records deposits only.
- Transfers currently use a separate destination account rather than allowing customers to select an account from their account list.
- Account information is stored in memory and does not persist between application sessions.
- The application does not currently support user authentication.
- The application uses `double` for monetary values rather than a decimal-based money representation.

These limitations do not prevent the core banking functionality from running.

## 4. Project Structure

```text
cse2307-project-group-6/
├── src/
│   ├── main/
│   │   ├── BankAccount.java
│   │   ├── Customer.java
│   │   └── MainMenu.java
│   └── test/
│       └── BankAccountTest.java
├── test-lib/
├── runApp.sh
└── README.md
```

### Main Classes

- `BankAccount.java`: Handles deposits, withdrawals, transfers, interest payments, fees, account balances, and transaction history.
- `Customer.java`: Manages opening, closing, and accessing customer accounts.
- `MainMenu.java`: Provides the command-line interface and connects the banking features.
- `BankAccountTest.java`: Contains JUnit tests for banking operations and customer account management.

## 5. Requirements

To compile and run the application, you need:

- Java Development Kit (JDK)
- A command-line terminal
- Bash or Git Bash to execute `runApp.sh`

## 6. How to Compile and Run

### Option 1: Run Using the Script

From the root directory of the repository, execute:

```bash
bash runApp.sh
```

The script creates the output directory, compiles the Java source files, and launches the banking application.

### Option 2: Compile and Run Manually

Create the output directory:

```bash
mkdir -p out
```

Compile the Java source files:

```bash
javac -d out src/main/*.java
```

Launch the application:

```bash
java -cp out main.MainMenu
```

The application displays a menu containing the implemented banking operations.

## 7. How to Run Tests

The project uses JUnit to test banking operations and customer account management.

Compile the application and test files:

```bash
javac -cp "test-lib/*" -d out src/main/*.java src/test/*.java
```

Run the JUnit test suite:

```bash
java -jar test-lib/junit-platform-console-standalone-1.13.0-M3.jar execute --class-path out --scan-class-path
```

The test suite covers:

- Valid and invalid deposits
- Account balance calculations
- Transfers between accounts
- Insufficient funds
- Transaction history
- Interest payments
- Fee collection
- Withdrawals
- Opening and closing accounts
- Invalid account operations

During Iteration 1 integration testing, all 39 JUnit tests passed.

## 8. Development Workflow

Our team used GitHub branches, pull requests, and a Kanban board to coordinate development.

Each team member implemented assigned user stories in separate feature branches. Team members reviewed contributions through pull requests before merging changes into `development`.

After integrating the features, we tested the unified application to identify and resolve issues.

The `development` branch serves as the integration branch. The completed iteration is deployed by merging `development` into `main`.

## 9. Future Improvements

Future iterations will focus on improving account management, transaction tracking, and data persistence while maintaining readable, modular code and automated tests.
