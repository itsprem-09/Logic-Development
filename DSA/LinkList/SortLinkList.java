package DSA.LinkList;

class Node{
    int data;
    Node next;

    public Node(int data){
        this.data = data;
        this.next = null;
    }
}

public class SortLinkList {
    public static Node sortList(Node head){
        if (head == null || head.next == null) {
            return head;
        }

        Node mid = getMid(head);

        Node nextOfMid = mid.next;
        mid.next = null;

        Node left = sortList(head);
        Node right = sortList(nextOfMid);

        return merge(left, right);
    }

    public static Node getMid(Node head){
        if (head == null) {
            return head;
        }

        Node slow = head;
        Node fast = head.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    public static Node merge(Node a, Node b){
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }

        Node result;

        if (a.data <= b.data) {
            result = a;
            a.next = merge(a.next, b);
        }
        else{
            result = b;
            b.next = merge(a, b.next);
        }

        return result;
    }

    public static void printList(Node head){
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = new Node(4);
        head.next = new Node(2);
        head.next.next = new Node(1);
        head.next.next.next = new Node(3);

        System.out.println("Original List:");
        printList(head);

        head = sortList(head);

        System.out.println("Sorted List:");
        printList(head);
    }
    
}