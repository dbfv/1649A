package user;

public class User {
    private String username;
    private String password;
    private String role; // "ADMIN" or "USER"
    private String email;
    private String address;
    private String customerId; // For users who are also customers
    
    public User() {}
    
    public User(String username, String password, String role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.address = "";
        this.customerId = "";
    }
    
    public User(String username, String password, String role, String email, String address, String customerId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.address = address;
        this.customerId = customerId;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    // Convenience methods for backward compatibility
    public String getId() { return customerId; }
    public void setId(String id) { this.customerId = id; }
    
    public String getName() { return username; }
    public void setName(String name) { this.username = name; }
    
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
    
    public boolean isUser() {
        return "USER".equalsIgnoreCase(role);
    }
    
    public boolean isCustomer() {
        return customerId != null && !customerId.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
    
    public String toCsvString() {
        // Only include address and customerId if they have values
        StringBuilder csv = new StringBuilder();
        csv.append(username).append(",")
           .append(password).append(",")
           .append(role).append(",")
           .append(email);
        
        // Only add address and customerId if they exist and are not empty
        if (address != null && !address.trim().isEmpty()) {
            csv.append(",").append(address);
            if (customerId != null && !customerId.trim().isEmpty()) {
                csv.append(",").append(customerId);
            }
        } else if (customerId != null && !customerId.trim().isEmpty()) {
            csv.append(",").append("").append(",").append(customerId);
        }
        
        return csv.toString();
    }
    
    public static User fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",", -1); // Keep empty strings
        if (parts.length >= 4) {
            String address = parts.length > 4 && !parts[4].trim().isEmpty() ? parts[4] : "";
            String customerId = parts.length > 5 && !parts[5].trim().isEmpty() ? parts[5] : "";
            return new User(parts[0], parts[1], parts[2], parts[3], address, customerId);
        }
        return null;
    }
}