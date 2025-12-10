package com.expensetracker.app;

import com.expensetracker.service.ExpenseTrackerService;

import java.util.Scanner;

public class ExpenseTrackerApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ExpenseTrackerService trackerService = new ExpenseTrackerService();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> trackerService.addNewUser();
                case 2 -> trackerService.addTransaction();
                case 3 -> trackerService.viewUserTransactions();
                case 4 -> trackerService.generateUserReport();
                case 5 -> trackerService.searchTransactions();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.println("Thank you for using the Expense Tracker!");
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Expense Tracker Menu ===");
        System.out.println("1. Add New User");
        System.out.println("2. Add Transaction");
        System.out.println("3. View User Transactions");
        System.out.println("4. Generate Financial Report");
        System.out.println("5. Search Transactions");
        System.out.println("6. Exit");
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}