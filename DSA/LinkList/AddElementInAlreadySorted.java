package DSA.LinkList;

class Node{
    int data;
    Node next;

    public Node(int data){
        this.data = data;
        this.next = null;
    }
}

public class AddElementInAlreadySorted {
    public static Node insert(Node head, int data){
        if (head == null) {
            return new Node(data);
        }
        if (data <= head.data) {
            Node n = new Node(data);
            n.next = head;
            return n;
        }

        Node t = head;
        
        while (t.next != null && t.next.data < data ) {
            t = t.next;
        }

        Node n = new Node(data);
        n.next = t.next;
        t.next = n;

        return head;
    }

    public static void printList(Node head){
        Node temp = head;

        while (temp != null) {
            System.out.println(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.next = new Node(3);
        head.next.next = new Node(5);

        System.out.println("Original List:");
        printList(head);

        head = insert(head, 4);
        System.out.println("After inserting 4:");
        printList(head);

        head = insert(head, 0);
        System.out.println("After inserting 0:");
        printList(head);

        head = insert(head, 6);
        System.out.println("After inserting 6:");
        printList(head);
    }
}
