import book.*;
import customer.*;
import java.util.Scanner;
import order.*;

public class App {
    private static BookService bookService;
    private static CustomerService customerService;
    private static OrderService orderService;
    private static BookController bookController;
    private static CustomerController customerController;
    private static OrderController orderController;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to Online Bookstore Order Processing System ===");
        
        // Initialize services
        scanner = new Scanner(System.in);
        bookService = new BookService();
        customerService = new CustomerService();
        orderService = new OrderService(bookService, customerService);
        
        // Initialize controllers
        bookController = new BookController(bookService, scanner);
        customerController = new CustomerController(customerService, scanner);
        orderController = new OrderController(orderService, bookService, customerService, scanner);
        
        // Start main menu
        showMainMenu();
        
        scanner.close();
        System.out.println("Thank you for using the Online Bookstore System!");
    }
    
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Admin Menu");
            System.out.println("2. User Menu");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    showAdminMenu();
                    break;
                case 2:
                    showUserMenu();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void showAdminMenu() {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Book Management");
            System.out.println("2. Customer Management");
            System.out.println("3. Order Management");
            System.out.println("4. View System Statistics");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    bookController.showBookMenu();
                    break;
                case 2:
                    customerController.showCustomerMenu();
                    break;
                case 3:
                    orderController.showOrderMenu();
                    break;
                case 4:
                    showSystemStatistics();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
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
            System.out.println("6. Register as New Customer");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    browseBooksUser();
                    break;
                case 2:
                    searchBooksUser();
                    break;
                case 3:
                    placeOrderUser();
                    break;
                case 4:
                    trackSingleOrderUser();
                    break;
                case 5:
                    viewOrderHistoryUser();
                    break;
                case 6:
                    registerNewCustomerStandalone();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
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
            case 1:
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
                break;
            case 2:
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
                        title = book.getTitle().length() > 25 ? book.getTitle().substring(0, 22) + "..." : book.getTitle();
                        String bookAuthor = book.getAuthor().length() > 20 ? book.getAuthor().substring(0, 17) + "..." : book.getAuthor();
                        System.out.printf("║ %-6s │ %-25s │ %-20s │ $%-7.2f │ %-5d ║%n", 
                            book.getId(), title, bookAuthor, book.getPrice(), book.getQuantity());
                    }
                    System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
                }
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    private static void placeOrderUser() {
        System.out.print("Enter your customer ID (or 'back' to return): ");
        String customerId = scanner.nextLine();
        if (customerId.equalsIgnoreCase("back")) {
            return;
        }
        
        Customer customer = customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer ID not found! Would you like to register? (y/n): ");
            String register = scanner.nextLine();
            if (register.toLowerCase().startsWith("y")) {
                registerNewCustomerWithId(customerId);
                customer = customerService.findCustomerById(customerId);
                if (customer == null) return;
            } else {
                return;
            }
        }
        
        System.out.println("Welcome " + customer.getName() + "!");
        
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
                String orderId = orderService.createOrder(customerId, items);
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
    
    private static void registerNewCustomerWithId(String customerId) {
        System.out.println("\n=== Customer Registration ===");
        System.out.println("Customer ID: " + customerId);
        System.out.print("Enter your name (or 'back' to return): ");
        String name = scanner.nextLine();
        if (name.equalsIgnoreCase("back")) {
            return;
        }
        System.out.print("Enter your email (or 'back' to return): ");
        String email = scanner.nextLine();
        if (email.equalsIgnoreCase("back")) {
            return;
        }
        System.out.print("Enter your address (or 'back' to return): ");
        String address = scanner.nextLine();
        if (address.equalsIgnoreCase("back")) {
            return;
        }
        
        Customer customer = new Customer(customerId, name, email, address);
        customerService.addCustomer(customer);
        System.out.println("Registration successful! You can now place orders.");
    }
    
    private static void trackSingleOrderUser() {
        System.out.print("Enter your order ID (or 'back' to return): ");
        String orderId = scanner.nextLine();
        if (orderId.equalsIgnoreCase("back")) {
            return;
        }
        
        Order order = orderService.findOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found!");
        } else {
            System.out.println("\n=== Order Status ===");
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Customer: " + order.getCustomer().getName());
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
        System.out.print("Enter your customer ID (or 'back' to return): ");
        String customerId = scanner.nextLine();
        if (customerId.equalsIgnoreCase("back")) {
            return;
        }
        
        Customer customer = customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│         Customer not found!        │");
            System.out.println("└─────────────────────────────────────┘");
            return;
        }
        
        java.util.List<Order> customerOrders = orderService.findOrdersByCustomerName(customer.getName());
        
        if (customerOrders.isEmpty()) {
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│  No orders found for " + String.format("%-30s", customer.getName()) + "│");
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                           ORDER HISTORY - " + String.format("%-25s", customer.getName()) + "║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ %-10s │ %-16s │ %-12s │ %-10s │ %-8s ║%n", 
                "Order ID", "Date", "Status", "Total", "Items");
            System.out.println("╠════════════┼══════════════════┼══════════════┼════════════┼══════════╣");
            
            double totalSpent = 0;
            for (Order order : customerOrders) {
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
                customerOrders.size(), totalSpent);
            System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private static void registerNewCustomerStandalone() {
        System.out.println("\n=== New Customer Registration ===");
        System.out.print("Enter desired customer ID (or 'back' to return): ");
        String id = scanner.nextLine();
        if (id.equalsIgnoreCase("back")) {
            return;
        }
        
        // Check if customer ID already exists
        if (customerService.findCustomerById(id) != null) {
            System.out.println("Customer ID already exists! Please choose a different ID.");
            return;
        }
        
        System.out.print("Enter your name (or 'back' to return): ");
        String name = scanner.nextLine();
        if (name.equalsIgnoreCase("back")) {
            return;
        }
        System.out.print("Enter your email (or 'back' to return): ");
        String email = scanner.nextLine();
        if (email.equalsIgnoreCase("back")) {
            return;
        }
        System.out.print("Enter your address (or 'back' to return): ");
        String address = scanner.nextLine();
        if (address.equalsIgnoreCase("back")) {
            return;
        }
        
        Customer customer = new Customer(id, name, email, address);
        customerService.addCustomer(customer);
        System.out.println("Registration successful!");
        System.out.println("Your customer ID is: " + id);
        System.out.println("You can now place orders using this ID.");
    }
    
    private static void showSystemStatistics() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              SYSTEM STATISTICS                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-40s │ %-10s ║%n", "Metric", "Count");
        System.out.println("╠══════════════════════════════════════════┼════════════╣");
        System.out.printf("║ %-40s │ %-10d ║%n", "Total Books", bookService.getAllBooks().size());
        System.out.printf("║ %-40s │ %-10d ║%n", "Total Customers", customerService.getAllCustomers().size());
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
