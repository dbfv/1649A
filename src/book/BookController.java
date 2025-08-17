package book;

import java.util.List;
import java.util.Scanner;

public class BookController {
    private BookService bookService;
    private Scanner scanner;
    
    public BookController(BookService bookService, Scanner scanner) {
        this.bookService = bookService;
        this.scanner = scanner;
    }
    
    public void showBookMenu() {
        while (true) {
            System.out.println("\n=== Book Management ===");
            System.out.println("1. View All Books");
            System.out.println("2. Search Books by Title");
            System.out.println("3. Search Books by Author");
            System.out.println("4. Add New Book");
            System.out.println("5. Update Book Quantity");
            System.out.println("6. Sort Books");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    viewAllBooks();
                    break;
                case 2:
                    searchBooksByTitle();
                    break;
                case 3:
                    searchBooksByAuthor();
                    break;
                case 4:
                    addNewBook();
                    break;
                case 5:
                    updateBookQuantity();
                    break;
                case 6:
                    sortBooksMenu();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void viewAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│          No books available        │");
            System.out.println("└─────────────────────────────────────┘");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                    ALL BOOKS                                        ║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ %-6s │ %-25s │ %-20s │ %-8s │ %-8s ║%n", "ID", "Title", "Author", "Price", "Quantity");
            System.out.println("╠════════┼═══════════════════════════┼══════════════════════┼══════════┼══════════╣");
            
            for (Book book : books) {
                String title = book.getTitle().length() > 25 ? book.getTitle().substring(0, 22) + "..." : book.getTitle();
                String author = book.getAuthor().length() > 20 ? book.getAuthor().substring(0, 17) + "..." : book.getAuthor();
                System.out.printf("║ %-6s │ %-25s │ %-20s │ $%-7.2f │ %-8d ║%n", 
                    book.getId(), title, author, book.getPrice(), book.getQuantity());
            }
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private void searchBooksByTitle() {
        System.out.print("Enter book title to search (or 'back' to return): ");
        String title = scanner.nextLine();
        if (title.equalsIgnoreCase("back")) {
            return;
        }
        List<Book> books = bookService.findBooksByTitle(title);
        
        if (books.isEmpty()) {
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│  No books found with title containing: " + String.format("%-15s", title) + "│");
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                              SEARCH RESULTS - TITLE                                ║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ %-6s │ %-25s │ %-20s │ %-8s │ %-8s ║%n", "ID", "Title", "Author", "Price", "Quantity");
            System.out.println("╠════════┼═══════════════════════════┼══════════════════════┼══════════┼══════════╣");
            
            for (Book book : books) {
                String bookTitle = book.getTitle().length() > 25 ? book.getTitle().substring(0, 22) + "..." : book.getTitle();
                String author = book.getAuthor().length() > 20 ? book.getAuthor().substring(0, 17) + "..." : book.getAuthor();
                System.out.printf("║ %-6s │ %-25s │ %-20s │ $%-7.2f │ %-8d ║%n", 
                    book.getId(), bookTitle, author, book.getPrice(), book.getQuantity());
            }
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private void searchBooksByAuthor() {
        System.out.print("Enter author name to search (or 'back' to return): ");
        String author = scanner.nextLine();
        if (author.equalsIgnoreCase("back")) {
            return;
        }
        List<Book> books = bookService.findBooksByAuthor(author);
        
        if (books.isEmpty()) {
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│  No books found by author containing: " + String.format("%-15s", author) + "│");
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                              SEARCH RESULTS - AUTHOR                               ║");
            System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("║ %-6s │ %-25s │ %-20s │ %-8s │ %-8s ║%n", "ID", "Title", "Author", "Price", "Quantity");
            System.out.println("╠════════┼═══════════════════════════┼══════════════════════┼══════════┼══════════╣");
            
            for (Book book : books) {
                String title = book.getTitle().length() > 25 ? book.getTitle().substring(0, 22) + "..." : book.getTitle();
                String bookAuthor = book.getAuthor().length() > 20 ? book.getAuthor().substring(0, 17) + "..." : book.getAuthor();
                System.out.printf("║ %-6s │ %-25s │ %-20s │ $%-7.2f │ %-8d ║%n", 
                    book.getId(), title, bookAuthor, book.getPrice(), book.getQuantity());
            }
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }
    
    private void addNewBook() {
        System.out.print("Enter book ID (or 'back' to return): ");
        String id = scanner.nextLine();
        if (id.equalsIgnoreCase("back")) {
            return;
        }
        System.out.print("Enter book title (or 'back' to return): ");
        String title = scanner.nextLine();
        if (title.equalsIgnoreCase("back")) {
            return;
        }
        System.out.print("Enter author name (or 'back' to return): ");
        String author = scanner.nextLine();
        if (author.equalsIgnoreCase("back")) {
            return;
        }
        System.out.print("Enter price (or 'back' to return): ");
        String priceInput = scanner.nextLine();
        if (priceInput.equalsIgnoreCase("back")) {
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format!");
            return;
        }
        System.out.print("Enter quantity (or 'back' to return): ");
        String quantityInput = scanner.nextLine();
        if (quantityInput.equalsIgnoreCase("back")) {
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity format!");
            return;
        }
        
        Book book = new Book(id, title, author, price, quantity);
        bookService.addBook(book);
        System.out.println("Book added successfully!");
    }
    
    private void updateBookQuantity() {
        System.out.print("Enter book ID (or 'back' to return): ");
        String bookId = scanner.nextLine();
        if (bookId.equalsIgnoreCase("back")) {
            return;
        }
        Book book = bookService.findBookById(bookId);
        
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }
        
        System.out.println("Current book: " + book);
        System.out.print("Enter new quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        bookService.updateBookQuantity(bookId, newQuantity);
        System.out.println("Book quantity updated successfully!");
    }
    
    private void sortBooksMenu() {
        System.out.println("\n=== Sort Books ===");
        System.out.println("1. Sort by Title (Insertion Sort)");
        System.out.println("2. Sort by Author (Selection Sort)");
        System.out.println("3. Sort by Price (Quick Sort)");
        System.out.println("4. Sort by ID (Merge Sort)");
        System.out.print("Choose sorting option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                bookService.sortBooksByTitle();
                System.out.println("Books sorted by title using Insertion Sort!");
                break;
            case 2:
                bookService.sortBooksByAuthor();
                System.out.println("Books sorted by author using Selection Sort!");
                break;
            case 3:
                bookService.sortBooksByPrice();
                System.out.println("Books sorted by price using Quick Sort!");
                break;
            case 4:
                bookService.sortBooksById();
                System.out.println("Books sorted by ID using Merge Sort!");
                break;
            default:
                System.out.println("Invalid option.");
        }
        
        viewAllBooks();
    }
}