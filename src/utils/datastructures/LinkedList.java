package utils.datastructures;

public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public static class Node<T> {
        private T data;
        private Node<T> next;
        private Node<T> pre;

        public Node(T data) {
            this.next = null;
            this.pre = null;
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public Node<T> getPre() {
            return pre;
        }

        public void setPre(Node<T> pre) {
            this.pre = pre;
        }
    }

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void addNode (T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPre(tail);
            tail = newNode;
        }
        size++;
    }

    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<T> nodeToDelete;
        if (index == 0) {
            // Deleting the head
            nodeToDelete = head;
            head = head.getNext();
            if (head != null) {
                head.setPre(null);
            } else {
                // The list is now empty
                tail = null;
            }
        } else if (index == size - 1) {
            // Deleting the tail
            nodeToDelete = tail;
            tail = tail.getPre();
            tail.setNext(null);
        } else {
            // Deleting from the middle
            Node<T> currentNode;
            if (index < size / 2) {
                currentNode = head;
                for (int i = 0; i < index; i++) {
                    currentNode = currentNode.getNext();
                }
            } else {
                currentNode = tail;
                for (int i = size - 1; i > index; i--) {
                    currentNode = currentNode.getPre();
                }
            }
            nodeToDelete = currentNode;
            nodeToDelete.getPre().setNext(nodeToDelete.getNext());
            nodeToDelete.getNext().setPre(nodeToDelete.getPre());
        }
        size--;
        return nodeToDelete.getData();
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> currentNode;
        if (index < size / 2) {
            currentNode = head;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.getNext();
            }
        } else {
            currentNode = tail;
            for (int i = size - 1; i > index; i--) {
                currentNode = currentNode.getPre();
            }
        }
        return currentNode.getData();
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPre(newNode);
            head = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new RuntimeException("List is empty");
        }
        Node<T> firstNode = head;
        head = head.getNext();
        if (head != null) {
            head.setPre(null);
        } else {
            tail = null;
        }
        size--;
        return firstNode.getData();
    }

    public Node<T> getHead() {
        return head;
    }
}
