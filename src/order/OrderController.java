package order;

import book.Book;
import book.BookService;
import user.User;
import user.UserService;
import utils.datastructures.Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderController {
    private OrderService orderService;
    private BookService bookService;
    private UserService userService;
    private Scanner scanner;
    
    public OrderController(OrderService orderService, BookService bookService, 
                          UserService userService, Scanner scanner) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.userService = userService;
        this.scanner = scanner;
    }
    
    public void showOrderMenu() {
        while (true) {
            System.out.println("\n=== Order Management ===");
            System.out.println("1. Create New Order");
            System.out.println("2. View All Orders");
            System.out.println("3. Track Order by ID");
            System.out.println("4. Search Orders by Customer Name");
            System.out.println("5. Search Orders by Status");
            System.out.println("6. Process Next Order");
            System.out.println("7. Complete Order");
            System.out.println("8. Cancel Order");
            System.out.println("9. View Order Queue");
            System.out.println("10. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1 -> createNewOrder();
                case 2 -> viewAllOrders();
                case 3 -> trackOrderById();
                case 4 -> searchOrdersByCustomerName();
                case 5 -> searchOrdersByStatus();
                case 6 -> processNextOrder();
                case 7 -> completeOrder();
                case 8 -> cancelOrder();
                case 9 -> viewOrderQueue();
                case 10 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void createNewOrder() {
        System.out.print("Enter customer ID (or 'back' to return): ");
        String customerId = scanner.nextLine();
        if (customerId.equalsIgnoreCase("back")) {
            return;
        }
        
        User customer = userService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found! Please add customer first.");
            return;
        }
        
        System.out.println("Customer: " + customer.getName());
        List<OrderItem> items = new ArrayList<>();
        
        while (true) {
            System.out.print("Enter book ID (or 'done' to finish, 'back' to cancel order): ");
            String bookId = scanner.nextLine();
            
            if (bookId.equalsIgnoreCase("done")) {
                break;
            }
            
            if (bookId.equalsIgnoreCase("back")) {
                System.out.println("Order creation cancelled.");
                return;
            }
            
            Book book = bookService.findBookById(bookId);
            if (book == null) {
                System.out.println("Book not found! Please try again.");
                continue;
            }
            
            System.out.println("Book: " + book.getTitle() + " - Available: " + book.getQuantity());
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            if (quantity <= 0) {
                System.out.println("Invalid quantity!");
                continue;
            }
            
            if (!bookService.isBookAvailable(bookId, quantity)) {
                System.out.println("Not enough books in stock!");
                continue;
            }
            
            items.add(new OrderItem(book, quantity));
            System.out.println("Added to order: " + book.getTitle() + " x " + quantity);
        }
        
        if (items.isEmpty()) {
            System.out.println("No items added to order!");
            return;
        }
        
        try {
            String orderId = orderService.createOrder(customerId, items);
            System.out.println("Order created successfully! Order ID: " + orderId);
        } catch (RuntimeException e) {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }
    
    private void viewAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│          No orders found           │");
            System.out.println("└─────────────────────────────────────┘");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                   ALL ORDERS                                        ║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ %-10s │ %-20s │ %-12s │ %-10s │ %-8s │ %-15s ║%n", 
                "Order ID", "Customer", "Status", "Total", "Items", "Date");
            System.out.println("╠════════════┼══════════════════════┼══════════════┼════════════┼══════════┼═════════════════╣");
            
            for (Order order : orders) {
                String customerName = order.getCustomer().getName().length() > 20 ? 
                    order.getCustomer().getName().substring(0, 17) + "..." : order.getCustomer().getName();
                String dateStr = order.getOrderDate().toString().substring(0, 16); // YYYY-MM-DDTHH:MM
                System.out.printf("║ %-10s │ %-20s │ %-12s │ $%-9.2f │ %-8d │ %-15s ║%n", 
                    order.getOrderId(), customerName, order.getStatus(), 
                    order.getTotalAmount(), order.getItems().size(), dateStr);
            }
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private void trackOrderById() {
        System.out.print("Enter order ID (or 'back' to return): ");
        String orderId = scanner.nextLine();
        if (orderId.equalsIgnoreCase("back")) {
            return;
        }
        
        Order order = orderService.findOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found!");
        } else {
            System.out.println("\n=== Order Details ===");
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Customer: " + order.getCustomer().getName());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Order Date: " + order.getOrderDate());
            System.out.println("Total Amount: $" + String.format("%.2f", order.getTotalAmount()));
            System.out.println("\nItems:");
            for (OrderItem item : order.getItems()) {
                System.out.println("- " + item.getBook().getTitle() + 
                                 " x " + item.getQuantity() + 
                                 " = $" + String.format("%.2f", item.getTotalPrice()));
            }
        }
    }
    
    private void searchOrdersByCustomerName() {
        System.out.print("Enter customer name (or 'back' to return): ");
        String customerName = scanner.nextLine();
        if (customerName.equalsIgnoreCase("back")) {
            return;
        }
        
        List<Order> orders = orderService.findOrdersByCustomerName(customerName);
        if (orders.isEmpty()) {
            System.out.println("No orders found for customer: " + customerName);
        } else {
            System.out.println("\n=== Orders for " + customerName + " ===");
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId() + 
                                 " - Status: " + order.getStatus() + 
                                 " - Total: $" + String.format("%.2f", order.getTotalAmount()));
            }
        }
    }
    
    private void searchOrdersByStatus() {
        System.out.print("Enter order status (PENDING/PROCESSING/COMPLETED/CANCELLED) or 'back' to return: ");
        String status = scanner.nextLine();
        if (status.equalsIgnoreCase("back")) {
            return;
        }
        
        List<Order> orders = orderService.findOrdersByStatus(status);
        if (orders.isEmpty()) {
            System.out.println("No orders found with status: " + status);
        } else {
            System.out.println("\n=== Orders with status " + status + " ===");
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId() + 
                                 " - Customer: " + order.getCustomer().getName() + 
                                 " - Total: $" + String.format("%.2f", order.getTotalAmount()));
            }
        }
    }
    
    private void processNextOrder() {
        Order order = orderService.processNextOrder();
        if (order == null) {
            System.out.println("No orders in queue to process.");
        } else {
            System.out.println("Processing order: " + order.getOrderId() + 
                             " for customer: " + order.getCustomer().getName());
        }
    }
    
    private void completeOrder() {
        System.out.print("Enter order ID to complete (or 'back' to return): ");
        String orderId = scanner.nextLine();
        if (orderId.equalsIgnoreCase("back")) {
            return;
        }
        
        Order order = orderService.findOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found!");
        } else {
            orderService.completeOrder(orderId);
            System.out.println("Order " + orderId + " marked as completed!");
        }
    }
    
    private void cancelOrder() {
        System.out.print("Enter order ID to cancel (or 'back' to return): ");
        String orderId = scanner.nextLine();
        if (orderId.equalsIgnoreCase("back")) {
            return;
        }
        
        Order order = orderService.findOrderById(orderId);
        if (order == null) {
            System.out.println("Order not found!");
        } else {
            orderService.cancelOrder(orderId);
            System.out.println("Order " + orderId + " has been cancelled!");
        }
    }
    
    private void viewOrderQueue() {
        Queue<Order> queue = orderService.getOrderQueue();
        if (queue.isEmpty()) {
            System.out.println("Order queue is empty.");
        } else {
            System.out.println("\n=== Order Processing Queue ===");
            System.out.println("Orders waiting to be processed: " + queue.size());
            System.out.println(queue.toString());
        }
    }
}