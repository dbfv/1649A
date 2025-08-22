# Online Bookstore Order Processing System

A Java console application demonstrating efficient data structures and algorithms for managing an online bookstore's inventory, customers, and order processing.

## System Overview

This application implements a complete bookstore management system with real-time search capabilities, multiple sorting algorithms, and queue-based order processing. It serves as a practical demonstration of data structures and algorithms in a real-world scenario.

## Data Characteristics & Algorithm Assumptions

### 📊 Data Size Assumptions

#### Books Inventory
- **Small Bookstore**: 500-2,000 books
- **Medium Bookstore**: 2,000-10,000 books  
- **Large Bookstore**: 10,000-50,000 books
- **Enterprise/Chain**: 50,000+ books

**Default Assumption**: **5,000 books** (medium bookstore scenario)

#### Customer Database
- **Active Customers**: 1,000-10,000 customers
- **Total Customers**: 5,000-25,000 customers
- **Growth Rate**: 100-500 new customers/month

**Default Assumption**: **8,000 total customers, 2,000 active**

#### Order Volume
- **Daily Orders**: 50-500 orders/day
- **Peak Season**: 2-5x normal volume
- **Order History**: 1-3 years retained
- **Total Orders**: 20,000-200,000 orders

**Default Assumption**: **200 orders/day, 100,000 total orders**

### 🔍 Search Pattern Assumptions

#### Real-Time Search Behavior
- **Search Frequency**: 10-50 searches per user session
- **Average Search Length**: 3-8 characters
- **Search Types**: 70% title, 25% author, 5% other
- **Response Time Requirement**: < 100ms for real-time feedback
- **Partial Match Expectation**: Users expect results after 2-3 characters

#### Search Result Expectations
- **Typical Results**: 5-20 matching books per search
- **Maximum Display**: 50 results (pagination beyond)
- **Relevance Priority**: Exact matches → Prefix matches → Contains matches

### ⚡ Performance Requirements

#### Response Time Targets
- **Real-time Search**: < 50ms per keystroke
- **Full Search Results**: < 200ms
- **Data Loading**: < 2 seconds on startup
- **Sorting Operations**: < 500ms for full dataset
- **Order Processing**: < 100ms per order

#### Memory Constraints
- **Available RAM**: 512MB - 2GB (typical Java console app)
- **Data Structure Overhead**: < 100MB for core data
- **Search Index Memory**: < 50MB additional
- **Concurrent Users**: 1 (console application)

### 🎯 Algorithm Selection Criteria

#### Primary Factors
1. **Data Size**: Medium-scale datasets (thousands, not millions)
2. **Search Type**: Partial string matching with real-time feedback
3. **Update Frequency**: Moderate (books added/updated daily)
4. **Memory Usage**: Conservative (standard desktop/laptop)
5. **Implementation Complexity**: Maintainable by junior developers

#### Secondary Factors
1. **Startup Time**: Fast application initialization
2. **Scalability**: Should handle 10x growth gracefully
3. **Reliability**: Consistent performance under load
4. **Flexibility**: Support multiple search criteria

### 📈 Scalability Considerations

#### Current Scale (Target)
- **Books**: 5,000 items
- **Customers**: 8,000 records
- **Orders**: 100,000 transactions
- **Search Operations**: 1,000/hour during peak

#### Growth Projections (3-5 years)
- **Books**: 25,000 items (5x growth)
- **Customers**: 40,000 records (5x growth)
- **Orders**: 1,000,000 transactions (10x growth)
- **Search Operations**: 10,000/hour during peak (10x growth)

### 🔧 Algorithm Recommendations Based on Assumptions

#### Search Algorithms
- **Primary**: **Optimized Linear Search** with early termination
  - Rationale: Simple, flexible, efficient for 5K books
  - Performance: O(n) but typically O(n/10) with optimizations
  - Memory: O(1) additional space

- **Alternative**: **Trie-based Prefix Search** for title searches
  - Consider when: > 25,000 books or < 10ms response requirement
  - Trade-off: Higher memory usage, complex implementation

#### Sorting Algorithms
- **Title/Author Sorting**: **Merge Sort** (stable, predictable O(n log n))
- **Price Sorting**: **Quick Sort** (average O(n log n), in-place)
- **Small Datasets**: **Insertion Sort** (< 100 items, simple implementation)

#### Data Structures
- **Primary Storage**: **ArrayList** (dynamic, cache-friendly)
- **Search Indexing**: Linear and binary search algorithms for lookups
- **Order Processing**: **Custom Queue** (FIFO)

### 🎛️ Configuration Parameters

```java
// Configurable constants based on assumptions
public class SystemConfig {
    // Data size assumptions
    public static final int EXPECTED_BOOK_COUNT = 5000;
    public static final int EXPECTED_CUSTOMER_COUNT = 8000;
    public static final int EXPECTED_DAILY_ORDERS = 200;
    
    // Performance targets
    public static final int SEARCH_RESPONSE_TIME_MS = 50;
    public static final int MAX_SEARCH_RESULTS = 50;
    public static final int REAL_TIME_SEARCH_MIN_CHARS = 2;
    
    // Algorithm thresholds
    public static final int LINEAR_SEARCH_THRESHOLD = 10000;
    public static final int INSERTION_SORT_THRESHOLD = 100;
    public static final int QUICK_SORT_THRESHOLD = 1000;
}
```

### 📋 Testing Scenarios

#### Load Testing
- **Minimum**: 100 books, 50 customers, 20 orders
- **Typical**: 5,000 books, 8,000 customers, 100,000 orders
- **Stress**: 50,000 books, 50,000 customers, 1,000,000 orders

#### Performance Benchmarks
- Search 5,000 books: Target < 50ms
- Sort 5,000 books: Target < 500ms
- Process 100 orders: Target < 10 seconds

## Getting Started

### Prerequisites
- Java 8 or higher
- Any Java IDE or text editor
- Terminal/Command Prompt

### Running the Application
```bash
cd src
javac App.java
java App
```

### Project Structure
```
src/
├── App.java                 # Main application entry point
├── book/                    # Book management module
│   ├── Book.java           # Book model
│   ├── BookController.java # UI and real-time search
│   └── BookService.java    # Business logic and algorithms
├── customer/               # Customer management module
├── order/                  # Order processing module
└── utils/                  # Algorithms and data structures
    ├── SearchAlgorithms.java
    ├── SortingAlgorithms.java
    ├── RealTimeSearchUtil.java
    └── datastructures/     # Custom implementations
```

## Features

- **Real-Time Search**: Type-ahead search with instant results
- **Multiple Sorting**: Insertion, Selection, Quick, and Merge sort implementations
- **Queue-Based Processing**: FIFO order processing system
- **CSV Persistence**: File-based data storage
- **Interactive Console UI**: User-friendly menu system

## Algorithm Implementations

The system demonstrates practical applications of:
- Linear and Binary Search algorithms
- Four different sorting algorithms with appropriate use cases
- Custom data structures (LinkedList, Queue, Stack)
- Real-time search optimization techniques

---

*These assumptions are designed for educational purposes and realistic business scenarios. Adjust parameters based on specific requirements.*
