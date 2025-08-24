package utils.datastructures;

public class Stack<T> {
    private final LinkedList<T> list;
    
    public Stack() {
        this.list = new LinkedList<>();
    }
    
    // Add element to the top of the stack
    public void push(T data) {
        list.addFirst(data);
    }
    
    // Remove and return element from the top of the stack
    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return list.removeFirst();
    }
    
    // Return top element without removing it
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return list.get(0);
    }
    
    // Check if stack is empty
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    // Get size of stack
    public int size() {
        return list.getSize();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stack: [");
        LinkedList.Node<T> current = list.getHead();
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("] <- top");
        return sb.toString();
    }
}