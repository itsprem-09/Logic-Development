package DSA.Stack;

import java.util.LinkedList;
import java.util.Queue;

public class StackUsingQueue {
    
    Queue<Integer> q = new LinkedList<>();

    public void push(int x){
        q.add(x);

        // shift all element behind previous one
        for (int i = 0; i < q.size() - 1; i++) {
            q.add(q.remove());
        }
    }

    public int pop(){
        if (q.isEmpty()) {
            return -1;
        }

        return q.remove();
    }

    public int peek(){
        if (q.isEmpty()) {
            return -1;
        }

        return q.peek();
    }

    public static void main(String[] args) {
        StackUsingQueue s = new StackUsingQueue();

        s.push(10);
        s.push(20);
        s.push(30);

        System.out.println(s.pop()); // 30
        System.out.println(s.peek()); // 20
    }
}
