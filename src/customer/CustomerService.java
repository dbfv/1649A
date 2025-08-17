package customer;

import utils.SearchAlgorithms;
import java.io.*;
import java.util.*;

public class CustomerService {
    private List<Customer> customers;
    private final String CSV_FILE = "customers.csv";
    
    public CustomerService() {
        this.customers = new ArrayList<>();
        loadCustomersFromCsv();
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomersToCSV();
    }
    
    public void removeCustomer(String customerId) {
        customers.removeIf(customer -> customer.getId().equals(customerId));
        saveCustomersToCSV();
    }
    
    public Customer findCustomerById(String customerId) {
        return SearchAlgorithms.searchCustomerById(customers, customerId);
    }
    
    public List<Customer> findCustomersByName(String name) {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(customer);
            }
        }
        return result;
    }
    
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }
    
    public void updateCustomer(Customer updatedCustomer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(updatedCustomer.getId())) {
                customers.set(i, updatedCustomer);
                saveCustomersToCSV();
                break;
            }
        }
    }
    
    private void loadCustomersFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = Customer.fromCsvString(line);
                if (customer != null) {
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing customers file found. Starting with empty customer list.");
            // Initialize with some sample customers
            initializeSampleCustomers();
        }
    }
    
    private void saveCustomersToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            for (Customer customer : customers) {
                writer.println(customer.toCsvString());
            }
        } catch (IOException e) {
            System.err.println("Error saving customers to CSV: " + e.getMessage());
        }
    }
    
    private void initializeSampleCustomers() {
        customers.add(new Customer("C001", "John Doe", "john.doe@email.com", "123 Main St, City, State"));
        customers.add(new Customer("C002", "Jane Smith", "jane.smith@email.com", "456 Oak Ave, City, State"));
        customers.add(new Customer("C003", "Bob Johnson", "bob.johnson@email.com", "789 Pine Rd, City, State"));
        saveCustomersToCSV();
    }
}