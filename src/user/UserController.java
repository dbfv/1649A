package user;

import java.util.List;
import java.util.Scanner;

public class UserController {
    private final UserService userService;
    private final AuthenticationService authService;
    private final Scanner scanner;
    
    public UserController(UserService userService, AuthenticationService authService, Scanner scanner) {
        this.userService = userService;
        this.authService = authService;
        this.scanner = scanner;
    }
    
    public boolean showLoginMenu() {
        while (true) {
            System.out.println("\n=== LOGIN MENU ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1 -> {
                    if (performLogin()) {
                        return true; // Successful login
                    }
                }
                case 2 -> registerNewUser();
                case 3 -> {
                    return false; // Exit application
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private boolean performLogin() {
        System.out.println("\n=== USER LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty!");
            return false;
        }
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (authService.login(username, password)) {
            User currentUser = authService.getCurrentUser();
            System.out.println("\n✓ Login successful!");
            System.out.println("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            return true;
        } else {
            System.out.println("\n✗ Login failed! Invalid username or password.");
            return false;
        }
    }
    
    private void registerNewUser() {
        System.out.println("\n=== USER REGISTRATION ===");
        
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty!");
            return;
        }
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (password.length() < 3) {
            System.out.println("Password must be at least 3 characters long!");
            return;
        }
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println("Email cannot be empty!");
            return;
        }
        
        // All new registrations are regular users (not admins)
        String role = "USER";
        if (userService.registerUser(username, password, role, email)) {
            System.out.println("\n✓ User registered successfully!");
            System.out.println("You can now login with your credentials.");
        } else {
            System.out.println("\n✗ Registration failed! Username already exists.");
        }
    }
    
    public void showUserManagementMenu() {
        if (!authService.hasAdminAccess()) {
            System.out.println("Access denied! Admin privileges required.");
            return;
        }
        
        while (true) {
            System.out.println("\n=== USER MANAGEMENT ===");
            System.out.println("1. View All Users");
            System.out.println("2. Search User by Username");
            System.out.println("3. View Users by Role");
            System.out.println("4. Delete User");
            System.out.println("5. Change User Password");
            System.out.println("6. Back to Admin Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1 -> viewAllUsers();
                case 2 -> searchUserByUsername();
                case 3 -> viewUsersByRole();
                case 4 -> deleteUser();
                case 5 -> changeUserPassword();
                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void viewAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                 ALL USERS                                   ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-15s │ %-10s │ %-25s │ %-20s ║%n", "Username", "Role", "Username", "Email");
        System.out.println("╠═════════════════┼════════════┼═══════════════════════════┼══════════════════════╣");
        
        for (User user : users) {
            String username = user.getUsername().length() > 25 ? user.getUsername().substring(0, 22) + "..." : user.getUsername();
            String email = user.getEmail().length() > 20 ? user.getEmail().substring(0, 17) + "..." : user.getEmail();
            System.out.printf("║ %-15s │ %-10s │ %-25s │ %-20s ║%n", 
                user.getUsername(), user.getRole(), username, email);
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
    }
    
    private void searchUserByUsername() {
        System.out.print("Enter username to search: ");
        String username = scanner.nextLine().trim();
        
        User user = userService.findUserByUsername(username);
        if (user != null) {
            System.out.println("\n✓ User found:");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Role: " + user.getRole());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
        } else {
            System.out.println("\n✗ User not found with username: " + username);
        }
    }
    
    private void viewUsersByRole() {
        System.out.println("Select role to filter:");
        System.out.println("1. ADMIN");
        System.out.println("2. USER");
        System.out.print("Choose role (1-2): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        String role = (choice == 1) ? "ADMIN" : "USER";
        List<User> users = userService.findUsersByRole(role);
        
        if (users.isEmpty()) {
            System.out.println("No users found with role: " + role);
            return;
        }
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            USERS WITH ROLE: " + String.format("%-8s", role) + "                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-15s │ %-25s │ %-30s ║%n", "Username", "Username", "Email");
        System.out.println("╠═════════════════┼═══════════════════════════┼════════════════════════════════╣");
        
        for (User user : users) {
            String username = user.getUsername().length() > 25 ? user.getUsername().substring(0, 22) + "..." : user.getUsername();
            String email = user.getEmail().length() > 30 ? user.getEmail().substring(0, 27) + "..." : user.getEmail();
            System.out.printf("║ %-15s │ %-25s │ %-30s ║%n", 
                user.getUsername(), username, email);
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
    }
    
    private void deleteUser() {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine().trim();
        
        if (username.equals(authService.getCurrentUsername())) {
            System.out.println("✗ Cannot delete your own account!");
            return;
        }
        
        User user = userService.findUserByUsername(username);
        if (user == null) {
            System.out.println("✗ User not found with username: " + username);
            return;
        }
        
        System.out.print("Are you sure you want to delete user '" + username + "'? (y/N): ");
        String confirmation = scanner.nextLine().trim();
        
        if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
            if (userService.deleteUser(username)) {
                System.out.println("✓ User deleted successfully!");
            } else {
                System.out.println("✗ Failed to delete user.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void changeUserPassword() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        
        User user = userService.findUserByUsername(username);
        if (user == null) {
            System.out.println("✗ User not found with username: " + username);
            return;
        }
        
        System.out.print("Enter current password: ");
        String oldPassword = scanner.nextLine();
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        if (newPassword.length() < 3) {
            System.out.println("✗ New password must be at least 3 characters long!");
            return;
        }
        
        if (userService.changePassword(username, oldPassword, newPassword)) {
            System.out.println("✓ Password changed successfully!");
        } else {
            System.out.println("✗ Failed to change password. Current password is incorrect.");
        }
    }
    
    public void showCustomerManagementMenu() {
        if (!authService.hasAdminAccess()) {
            System.out.println("Access denied! Admin privileges required.");
            return;
        }
        
        while (true) {
            System.out.println("\n=== CUSTOMER MANAGEMENT ===");
            System.out.println("1. View All Customers");
            System.out.println("2. Search Customer by ID");
            System.out.println("3. Search Customers by Name");
            System.out.println("4. Add New Customer");
            System.out.println("5. Update Customer");
            System.out.println("6. Remove Customer");
            System.out.println("7. Back to Admin Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1 -> viewAllCustomers();
                case 2 -> searchCustomerById();
                case 3 -> searchCustomersByName();
                case 4 -> addNewCustomer();
                case 5 -> updateCustomer();
                case 6 -> removeCustomer();
                case 7 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void viewAllCustomers() {
        List<User> customers = userService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│         No customers found         │");
            System.out.println("└─────────────────────────────────────┘");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                  ALL CUSTOMERS                                      ║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ %-8s │ %-20s │ %-25s │ %-30s ║%n", "ID", "Name", "Email", "Address");
            System.out.println("╠══════════┼══════════════════════┼═══════════════════════════┼════════════════════════════════╣");
            
            for (User customer : customers) { 
                String name = customer.getUsername().length() > 20 ? customer.getUsername().substring(0, 17) + "..." : customer.getUsername();
                String email = customer.getEmail().length() > 25 ? customer.getEmail().substring(0, 22) + "..." : customer.getEmail();
                String address = customer.getAddress().length() > 30 ? customer.getAddress().substring(0, 27) + "..." : customer.getAddress();
                System.out.printf("║ %-8s │ %-20s │ %-25s │ %-30s ║%n", 
                    customer.getCustomerId(), name, email, address);
            }
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private void searchCustomerById() {
        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine();
        User customer = userService.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Customer not found!");
        } else {
            System.out.println("\n=== Customer Found ===");
            System.out.println("ID: " + customer.getCustomerId());
            System.out.println("Name: " + customer.getUsername());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("Address: " + customer.getAddress());
            System.out.println("Username: " + customer.getUsername());
        }
    }
    
    private void searchCustomersByName() {
        System.out.print("Enter customer name to search: ");
        String name = scanner.nextLine();
        List<User> customers = userService.findCustomersByName(name);
        
        if (customers.isEmpty()) {
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│  No customers found with name containing: " + String.format("%-13s", name) + "│");
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                              SEARCH RESULTS - CUSTOMERS                             ║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ %-8s │ %-20s │ %-25s │ %-30s ║%n", "ID", "Name", "Email", "Address");
            System.out.println("╠══════════┼══════════════════════┼═══════════════════════════┼════════════════════════════════╣");
            
            for (User customer : customers) {
                String customerName = customer.getUsername().length() > 20 ? customer.getUsername().substring(0, 17) + "..." : customer.getUsername();
                String email = customer.getEmail().length() > 25 ? customer.getEmail().substring(0, 22) + "..." : customer.getEmail();
                String address = customer.getAddress().length() > 30 ? customer.getAddress().substring(0, 27) + "..." : customer.getAddress();
                System.out.printf("║ %-8s │ %-20s │ %-25s │ %-30s ║%n", 
                    customer.getCustomerId(), customerName, email, address);
            }
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private void addNewCustomer() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        
        User customer = userService.createCustomer(name, email, address);
        System.out.println("Customer added successfully!");
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Username: " + customer.getUsername());
        System.out.println("Default Password: temp123 (customer should change this)");
    }
    
    private void updateCustomer() {
        System.out.print("Enter customer ID to update: ");
        String customerId = scanner.nextLine();
        User customer = userService.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        
        System.out.println("Current customer: " + customer.getUsername());
        System.out.print("Enter new username (or press Enter to keep current): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            customer.setUsername(name);
        }
        
        System.out.print("Enter new email (or press Enter to keep current): ");
        String email = scanner.nextLine();
        if (!email.trim().isEmpty()) {
            customer.setEmail(email);
        }
        
        System.out.print("Enter new address (or press Enter to keep current): ");
        String address = scanner.nextLine();
        if (!address.trim().isEmpty()) {
            customer.setAddress(address);
        }
        
        userService.updateCustomer(customer);
        System.out.println("Customer updated successfully!");
    }
    
    private void removeCustomer() {
        System.out.print("Enter customer ID to remove: ");
        String customerId = scanner.nextLine();
        
        User customer = userService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        
        System.out.print("Are you sure you want to remove customer '" + customer.getUsername() + "'? (y/N): ");
        String confirmation = scanner.nextLine().trim();
        
        if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
            userService.removeCustomer(customerId);
            System.out.println("Customer removed successfully!");
        } else {
            System.out.println("Removal cancelled.");
        }
    }
}