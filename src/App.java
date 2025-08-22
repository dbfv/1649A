import book.*;
import java.util.Scanner;
import order.*;
import user.*;

public class App {
    private static BookService bookService;
    private static OrderService orderService;
    private static UserService userService;
    private static AuthenticationService authService;
    private static BookController bookController;
    private static OrderController orderController;
    private static UserController userController;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to Online Bookstore Order Processing System ===");
        
        // Initialize services
        scanner = new Scanner(System.in);
        bookService = new BookService();
        userService = new UserService();
        authService = new AuthenticationService(userService);
        orderService = new OrderService(bookService, userService);
        
        // Initialize controllers
        bookController = new BookController(bookService, scanner);
        orderController = new OrderController(orderService, bookService, userService, scanner);
        userController = new UserController(userService, authService, scanner);
        
        // Start with authentication
        if (userController.showLoginMenu()) {
            // User successfully logged in, show main menu
            showMainMenu();
        }
        
        scanner.close();
        System.out.println("Thank you for using the Online Bookstore System!");
    }
    
    private static void showMainMenu() {
        while (true) {
            User currentUser = authService.getCurrentUser();
            
            // Check if user is still logged in
            if (currentUser == null) {
                System.out.println("Session expired. Please log in again.");
                return;
            }
            
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("Logged in as " + currentUser.getUsername());
            System.out.println();
            
            if (authService.isCurrentUserAdmin()) {
                System.out.println("1. Admin Menu");
                System.out.println("2. User Menu");
                System.out.println("3. Logout");
                System.out.println("4. Exit");
            } else {
                System.out.println("1. User Menu");
                System.out.println("2. Logout");
                System.out.println("3. Exit");
            }
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            if (authService.isCurrentUserAdmin()) {
                switch (choice) {
                    case 1 -> showAdminMenu();
                    case 2 -> showUserMenu();
                    case 3 -> {
                        authService.logout();
                        System.out.println("Logged out successfully!");
                        return;
                    }
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } else {
                switch (choice) {
                    case 1 -> showUserMenu();
                    case 2 -> {
                        authService.logout();
                        System.out.println("Logged out successfully!");
                        return;
                    }
                    case 3 -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
    
    private static void showAdminMenu() {
        if (!authService.hasAdminAccess()) {
            System.out.println("Access denied! Admin privileges required.");
            return;
        }
        
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Book Management");
            System.out.println("2. Customer Management");
            System.out.println("3. Order Management");
            System.out.println("4. User Management");
            System.out.println("5. View System Statistics");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1 -> bookController.showBookMenu();
                case 2 -> userController.showCustomerManagementMenu();
                case 3 -> orderController.showOrderMenu();
                case 4 -> userController.showUserManagementMenu();
                case 5 -> showSystemStatistics();
                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void showUserMenu() {
        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. Browse Books");
            System.out.println("2. Search Books");
            System.out.println("3. Place Order");
            System.out.println("4. Track Single Order");
            System.out.println("5. View My Order History");
            System.out.println("6. Update Profile");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1 -> browseBooksUser();
                case 2 -> searchBooksUser();
                case 3 -> placeOrderUser();
                case 4 -> trackSingleOrderUser();
                case 5 -> viewOrderHistoryUser();
                case 6 -> updateUserProfile();
                case 7 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void browseBooksUser() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              AVAILABLE BOOKS                                ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        
        java.util.List<Book> availableBooks = bookService.getAllBooks().stream()
            .filter(book -> book.getQuantity() > 0)
            .collect(java.util.stream.Collectors.toList());
            
        if (availableBooks.isEmpty()) {
            System.out.println("║                           No books available                                ║");
        } else {
            System.out.printf("║ %-6s │ %-25s │ %-20s │ %-8s │ %-5s ║%n", "ID", "Title", "Author", "Price", "Stock");
            System.out.println("╠════════┼═══════════════════════════┼══════════════════════┼══════════┼═══════╣");
            
            for (Book book : availableBooks) {
                String title = book.getTitle().length() > 25 ? book.getTitle().substring(0, 22) + "..." : book.getTitle();
                String author = book.getAuthor().length() > 20 ? book.getAuthor().substring(0, 17) + "..." : book.getAuthor();
                System.out.printf("║ %-6s │ %-25s │ %-20s │ $%-7.2f │ %-5d ║%n", 
                    book.getId(), title, author, book.getPrice(), book.getQuantity());
            }
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
    }
    
    private static void searchBooksUser() {
        System.out.println("\n=== Search Books ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Go Back");
        System.out.print("Choose search type: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1 -> {
                System.out.print("Enter title (or 'back' to return): ");
                String title = scanner.nextLine();
                if (title.equalsIgnoreCase("back")) {
                    return;
                }
                java.util.List<Book> titleResults = bookService.findBooksByTitle(title);
                if (titleResults.isEmpty()) {
                    System.out.println("\n┌─────────────────────────────────────────────────────────┐");
                    System.out.println("│  No books found with title containing: " + String.format("%-15s", title) + "│");
                    System.out.println("└─────────────────────────────────────────────────────────┘");
                } else {
                    System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
                    System.out.println("║                          SEARCH RESULTS - TITLE                             ║");
                    System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
                    System.out.printf("║ %-6s │ %-25s │ %-20s │ %-8s │ %-5s ║%n", "ID", "Title", "Author", "Price", "Stock");
                    System.out.println("╠════════┼═══════════════════════════┼══════════════════════┼══════════┼═══════╣");
                    
                    for (Book book : titleResults) {
                        String bookTitle = book.getTitle().length() > 25 ? book.getTitle().substring(0, 22) + "..." : book.getTitle();
                        String author = book.getAuthor().length() > 20 ? book.getAuthor().substring(0, 17) + "..." : book.getAuthor();
                        System.out.printf("║ %-6s │ %-25s │ %-20s │ $%-7.2f │ %-5d ║%n", 
                            book.getId(), bookTitle, author, book.getPrice(), book.getQuantity());
                    }
                    System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
                }
            }
            case 2 -> {
                System.out.print("Enter author (or 'back' to return): ");
                String author = scanner.nextLine();
                if (author.equalsIgnoreCase("back")) {
                    return;
                }
                java.util.List<Book> authorResults = bookService.findBooksByAuthor(author);
                if (authorResults.isEmpty()) {
                    System.out.println("\n┌─────────────────────────────────────────────────────────┐");
                    System.out.println("│  No books found by author containing: " + String.format("%-15s", author) + "│");
                    System.out.println("└─────────────────────────────────────────────────────────┘");
                } else {
                    System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
                    System.out.println("║                          SEARCH RESULTS - AUTHOR                            ║");
                    System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
                    System.out.printf("║ %-6s │ %-25s │ %-20s │ %-8s │ %-5s ║%n", "ID", "Title", "Author", "Price", "Stock");
                    System.out.println("╠════════┼═══════════════════════════┼══════════════════════┼══════════┼═══════╣");
                    
                    for (Book book : authorResults) {
                        String title = book.getTitle().length() > 25 ? book.getTitle().substring(0, 22) + "..." : book.getTitle();
                        String bookAuthor = book.getAuthor().length() > 20 ? book.getAuthor().substring(0, 17) + "..." : book.getAuthor();
                        System.out.printf("║ %-6s │ %-25s │ %-20s │ $%-7.2f │ %-5d ║%n", 
                            book.getId(), title, bookAuthor, book.getPrice(), book.getQuantity());
                    }
                    System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
                }
            }
            case 3 -> {
                return;
            }
            default -> System.out.println("Invalid option.");
        }
    }
    
    private static void placeOrderUser() {
        // Get the currently logged-in user
        User currentUser = authService.getCurrentUser();
        
        // Check if user is still logged in
        if (currentUser == null) {
            System.out.println("Session expired. Please log in again.");
            return;
        }
        
        User orderingUser = null;
        
        // Check if current user has profile information for orders
        if (currentUser.isCustomer()) {
            orderingUser = currentUser;
            System.out.println("Using your profile: " + currentUser.getUsername());
        } else {
            System.out.println("You need to complete your profile first to place orders.");
            System.out.print("Would you like to complete your profile now? (y/n): ");
            String register = scanner.nextLine();
            if (register.toLowerCase().startsWith("y")) {
                // Complete user profile for ordering
                orderingUser = completeUserProfile(currentUser);
                if (orderingUser == null) {
                    System.out.println("Failed to complete profile. Please try again.");
                    return;
                }
            } else {
                return;
            }
        }
        
        System.out.println("Welcome " + orderingUser.getUsername() + "!");
        
        // Show available books first
        browseBooksUser();
        
        // Collect order items
        java.util.List<OrderItem> items = new java.util.ArrayList<>();
        
        while (true) {
            // Show current cart if not empty
            if (!items.isEmpty()) {
                System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                              SHOPPING CART                                  ║");
                System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
                System.out.printf("║ %-35s │ %-8s │ %-10s │ %-10s ║%n", "Book Title", "Quantity", "Unit Price", "Subtotal");
                System.out.println("╠═════════════════════════════════════┼══════════┼════════════┼════════════╣");
                
                double cartTotal = 0;
                for (OrderItem item : items) {
                    String title = item.getBook().getTitle().length() > 35 ? 
                        item.getBook().getTitle().substring(0, 32) + "..." : item.getBook().getTitle();
                    System.out.printf("║ %-35s │ %-8d │ $%-9.2f │ $%-9.2f ║%n", 
                        title, item.getQuantity(), item.getBook().getPrice(), item.getTotalPrice());
                    cartTotal += item.getTotalPrice();
                }
                System.out.println("╠═════════════════════════════════════┴══════════┴════════════┼════════════╣");
                System.out.printf("║ %-65s │ $%-9.2f ║%n", "CART TOTAL", cartTotal);
                System.out.println("╚══════════════════════════════════════════════════════════════┴════════════╝");
            }
            
            System.out.print("\nEnter book ID to add to cart (or 'done' to finish, 'show' to see books, 'back' to cancel): ");
            String bookId = scanner.nextLine();
            
            if (bookId.equalsIgnoreCase("done")) {
                break;
            }
            
            if (bookId.equalsIgnoreCase("back")) {
                System.out.println("Order cancelled.");
                return;
            }
            
            if (bookId.equalsIgnoreCase("show")) {
                browseBooksUser();
                continue;
            }
            
            Book book = bookService.findBookById(bookId);
            if (book == null) {
                System.out.println("Book not found! Please try again.");
                continue;
            }
            
            if (book.getQuantity() <= 0) {
                System.out.println("Sorry, this book is out of stock!");
                continue;
            }
            
            System.out.println("Selected: " + book.getTitle() + " by " + book.getAuthor());
            System.out.println("Price: $" + String.format("%.2f", book.getPrice()) + " | Available: " + book.getQuantity());
            System.out.print("Enter quantity: ");
            
            int quantity;
            try {
                quantity = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } catch (Exception e) {
                System.out.println("Invalid quantity!");
                scanner.nextLine(); // consume invalid input
                continue;
            }
            
            if (quantity <= 0) {
                System.out.println("Invalid quantity!");
                continue;
            }
            
            if (!bookService.isBookAvailable(bookId, quantity)) {
                System.out.println("Not enough books in stock! Available: " + book.getQuantity());
                continue;
            }
            
            items.add(new OrderItem(book, quantity));
            System.out.println("Added to cart: " + book.getTitle() + " x " + quantity + 
                             " = $" + String.format("%.2f", book.getPrice() * quantity));
            
            // Show current cart total
            double cartTotal = items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
            System.out.println("Current cart total: $" + String.format("%.2f", cartTotal));
        }
        
        if (items.isEmpty()) {
            System.out.println("No items in cart. Order cancelled.");
            return;
        }
        
        // Show order summary
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              ORDER SUMMARY                                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-35s │ %-8s │ %-10s │ %-10s ║%n", "Book Title", "Quantity", "Unit Price", "Subtotal");
        System.out.println("╠═════════════════════════════════════┼══════════┼════════════┼════════════╣");
        
        double total = 0;
        for (OrderItem item : items) {
            String title = item.getBook().getTitle().length() > 35 ? 
                item.getBook().getTitle().substring(0, 32) + "..." : item.getBook().getTitle();
            System.out.printf("║ %-35s │ %-8d │ $%-9.2f │ $%-9.2f ║%n", 
                title, item.getQuantity(), item.getBook().getPrice(), item.getTotalPrice());
            total += item.getTotalPrice();
        }
        System.out.println("╠═════════════════════════════════════┴══════════┴════════════┼════════════╣");
        System.out.printf("║ %-65s │ $%-9.2f ║%n", "TOTAL AMOUNT", total);
        System.out.println("╚══════════════════════════════════════════════════════════════┴════════════╝");
        
        System.out.print("Confirm order? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.toLowerCase().startsWith("y")) {
            try {
                String orderId = orderService.createOrder(orderingUser.getCustomerId(), items);
                System.out.println("\n🎉 Order placed successfully!");
                System.out.println("Order ID: " + orderId);
                System.out.println("Total: $" + String.format("%.2f", total));
                System.out.println("You can track your order using the Order ID.");
            } catch (RuntimeException e) {
                System.out.println("Error placing order: " + e.getMessage());
            }
        } else {
            System.out.println("Order cancelled.");
        }
    }
    
    private static User completeUserProfile(User user) {
        System.out.println("\n=== Complete Your Profile ===");
        System.out.println("Adding address information to your account...");
        System.out.print("Enter your address (or 'back' to cancel): ");
        String address = scanner.nextLine();
        if (address.equalsIgnoreCase("back")) {
            return null;
        }
        
        // Add customer ID and address to existing user without changing username
        String customerId = userService.generateCustomerId();
        user.setAddress(address);
        user.setCustomerId(customerId);
        userService.updateUser(user);
        
        System.out.println("✓ Profile completed successfully!");
        System.out.println("Your user ID is: " + customerId);
        System.out.println("You can now place orders!");
        
        return user;
    }
    
    
    private static void trackSingleOrderUser() {
        // Get the currently logged-in user
        User currentUser = authService.getCurrentUser();
        
        // Check if user is still logged in
        if (currentUser == null) {
            System.out.println("Session expired. Please log in again.");
            return;
        }
        
        // Check if current user has complete profile
        if (!currentUser.isCustomer()) {
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│  You need to complete your profile to track orders.    │");
            System.out.println("│  Please complete your profile first.                   │");
            System.out.println("└─────────────────────────────────────────────────────────┘");
            return;
        }
        
        System.out.print("Enter your order ID (or 'back' to return): ");
        String orderId = scanner.nextLine();
        if (orderId.equalsIgnoreCase("back")) {
            return;
        }
        
        Order order = orderService.findOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found!");
        } else {
            // Verify that this order belongs to the current user
            if (!order.getCustomer().getCustomerId().equals(currentUser.getCustomerId())) {
                System.out.println("Access denied! This order does not belong to you.");
                return;
            }
            
            System.out.println("\n=== Order Status ===");
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("User: " + order.getCustomer().getName());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Order Date: " + order.getOrderDate());
            System.out.println("Total: $" + String.format("%.2f", order.getTotalAmount()));
            
            System.out.println("\nItems in this order:");
            for (OrderItem item : order.getItems()) {
                System.out.println("- " + item.getBook().getTitle() + 
                                 " by " + item.getBook().getAuthor() +
                                 " x " + item.getQuantity() + 
                                 " = $" + String.format("%.2f", item.getTotalPrice()));
            }
        }
    }
    
    private static void viewOrderHistoryUser() {
        // Get the currently logged-in user
        User currentUser = authService.getCurrentUser();
        
        // Check if user is still logged in
        if (currentUser == null) {
            System.out.println("Session expired. Please log in again.");
            return;
        }
        
        // Check if current user has complete profile
        if (!currentUser.isCustomer()) {
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│  You need to complete your profile to view order       │");
            System.out.println("│  history. Please complete your profile first.          │");
            System.out.println("└─────────────────────────────────────────────────────────┘");
            return;
        }
        
        User user = currentUser;
        System.out.println("Viewing order history for: " + user.getUsername() + " (ID: " + user.getCustomerId() + ")");
        
        java.util.List<Order> userOrders = orderService.findOrdersByCustomerName(user.getName());
        
        if (userOrders.isEmpty()) {
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│  No orders found for " + String.format("%-30s", user.getName()) + "│");
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                           ORDER HISTORY - " + String.format("%-25s", user.getName()) + "║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ %-10s │ %-16s │ %-12s │ %-10s │ %-8s ║%n", 
                "Order ID", "Date", "Status", "Total", "Items");
            System.out.println("╠════════════┼══════════════════┼══════════════┼════════════┼══════════╣");
            
            double totalSpent = 0;
            for (Order order : userOrders) {
                String dateStr = order.getOrderDate().toString().substring(0, 16);
                System.out.printf("║ %-10s │ %-16s │ %-12s │ $%-9.2f │ %-8d ║%n", 
                    order.getOrderId(), dateStr, order.getStatus(), 
                    order.getTotalAmount(), order.getItems().size());
                
                if (order.getStatus().equals("COMPLETED")) {
                    totalSpent += order.getTotalAmount();
                }
            }
            
            System.out.println("╠════════════┴══════════════════┴══════════════┴════════════┴══════════╣");
            System.out.printf("║ Total Orders: %-3d                    Total Spent: $%-9.2f        ║%n", 
                userOrders.size(), totalSpent);
            System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private static void updateUserProfile() {
        User currentUser = authService.getCurrentUser();
        
        // Check if user is still logged in
        if (currentUser == null) {
            System.out.println("Session expired. Please log in again.");
            return;
        }
        
        System.out.println("\n=== Update Profile ===");
        System.out.println("Current profile information:");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Address: " + (currentUser.getAddress().isEmpty() ? "Not set" : currentUser.getAddress()));
        System.out.println("Profile Status: " + (currentUser.isCustomer() ? "Complete" : "Incomplete"));
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Email");
        System.out.println("2. Address");
        System.out.println("3. Password");
        System.out.println("4. Complete Profile (add address for ordering)");
        System.out.println("5. Back");
        System.out.print("Choose an option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1 -> {
                System.out.print("Enter new email (or 'back' to cancel): ");
                String newEmail = scanner.nextLine();
                if (!newEmail.equalsIgnoreCase("back") && !newEmail.trim().isEmpty()) {
                    currentUser.setEmail(newEmail);
                    userService.updateUser(currentUser);
                    System.out.println("✓ Email updated successfully!");
                }
            }
            case 2 -> {
                System.out.print("Enter new address (or 'back' to cancel): ");
                String newAddress = scanner.nextLine();
                if (!newAddress.equalsIgnoreCase("back") && !newAddress.trim().isEmpty()) {
                    currentUser.setAddress(newAddress);
                    if (!currentUser.isCustomer()) {
                        // If user doesn't have customer ID, add one without changing username
                        String customerId = userService.generateCustomerId();
                        currentUser.setAddress(newAddress);
                        currentUser.setCustomerId(customerId);
                        userService.updateUser(currentUser);
                        System.out.println("✓ Address added and profile completed!");
                        System.out.println("Your user ID is: " + customerId);
                    } else {
                        userService.updateUser(currentUser);
                        System.out.println("✓ Address updated successfully!");
                    }
                }
            }
            case 3 -> {
                System.out.print("Enter current password: ");
                String oldPassword = scanner.nextLine();
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                if (userService.changePassword(currentUser.getUsername(), oldPassword, newPassword)) {
                    System.out.println("✓ Password changed successfully!");
                } else {
                    System.out.println("✗ Failed to change password. Current password is incorrect.");
                }
            }
            case 4 -> {
                if (currentUser.isCustomer()) {
                    System.out.println("Your profile is already complete!");
                } else {
                    User completedUser = completeUserProfile(currentUser);
                    if (completedUser != null) {
                        System.out.println("✓ Profile completed! You can now place orders.");
                    }
                }
            }
            case 5 -> {
                return;
            }
            default -> System.out.println("Invalid option.");
        }
    }
    
    private static void showSystemStatistics() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              SYSTEM STATISTICS                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-40s │ %-10s ║%n", "Metric", "Count");
        System.out.println("╠══════════════════════════════════════════┼════════════╣");
        System.out.printf("║ %-40s │ %-10d ║%n", "Total Books", bookService.getAllBooks().size());
        System.out.printf("║ %-40s │ %-10d ║%n", "Total Customers", userService.getAllCustomers().size());
        System.out.printf("║ %-40s │ %-10d ║%n", "Total Orders", orderService.getAllOrders().size());
        System.out.println("╠══════════════════════════════════════════┼════════════╣");
        System.out.printf("║ %-40s │ %-10d ║%n", "Pending Orders", orderService.findOrdersByStatus("PENDING").size());
        System.out.printf("║ %-40s │ %-10d ║%n", "Processing Orders", orderService.findOrdersByStatus("PROCESSING").size());
        System.out.printf("║ %-40s │ %-10d ║%n", "Completed Orders", orderService.findOrdersByStatus("COMPLETED").size());
        System.out.printf("║ %-40s │ %-10d ║%n", "Cancelled Orders", orderService.findOrdersByStatus("CANCELLED").size());
        System.out.printf("║ %-40s │ %-10d ║%n", "Orders in Queue", orderService.getOrderQueue().size());
        System.out.println("╚══════════════════════════════════════════┴════════════╝");
    }
}
