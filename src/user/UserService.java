package user;

import java.io.*;
import java.util.*;
import utils.SearchAlgorithms;

public class UserService {
    private final List<User> users;
    private final String CSV_FILE = "users.csv";
    private static int customerIdCounter = 1;
    
    public UserService() {
        this.users = new ArrayList<>();
        loadUsersFromCsv();
    }
    
    public boolean registerUser(String username, String password, String role, String email) {
        // Check if username already exists
        if (findUserByUsername(username) != null) {
            return false; // Username already exists
        }
        
        User newUser = new User(username, password, role.toUpperCase(), email);
        users.add(newUser);
        saveUsersToCSV();
        return true;
    }
    
    public User authenticateUser(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // Authentication failed
    }
    
    public User findUserByUsername(String username) {
        // Using linear search for username lookup
        return SearchAlgorithms.linearSearchUsers(users, username);
    }
    
    public List<User> findUsersByRole(String role) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equalsIgnoreCase(role)) {
                result.add(user);
            }
        }
        return result;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public boolean updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                saveUsersToCSV();
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteUser(String username) {
        boolean removed = users.removeIf(user -> user.getUsername().equals(username));
        if (removed) {
            saveUsersToCSV();
        }
        return removed;
    }
    
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = findUserByUsername(username);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            saveUsersToCSV();
            return true;
        }
        return false;
    }
    
    // Customer management methods
    public User createCustomer(String name, String email, String address) {
        String customerId = "C" + String.format("%03d", customerIdCounter++);
        String username = "customer_" + customerId.toLowerCase();
        String password = "temp123"; // Default password, user should change it
        
        User customer = new User(username, password, "USER", email, address, customerId);
        users.add(customer);
        saveUsersToCSV();
        return customer;
    }
    
    public User findCustomerById(String customerId) {
        for (User user : users) {
            if (customerId.equals(user.getCustomerId())) {
                return user;
            }
        }
        return null;
    }
    
    public List<User> findCustomersByName(String name) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.isCustomer() && user.getUsername().toLowerCase().contains(name.toLowerCase())) {
                result.add(user);
            }
        }
        return result;
    }
    
    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();
        for (User user : users) {
            if (user.isCustomer()) {
                customers.add(user);
            }
        }
        return customers;
    }
    
    public void updateCustomer(User customer) {
        updateUser(customer);
    }
    
    public void removeCustomer(String customerId) {
        users.removeIf(user -> customerId.equals(user.getCustomerId()));
        saveUsersToCSV();
    }
    
    public String generateCustomerId() {
        return "C" + String.format("%03d", customerIdCounter++);
    }
    
    private void loadUsersFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromCsvString(line);
                if (user != null) {
                    users.add(user);
                }
            }
            initializeCustomerIdCounter();
        } catch (IOException e) {
            System.out.println("No existing users file found. Creating default users.");
            initializeDefaultUsers();
        }
    }
    
    private void initializeCustomerIdCounter() {
        int maxId = 0;
        for (User user : users) {
            String customerId = user.getCustomerId();
            if (customerId != null && customerId.startsWith("C") && customerId.length() > 1) {
                try {
                    int numericId = Integer.parseInt(customerId.substring(1));
                    maxId = Math.max(maxId, numericId);
                } catch (NumberFormatException e) {
                    // Skip non-numeric IDs
                }
            }
        }
        customerIdCounter = maxId + 1;
    }
    
    private void saveUsersToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            for (User user : users) {
                writer.println(user.toCsvString());
            }
        } catch (IOException e) {
            System.err.println("Error saving users to CSV: " + e.getMessage());
        }
    }
    
    private void initializeDefaultUsers() {
        // Create default admin user
        registerUser("admin", "admin123", "ADMIN", "admin@bookstore.com");
        
        // Create a sample regular user
        registerUser("user", "user123", "USER", "user@example.com");
        
        // Create sample customers
        createCustomer("John Doe", "john.doe@email.com", "123 Main St, City, State");
        createCustomer("Jane Smith", "jane.smith@email.com", "456 Oak Ave, City, State");
        createCustomer("Bob Johnson", "bob.johnson@email.com", "789 Pine Rd, City, State");
        
        System.out.println("Default users created:");
        System.out.println("Admin - Username: admin, Password: admin123");
        System.out.println("User - Username: user, Password: user123");
        System.out.println("Sample customers created with default password: temp123");
    }
}