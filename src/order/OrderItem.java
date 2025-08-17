package order;

import book.Book;

public class OrderItem {
    private Book book;
    private int quantity;
    
    public OrderItem() {}
    
    public OrderItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }
    
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getTotalPrice() {
        return book.getPrice() * quantity;
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "book=" + book +
                ", quantity=" + quantity +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
}