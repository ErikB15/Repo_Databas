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
                case 1: productDAO.showAllProducts();{
                    break;
                }
                case 2: orderDAO.placeOrder(loggedUserID, menu);{
                    break;
                }
                case 3: orderDAO.showUserOrders(loggedUserID);{
                    break;
                }
                case 4: customerDAO.showCustomerInfo(loggedUserID);{
                    break;
                }
                case 0: running = false;{
                    break;
                }
                default: System.out.println("Invalid input. Try again");{
                    break;
                }
            }
        }
    }
    private void adminSession() throws Exception {
        boolean running = true;
        while (running) {
            int choice = menu.showAdminMenu();
            switch (choice) {
                case 1: productDAO.showAllProducts();{
                    break;
                }
                case 2: productDAO.addProduct(menu.readString("Product name: "), menu.readString("Article Number: "), menu.readInt("Stock: "), menu.readInt("Price: "), menu.readString("Supplier ID: "));{
                    break;
                }
                case 3: productDAO.deleteProduct(menu.readInt("Product ID to delete: "));{
                    break;
                }
                case 4: productDAO.editQuantity(menu.readInt("Product ID: "), menu.readInt("New quantity: "));{
                    break;
                }
                case 5: orderDAO.showAllOrders();{
                    break;
                }
                case 0: running = false;{
                    break;
                }
                default: System.out.println("Invalid input");{
                    break;
                }
            }
        }
    }
}
