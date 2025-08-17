package utils.datastructures;

public class LinkedList<T> {
    private Node<T> head;
    private int size;
    
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }
    
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.setNext(head);
        head = newNode;
        size++;
    }
    
    public T removeFirst() {
        if (head == null) {
            return null;
        }
        T data = head.getData();
        head = head.getNext();
        size--;
        return data;
    }
    
    public T removeLast() {
        if (head == null) {
            return null;
        }
        if (head.getNext() == null) {
            T data = head.getData();
            head = null;
            size--;
            return data;
        }
        
        Node<T> current = head;
        while (current.getNext().getNext() != null) {
            current = current.getNext();
        }
        T data = current.getNext().getData();
        current.setNext(null);
        size--;
        return data;
    }
    
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }
    
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    
    public Node<T> getHead() { return head; }
}