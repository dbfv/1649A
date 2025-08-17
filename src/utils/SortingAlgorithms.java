package utils;

import book.Book;
import java.util.List;
import java.util.Comparator;

public class SortingAlgorithms {
    
    // Insertion Sort
    public static void insertionSort(List<Book> books, Comparator<Book> comparator) {
        for (int i = 1; i < books.size(); i++) {
            Book key = books.get(i);
            int j = i - 1;
            
            while (j >= 0 && comparator.compare(books.get(j), key) > 0) {
                books.set(j + 1, books.get(j));
                j--;
            }
            books.set(j + 1, key);
        }
    }
    
    // Selection Sort
    public static void selectionSort(List<Book> books, Comparator<Book> comparator) {
        for (int i = 0; i < books.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < books.size(); j++) {
                if (comparator.compare(books.get(j), books.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Book temp = books.get(i);
                books.set(i, books.get(minIndex));
                books.set(minIndex, temp);
            }
        }
    }
    
    // Quick Sort
    public static void quickSort(List<Book> books, Comparator<Book> comparator) {
        quickSort(books, 0, books.size() - 1, comparator);
    }
    
    private static void quickSort(List<Book> books, int low, int high, Comparator<Book> comparator) {
        if (low < high) {
            int pivotIndex = partition(books, low, high, comparator);
            quickSort(books, low, pivotIndex - 1, comparator);
            quickSort(books, pivotIndex + 1, high, comparator);
        }
    }
    
    private static int partition(List<Book> books, int low, int high, Comparator<Book> comparator) {
        Book pivot = books.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (comparator.compare(books.get(j), pivot) <= 0) {
                i++;
                Book temp = books.get(i);
                books.set(i, books.get(j));
                books.set(j, temp);
            }
        }
        
        Book temp = books.get(i + 1);
        books.set(i + 1, books.get(high));
        books.set(high, temp);
        
        return i + 1;
    }
    
    // Merge Sort
    public static void mergeSort(List<Book> books, Comparator<Book> comparator) {
        if (books.size() <= 1) return;
        mergeSort(books, 0, books.size() - 1, comparator);
    }
    
    private static void mergeSort(List<Book> books, int left, int right, Comparator<Book> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(books, left, mid, comparator);
            mergeSort(books, mid + 1, right, comparator);
            merge(books, left, mid, right, comparator);
        }
    }
    
    private static void merge(List<Book> books, int left, int mid, int right, Comparator<Book> comparator) {
        Book[] temp = new Book[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        
        while (i <= mid && j <= right) {
            if (comparator.compare(books.get(i), books.get(j)) <= 0) {
                temp[k++] = books.get(i++);
            } else {
                temp[k++] = books.get(j++);
            }
        }
        
        while (i <= mid) {
            temp[k++] = books.get(i++);
        }
        
        while (j <= right) {
            temp[k++] = books.get(j++);
        }
        
        for (i = left; i <= right; i++) {
            books.set(i, temp[i - left]);
        }
    }
    
    // Comparators for different sorting criteria
    public static class BookComparators {
        public static final Comparator<Book> BY_TITLE = 
            (b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle());
        
        public static final Comparator<Book> BY_AUTHOR = 
            (b1, b2) -> b1.getAuthor().compareToIgnoreCase(b2.getAuthor());
        
        public static final Comparator<Book> BY_PRICE = 
            (b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice());
        
        public static final Comparator<Book> BY_ID = 
            (b1, b2) -> b1.getId().compareToIgnoreCase(b2.getId());
    }
}