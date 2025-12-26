package view;

import java.util.Scanner;

public class Menu {
    private Scanner scanner = new Scanner(System.in);

    public int showMainMenu() {
        while (true) {
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
            System.out.println("--- Program ---");
            System.out.println("0. Exit program");
            System.out.println();
            try {
                return readInt("What would you like to do?: ");
            } catch (Exception e) {
                System.out.println("Wrong input, please enter a number shown in menu");
                scanner.nextLine();
            }
        }
    }

    public int showUserMenu() {
        while (true) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Show all products");
            System.out.println("2. Add products to cart");
            System.out.println("3. Checkout");
            System.out.println("4. Show my orders");
            System.out.println("5. Show my account info");
            System.out.println("6. Search for products");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Wrong input, please enter a number shown in menu");
                scanner.nextLine();
            }
        }
    }

    public int showAdminMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Show all products");
            System.out.println("2. Add product");
            System.out.println("3. Delete product");
            System.out.println("4. Edit product quantity");
            System.out.println("5. Show all orders");
            System.out.println("6. Add supplier");
            System.out.println("7. Show all suppliers");
            System.out.println("8. Approve/Reject orders");
            System.out.println("9. Search for products: ");
            System.out.println("10. list of product most orderd in each month");
            System.out.println("0. Logout");
            System.out.print("Choice: ");
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Wrong input, please enter a number shown in menu");
                scanner.nextLine();
                }
            }
        }
    public int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input, enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}