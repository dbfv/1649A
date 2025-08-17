package order;

import book.Book;
import book.BookService;
import customer.Customer;
import customer.CustomerService;
import java.io.*;
import java.util.*;
import utils.SearchAlgorithms;
import utils.SortingAlgorithms;
import utils.datastructures.Queue;

public class OrderService {
    private final List<Order> orders;
    private final Queue<Order> orderQueue;
    private final BookService bookService;
    private final CustomerService customerService;
    private final String CSV_FILE = "orders.csv";
    private int orderCounter;
    
    public OrderService(BookService bookService, CustomerService customerService) {
        this.orders = new ArrayList<>();
        this.orderQueue = new Queue<>();
        this.bookService = bookService;
        this.customerService = customerService;
        this.orderCounter = 1;
        loadOrdersFromCsv();
    }
    
    public String createOrder(String customerId, List<OrderItem> items) {
        Customer customer = customerService.findCustomerById(customerId);
        if (customer == null) {
            throw new RuntimeException("Customer not found: " + customerId);
        }
        
        // Check book availability
        for (OrderItem item : items) {
            if (!bookService.isBookAvailable(item.getBook().getId(), item.getQuantity())) {
                throw new RuntimeException("Book not available: " + item.getBook().getTitle());
            }
        }
        
        // Create order
        String orderId = "ORD" + String.format("%04d", orderCounter++);
        Order order = new Order(orderId, customer);
        
        for (OrderItem item : items) {
            order.addItem(item);
            // Update book inventory
            Book book = bookService.findBookById(item.getBook().getId());
            bookService.updateBookQuantity(book.getId(), book.getQuantity() - item.getQuantity());
        }
        
        // Sort books in order by title
        List<Book> booksInOrder = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            booksInOrder.add(item.getBook());
        }
        SortingAlgorithms.insertionSort(booksInOrder, SortingAlgorithms.BookComparators.BY_TITLE);
        
        // Add to queue for processing
        orderQueue.enqueue(order);
        orders.add(order);
        saveOrdersToCSV();
        
        return orderId;
    }
    
    public Order processNextOrder() {
        if (orderQueue.isEmpty()) {
            return null;
        }
        Order order = orderQueue.dequeue();
        order.setStatus("PROCESSING");
        saveOrdersToCSV();
        return order;
    }
    
    public void completeOrder(String orderId) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus("COMPLETED");
            saveOrdersToCSV();
        }
    }
    
    public void cancelOrder(String orderId) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus("CANCELLED");
            // Restore book inventory
            for (OrderItem item : order.getItems()) {
                Book book = bookService.findBookById(item.getBook().getId());
                bookService.updateBookQuantity(book.getId(), book.getQuantity() + item.getQuantity());
            }
            saveOrdersToCSV();
        }
    }
    
    public Order findOrderById(String orderId) {
        return SearchAlgorithms.linearSearchOrderById(orders, orderId);
    }
    
    public List<Order> findOrdersByCustomerName(String customerName) {
        return SearchAlgorithms.linearSearchOrdersByCustomerName(orders, customerName);
    }
    
    public List<Order> findOrdersByStatus(String status) {
        return SearchAlgorithms.searchOrdersByStatus(orders, status);
    }
    
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
    
    public Queue<Order> getOrderQueue() {
        return orderQueue;
    }
    
    private void loadOrdersFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Note: This is a simplified CSV loading for orders
                // In a real implementation, you'd need to handle order items separately
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String orderId = parts[0];
                    String customerId = parts[1];
                    String status = parts[2];
                    // Parse date and total amount as needed
                    
                    Customer customer = customerService.findCustomerById(customerId);
                    if (customer != null) {
                        Order order = new Order(orderId, customer);
                        order.setStatus(status);
                        orders.add(order);
                        
                        // Update order counter
                        try {
                            int orderNum = Integer.parseInt(orderId.substring(3));
                            if (orderNum >= orderCounter) {
                                orderCounter = orderNum + 1;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore parsing errors
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No existing orders file found. Starting with empty order list.");
        }
    }
    
    private void saveOrdersToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            for (Order order : orders) {
                writer.println(order.toCsvString());
            }
        } catch (IOException e) {
            System.err.println("Error saving orders to CSV: " + e.getMessage());
        }
    }
}