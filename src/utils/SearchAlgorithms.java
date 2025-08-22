package utils;

import order.Order;
import book.Book;
import user.User;
import java.util.List;
import java.util.ArrayList;

public class SearchAlgorithms {
    
    // Linear Search for Orders by Order ID
    public static Order linearSearchOrderById(List<Order> orders, String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                return order;
            }
        }
        return null;
    }
    
    // Linear Search for Orders by Customer Name
    public static List<Order> linearSearchOrdersByCustomerName(List<Order> orders, String customerName) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().getName().toLowerCase().contains(customerName.toLowerCase())) {
                result.add(order);
            }
        }
        return result;
    }
    
    // Linear Search for Books by Title
    public static List<Book> linearSearchBooksByTitle(List<Book> books, String title) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }
    
    // Linear Search for Books by Author
    public static List<Book> linearSearchBooksByAuthor(List<Book> books, String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }
    
    // Binary Search for Books by ID (requires sorted list)
    public static Book binarySearchBookById(List<Book> sortedBooks, String bookId) {
        int left = 0;
        int right = sortedBooks.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = sortedBooks.get(mid).getId().compareToIgnoreCase(bookId);
            
            if (comparison == 0) {
                return sortedBooks.get(mid);
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    
    // Binary Search for Books by Title (requires sorted list)
    public static Book binarySearchBookByTitle(List<Book> sortedBooks, String title) {
        int left = 0;
        int right = sortedBooks.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = sortedBooks.get(mid).getTitle().compareToIgnoreCase(title);
            
            if (comparison == 0) {
                return sortedBooks.get(mid);
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    
    // Search for Customer by ID (now using User)
    public static User searchCustomerById(List<User> customers, String customerId) {
        for (User customer : customers) {
            if (customer.getId() != null && customer.getId().equalsIgnoreCase(customerId)) {
                return customer;
            }
        }
        return null;
    }
    
    // Search for Orders by Status
    public static List<Order> searchOrdersByStatus(List<Order> orders, String status) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus().equalsIgnoreCase(status)) {
                result.add(order);
            }
        }
        return result;
    }
    
    // Linear Search for Users by Username
    public static User linearSearchUsers(List<User> users, String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }
    
    // Search for Users by Role
    public static List<User> searchUsersByRole(List<User> users, String role) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equalsIgnoreCase(role)) {
                result.add(user);
            }
        }
        return result;
    }
}