package utils.datastructures;

public class Queue<T> {
    private LinkedList<T> list;
    
    public Queue() {
        this.list = new LinkedList<>();
    }
    
    // Add element to the rear of the queue
    public void enqueue(T data) {
        list.add(data);
    }
    
    // Remove and return element from the front of the queue
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return list.removeFirst();
    }
    
    // Return front element without removing it
    public T front() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return list.get(0);
    }
    
    // Check if queue is empty
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    // Get size of queue
    public int size() {
        return list.size();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Queue: [");
        Node<T> current = list.getHead();
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}