package customer;

public class Customer {
    private String id;
    private String name;
    private String email;
    private String address;
    
    public Customer() {}
    
    public Customer(String id, String name, String email, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
    
    public String toCsvString() {
        return id + "," + name + "," + email + "," + address;
    }
    
    public static Customer fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 4) {
            return new Customer(parts[0], parts[1], parts[2], parts[3]);
        }
        return null;
    }
}