package controller;

import view.Menu;
import entity.UserDAO;
import entity.ProductDAO;
import entity.OrderDAO;
import entity.CustomerDAO;


public class Controller {

    private Menu menu = new Menu();
    private UserDAO userDAO;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private CustomerDAO customerDAO;

    private int loggedUserID;
    private int loggedAdminID;
    private boolean isAdmin;

    public Controller() throws Exception {
        userDAO = new UserDAO();
        productDAO = new ProductDAO();
        orderDAO = new OrderDAO();
        customerDAO = new CustomerDAO();
    }

    public void start() throws Exception {
        boolean running = true;

        while (running) {
            int choice = menu.showMainMenu();

            switch (choice) {
                case 1:
                    productDAO.showAllProducts();
                    break;

                case 2:
                    String searchterm = menu.readString("Search for products: ");
                    productDAO.searchProducts(searchterm);
                    break;

                case 3:
                    if (userDAO.loginUser(menu)) {
                        loggedUserID = userDAO.getLoggedUserID();
                        isAdmin = false;
                        userSession();
                    }
                    break;

                case 4:
                    if (userDAO.loginAdmin(menu)) {
                        loggedAdminID = userDAO.getLoggedAdminID();
                        isAdmin = true;
                        adminSession();
                    }
                    break;

                case 5:
                    userDAO.registerUser(menu);
                    System.out.println("Please login after registration.");
                    break;

                case 6:
                    userDAO.registerAdmin(menu);
                    System.out.println("Please login after registration.");
                    break;

                case 0:
                    System.out.println("Exiting program...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid input. Try again.");
            }
        }
    }

    private void userSession() throws Exception {
        boolean running = true;

        while (running) {
            int choice = menu.showUserMenu();

            switch (choice) {
                case 1:
                    productDAO.showAllProducts();
                    break;

                case 2:
                    System.out.println("--- Ordering ---");
                    int AN = menu.readInt("Article number of product: ");
                    int a = menu.readInt("Amount: ");
                    String addResult = orderDAO.addToCart(loggedUserID, AN, a);
                    System.out.println(addResult);
                    break;

                case 3:
                    checkoutMenu(loggedUserID, menu);
                    break;

                case 4:
                    orderDAO.showUserOrders(loggedUserID);
                    break;

                case 5:
                    customerDAO.showCustomerInfo(loggedUserID);
                    break;
                case 6:
                    String searchterm = menu.readString("Search for products: ");
                    productDAO.searchProducts(searchterm);
                    break;

                case 7:
                    productDAO.showCurrentDiscounts();
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid input. Try again.");
            }
        }
    }

    private void adminSession() throws Exception {
        boolean running = true;

        while (running) {
            int choice = menu.showAdminMenu();

            switch (choice) {
                case 1:
                    productDAO.showAllProducts();
                    break;

                case 2:
                    String productName = menu.readString("Product name: ");
                    int articleNumber = menu.readInt("Article Number: ");
                    int stock = menu.readInt("Stock: ");
                    int price = menu.readInt("Price: ");
                    int supplierID = menu.readInt("Supplier id: ");
                    productDAO.addProduct(productName, articleNumber, stock, price, supplierID);
                    break;

                case 3:
                    productDAO.deleteProduct(menu);
                    break;

                case 4:
                    int editID = menu.readInt("Product ID: ");
                    int newQuantity = menu.readInt("New quantity: ");
                    productDAO.editQuantity(editID, newQuantity);
                    break;

                case 5:
                    orderDAO.showAllOrders();
                    break;

                case 6:
                    String supplierName = menu.readString("Supplier's name: ");
                    String phoneNumber = menu.readString("Phone number (07...): ");
                    int supplierId = menu.readInt("Supplier id (ex. 123...): ");
                    String address = menu.readString("Address: ");
                    productDAO.addSupplier(supplierName, phoneNumber, supplierId, address);
                    break;

                case 7:
                    productDAO.showAllSuppliers();
                    break;

                case 8:
                    int orderID = menu.readInt("Enter order ID: ");
                    int ARchoice = menu.readInt("1 = Approve, 2 = Reject: ");
                    boolean approve = ARchoice == 1;
                    String result = orderDAO.handleOrder(orderID, approve);
                    System.out.println(result);
                    break;

                case 9:
                    String searchterm = menu.readString("Enter product name, article number or supplier: ");
                    productDAO.searchProducts(searchterm);
                    break;

                case 10:
                    orderDAO.maxOrders();
                    break;

                case 11:
                    String code = menu.readString("Discount code: ");
                    String codeName = menu.readString("Discount name: ");
                    int percentage = menu.readInt("Discount percentage (0-100): ");
                    String reason = menu.readString("Reason for discount: ");
                    String addDiscountResult = productDAO.addDiscount(code, codeName, percentage, reason);
                    System.out.println(addDiscountResult);
                    break;

                case 12:
                    productDAO.showAllProductsBySupplier();
                    String articleInput = menu.readString("Article number of product to assign discount: ");
                    int articleNum = Integer.parseInt(articleInput);
                    String dCode = menu.readString("Discount code to assign: ");
                    String from = menu.readString("Start date (YYYY-MM-DD): ");
                    String to = menu.readString("End date (YYYY-MM-DD): ");

                    System.out.println(productDAO.assignDiscount(articleNum, dCode, from, to));
                    break;

                case 13:
                    productDAO.showDiscountHistory();
                    break;

                case 14:
                    String updCode = menu.readString("Discount code to update: ");
                    int updProductID = menu.readInt("Article number: ");
                    int newPercentage = menu.readInt("New discount percentage (0-100): ");
                    String newFromDate = menu.readString("New start date (YYYY-MM-DD): ");
                    String newToDate = menu.readString("New end date (YYYY-MM-DD): ");
                    String updateResult = productDAO.updateDiscount(updCode, updProductID, newPercentage, newFromDate, newToDate);
                    System.out.println(updateResult);
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid input. Try again.");
            }
        }
    }


    public void checkoutMenu(int customerID, Menu menu) {
        orderDAO.showCart(customerID);

        System.out.println("1. Confirm order");
        System.out.println("2. Cancel order");
        System.out.println("3. Continue shopping");

        int choice = menu.readInt("Choose option: ");

        switch (choice) {
            case 1 -> System.out.println(orderDAO.confirmCheckout(customerID));
            case 2 -> System.out.println(orderDAO.cancelCart(customerID));
            case 3 -> System.out.println("Continuing shopping...");
            default -> System.out.println("Invalid choice");
        }
    }
}