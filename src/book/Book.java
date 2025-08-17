package book;

public class Book {
    private String id;
    private String title;
    private String author;
    private double price;
    private int quantity;
    
    public Book() {}
    
    public Book(String id, String title, String author, double price, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
    
    public String toCsvString() {
        return id + "," + title + "," + author + "," + price + "," + quantity;
    }
    
    public static Book fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 5) {
            return new Book(parts[0], parts[1], parts[2], 
                          Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
        }
        return null;
    }
}