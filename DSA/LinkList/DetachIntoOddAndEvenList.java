package DSA.LinkList;

class Node{
    int data;
    Node next;
    Node prev;

    public Node(int data){
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}

public class DetachIntoOddAndEvenList {
    
    public static void detach(Node head){
        if (head == null) {
            return;
        }

        Node oddHead = null;
        Node oddTail = null;
        Node evenHead = null;
        Node evenTail = null;

        Node curr = head;
        while (curr != null) {
            Node next = curr.next;

            // Detach the current node
            curr.next = null;
            curr.prev = null;

            if (curr.data % 2 == 0) {
                // even node
                if (evenHead == null) {
                    evenHead = curr;
                    evenTail = curr;
                }
                else{
                    evenTail.next = curr;
                    curr.prev = evenTail;
                    evenTail = curr;
                }
            }
            else{
                // odd node
                if (oddHead == null) {
                    oddHead = curr;
                    oddTail = curr;
                }
                else{
                    oddTail.next = curr;
                    curr.prev = oddTail;
                    oddTail = curr;
                }
            }

            curr = next;
        }

        System.out.println("Odd List:");
        printList(oddHead);

        System.out.println("Even List:");   
        printList(evenHead);
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
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.prev = head;

        head.next.next = new Node(3);
        head.next.next.prev = head.next;

        head.next.next.next = new Node(4);
        head.next.next.next.prev = head.next.next;

        head.next.next.next.next = new Node(5);
        head.next.next.next.next.prev = head.next.next.next;

        detach(head);
    }

}
