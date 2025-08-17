package customer;

import java.util.List;
import java.util.Scanner;

public class CustomerController {
    private final CustomerService customerService;
    private final Scanner scanner;
    
    public CustomerController(CustomerService customerService, Scanner scanner) {
        this.customerService = customerService;
        this.scanner = scanner;
    }
    
    public void showCustomerMenu() {
        while (true) {
            System.out.println("\n=== Customer Management ===");
            System.out.println("1. View All Customers");
            System.out.println("2. Search Customer by ID");
            System.out.println("3. Search Customers by Name");
            System.out.println("4. Add New Customer");
            System.out.println("5. Update Customer");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    viewAllCustomers();
                    break;
                case 2:
                    searchCustomerById();
                    break;
                case 3:
                    searchCustomersByName();
                    break;
                case 4:
                    addNewCustomer();
                    break;
                case 5:
                    updateCustomer();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void viewAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
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
            
            for (Customer customer : customers) { 
                String name = customer.getName().length() > 20 ? customer.getName().substring(0, 17) + "..." : customer.getName();
                String email = customer.getEmail().length() > 25 ? customer.getEmail().substring(0, 22) + "..." : customer.getEmail();
                String address = customer.getAddress().length() > 30 ? customer.getAddress().substring(0, 27) + "..." : customer.getAddress();
                System.out.printf("║ %-8s │ %-20s │ %-25s │ %-30s ║%n", 
                    customer.getId(), name, email, address);
            }
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private void searchCustomerById() {
        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine();
        Customer customer = customerService.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Customer not found!");
        } else {
            System.out.println("\n=== Customer Found ===");
            System.out.println(customer);
        }
    }
    
    private void searchCustomersByName() {
        System.out.print("Enter customer name to search: ");
        String name = scanner.nextLine();
        List<Customer> customers = customerService.findCustomersByName(name);
        
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
            
            for (Customer customer : customers) {
                String customerName = customer.getName().length() > 20 ? customer.getName().substring(0, 17) + "..." : customer.getName();
                String email = customer.getEmail().length() > 25 ? customer.getEmail().substring(0, 22) + "..." : customer.getEmail();
                String address = customer.getAddress().length() > 30 ? customer.getAddress().substring(0, 27) + "..." : customer.getAddress();
                System.out.printf("║ %-8s │ %-20s │ %-25s │ %-30s ║%n", 
                    customer.getId(), customerName, email, address);
            }
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private void addNewCustomer() {
        System.out.print("Enter customer ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        
        Customer customer = new Customer(id, name, email, address);
        customerService.addCustomer(customer);
        System.out.println("Customer added successfully!");
    }
    
    private void updateCustomer() {
        System.out.print("Enter customer ID to update: ");
        String customerId = scanner.nextLine();
        Customer customer = customerService.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        
        System.out.println("Current customer: " + customer);
        System.out.print("Enter new name (or press Enter to keep current): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            customer.setName(name);
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
        
        customerService.updateCustomer(customer);
        System.out.println("Customer updated successfully!");
    }
}