package utils.datastructures;

public class Queue<T> {
    private final LinkedList<T> list;

    public Queue() {
        this.list = new LinkedList<>();
    }

    public void enqueue(T data) {
        list.addNode(data);
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Cannot dequeue from an empty queue");
        }
        return list.delete(0);
    }

    public T peek() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Queue is empty");
        }
        return list.get(0);
    }

    public int getSize() {
        return list.getSize();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
