package view;

import java.util.Scanner;

public class Menu {
    private Scanner scanner = new Scanner(System.in);

    public int showMainMenu() {
        System.out.println("\n--- BROWSE PRODUCTS---");
        System.out.println("1. Show all products");
        System.out.println("2. Search for product name");
        System.out.println();
        System.out.println("--- TO ORDER, LOGIN TO YOU ACCOUNT ---");
        System.out.println();
        System.out.println("--- LOGIN ---");
        System.out.println("3. Login user");
        System.out.println("4. Login admin");
        System.out.println();
        System.out.println("--- REGISTER ---");
        System.out.println("5. Register user");
        System.out.println("6. Register admin");
        System.out.println();
        System.out.print("What would you like to do?: ");
        return scanner.nextInt();
    }

    public int showUserMenu() {
        System.out.println("\n--- USER MENU ---");
        System.out.println("1. Show all products");
        System.out.println("2. Place order");
        System.out.println("3. Show my orders");
        System.out.println("4. Show my account info");
        System.out.println("0. Logout");
        System.out.print("Choice: ");
        return scanner.nextInt();
    }

    public int showAdminMenu() {
        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("1. Show all products");
        System.out.println("2. Add product");
        System.out.println("3. Delete product");
        System.out.println("4. Edit product quantity");
        System.out.println("5. Show all orders");
        System.out.println("0. Logout");
        System.out.print("Choice: ");
        return scanner.nextInt();
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input, enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        scanner.nextLine();
        return scanner.nextInt();
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.next();
    }
}