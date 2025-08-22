# I. Introduction

This project is an Online Bookstore Order Processing System. It's a console-based Java application that demonstrates the use of various data structures and algorithms for managing an online bookstore's inventory, customers, and orders.

### Project's functionality requirements: (Use case diagram)

#### 1. Actors:
*   **Customer:** A registered user of the system.
*   **Admin:** A user with special privileges to manage the system.

#### 2. Use cases:
*   **Customer:**
    *   View Products (Books)
    *   Place Order
    *   View Order History
    *   Sorting & searching functionalities for books.
*   **Admin:**
    *   CRUD (Create, Read, Update, Delete) operations for Books.
    *   Order Management (Update Order Status).
    *   User Management.

# II. System Design

### Project type:
This is a **console-based application**. The user interacts with the system through a command-line interface.

### Data storage:
The application uses a **file-based data storage** system. Data for books, users, and orders are stored in CSV files (`books.csv`, `users.csv`, `orders.csv`).

### Data structure:
The following data structures are used in this project:

*   **Array/ArrayList:** The primary data structure for storing lists of books, users, and orders in memory. It provides dynamic resizing and efficient access to elements by index.
*   **Linked List:** A custom implementation of a generic `LinkedList<T>` is used as the underlying data structure for the `Queue` and `Stack`. It consists of `Node<T>` objects, each containing data and a reference to the next node.
*   **Queue:** A custom generic `Queue<T>` is implemented using the `LinkedList`. It follows the First-In-First-Out (FIFO) principle and is used to manage the processing of orders. New orders are enqueued and processed in the order they are received.

# III. Implementation & Important algorithms

### Project's architecture:
The project follows a layered architecture, separating concerns into different packages:

*   `book/`: Contains classes related to books (`Book`, `BookController`, `BookService`).
*   `user/`: Contains classes for user management and authentication (`User`, `UserController`, `UserService`, `AuthenticationService`).
*   `order/`: Contains classes for order processing (`Order`, `OrderItem`, `OrderController`, `OrderService`).
*   `utils/`: Contains utility classes, including implementations of searching and sorting algorithms, and custom data structures.
*   `App.java`: The main entry point of the application, responsible for initializing services and controllers and managing the main menu.

### Project's flow:
The application starts by presenting a login menu. Users can either log in with existing credentials or register for a new account. Once logged in, the main menu is displayed.

*   **Admin users** have access to an admin menu with options for book management, customer management, order management, and user management.
*   **Regular users** have a user menu with options to browse and search for books, place orders, and view their order history.

The application uses a scanner to get user input and controllers to handle user interactions, which then call services to perform business logic.

### Data Characteristics & Algorithm Assumptions

#### 📊 Data Size Assumptions

- **Books Inventory**: Default assumption is **5,000 books**.
- **Customer Database**: Default assumption is **8,000 total customers, 2,000 active**.
- **Order Volume**: Default assumption is **200 orders/day, 100,000 total orders**.

#### 🔍 Search Pattern Assumptions

- **Search Frequency**: 10-50 searches per user session.
- **Response Time Requirement**: < 100ms for real-time feedback.
- **Partial Match Expectation**: Users expect results after 2-3 characters.

#### ⚡ Performance Requirements

- **Real-time Search**: < 50ms per keystroke.
- **Full Search Results**: < 200ms.
- **Data Loading**: < 2 seconds on startup.
- **Sorting Operations**: < 500ms for full dataset.

#### 🎯 Algorithm Selection Criteria

- **Primary Factors**:
    1.  **Data Size**: Medium-scale datasets (thousands, not millions).
    2.  **Search Type**: Partial string matching with real-time feedback.
    3.  **Update Frequency**: Moderate (books added/updated daily).
    4.  **Memory Usage**: Conservative.
    5.  **Implementation Complexity**: Maintainable by junior developers.

### Project's important algorithms:

#### Sorting algorithm used in the project:
The project implements and uses several sorting algorithms for different purposes:

*   **Insertion Sort:** Used to sort books by title.
*   **Selection Sort:** Used to sort books by author.
*   **Quick Sort:** Used to sort books by price.
*   **Merge Sort:** Used to sort books by ID.

These algorithms are implemented in `utils/SortingAlgorithms.java` and are used in the `BookService` to provide different sorting options for the book list.

#### Searching algorithm used in the project:
The project primarily uses **Linear Search** for most of its search operations.

*   **Linear Search:** Implemented in `utils/SearchAlgorithms.java`, it is used to find books by title or author, orders by ID or customer name, and users by username.

While the `README.md` mentions the possibility of using Binary Search, the current implementation in `SearchAlgorithms.java` shows that it is available but not actively used in the services. The services exclusively use linear search.

# IV. Evaluation

### Time complexity of the core operations:
*   **Searching (Linear Search):** O(n), where 'n' is the number of items in the list (books, orders, or users).
*   **Sorting:**
    *   **Insertion Sort:** O(n^2) in the average and worst case.
    *   **Selection Sort:** O(n^2) in all cases.
    *   **Quick Sort:** O(n log n) on average, but O(n^2) in the worst case.
    *   **Merge Sort:** O(n log n) in all cases.
*   **Order Processing (Queue):**
    *   `enqueue`: O(1)
    *   `dequeue`: O(1)

### Space complexity of the core operations:
*   **Data Storage (ArrayLists):** O(n) to store the lists of books, users, and orders in memory.
*   **Sorting:**
    *   **Insertion Sort, Selection Sort, Quick Sort:** O(1) (in-place sorting).
    *   **Merge Sort:** O(n) for the temporary array used during the merge process.
*   **Queue (LinkedList-based):** O(n) to store the elements in the queue.

### Limitations of the project:
*   **Scalability:** The file-based storage and the use of linear search for most operations can lead to performance degradation as the amount of data grows.
*   **No GUI:** The console-based interface is not as user-friendly as a graphical user interface.
*   **Single-threaded:** The application is single-threaded, which means it can only handle one user at a time.
*   **Data Integrity:** The CSV file storage does not enforce data integrity constraints like a relational database would.
*   **Security:** Passwords are stored in plain text in the `users.csv` file, which is a major security vulnerability.

# V. Conclusion

### Final thoughts about the project:
This project provides a solid foundation for an online bookstore system and serves as an excellent demonstration of fundamental data structures and algorithms in a practical context. The separation of concerns into different layers makes the code relatively easy to understand and maintain.

### Future improvements:
*   **Database Integration:** Replace the file-based storage with a relational database (e.g., MySQL, PostgreSQL) to improve scalability, data integrity, and performance.
*   **GUI:** Develop a graphical user interface using a framework like Swing or JavaFX to enhance the user experience.
*   **Web Application:** Convert the project into a web application using technologies like Spring Boot and a modern front-end framework (e.g., React, Angular).
*   **Improved Search:** Implement more efficient search algorithms, such as using a hash map for O(1) lookups by ID or a more advanced text search library like Apache Lucene for full-text search.
*   **Password Hashing:** Implement password hashing (e.g., using bcrypt) to securely store user passwords.
*   **Concurrency:** Introduce multi-threading to handle multiple users or background tasks concurrently.
