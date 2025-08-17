package book;

import utils.SortingAlgorithms;
import utils.SearchAlgorithms;
import java.io.*;
import java.util.*;

public class BookService {
    private List<Book> books;
    private final String CSV_FILE = "books.csv";
    
    public BookService() {
        this.books = new ArrayList<>();
        loadBooksFromCsv();
    }
    
    public void addBook(Book book) {
        books.add(book);
        saveBooksToCSV();
    }
    
    public void removeBook(String bookId) {
        books.removeIf(book -> book.getId().equals(bookId));
        saveBooksToCSV();
    }
    
    public Book findBookById(String bookId) {
        return books.stream().filter(b -> b.getId().equals(bookId)).findFirst().orElse(null);
    }
    
    public List<Book> findBooksByTitle(String title) {
        return SearchAlgorithms.linearSearchBooksByTitle(books, title);
    }
    
    public List<Book> findBooksByAuthor(String author) {
        return SearchAlgorithms.linearSearchBooksByAuthor(books, author);
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
    
    public void sortBooksByTitle() {
        SortingAlgorithms.insertionSort(books, SortingAlgorithms.BookComparators.BY_TITLE);
    }
    
    public void sortBooksByAuthor() {
        SortingAlgorithms.selectionSort(books, SortingAlgorithms.BookComparators.BY_AUTHOR);
    }
    
    public void sortBooksByPrice() {
        SortingAlgorithms.quickSort(books, SortingAlgorithms.BookComparators.BY_PRICE);
    }
    
    public void sortBooksById() {
        SortingAlgorithms.mergeSort(books, SortingAlgorithms.BookComparators.BY_ID);
    }
    
    public boolean isBookAvailable(String bookId, int quantity) {
        Book book = findBookById(bookId);
        return book != null && book.getQuantity() >= quantity;
    }
    
    public void updateBookQuantity(String bookId, int newQuantity) {
        Book book = findBookById(bookId);
        if (book != null) {
            book.setQuantity(newQuantity);
            saveBooksToCSV();
        }
    }
    
    private void loadBooksFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Book book = Book.fromCsvString(line);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing books file found. Starting with empty inventory.");
            // Initialize with some sample books
            initializeSampleBooks();
        }
    }
    
    private void saveBooksToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            for (Book book : books) {
                writer.println(book.toCsvString());
            }
        } catch (IOException e) {
            System.err.println("Error saving books to CSV: " + e.getMessage());
        }
    }
    
    private void initializeSampleBooks() {
        books.add(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", 12.99, 50));
        books.add(new Book("B002", "To Kill a Mockingbird", "Harper Lee", 14.99, 30));
        books.add(new Book("B003", "1984", "George Orwell", 13.99, 25));
        books.add(new Book("B004", "Pride and Prejudice", "Jane Austen", 11.99, 40));
        books.add(new Book("B005", "The Catcher in the Rye", "J.D. Salinger", 13.50, 35));
        saveBooksToCSV();
    }
}