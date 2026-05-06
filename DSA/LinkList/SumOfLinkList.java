package DSA.LinkList;

class Node{
    int data;
    Node next;

    public Node(int data){
        this.data = data;
        this.next = null;
    }
}

public class SumOfLinkList {
    
    public static Node sum(Node head1, Node head2){
        int sum1 = 0;
        Node t = head1;

        while (t != null) {
            sum1 = sum1 * 10 + t.data;
            t = t.next;
        }

        int sum2 = 0;
        t = head2;
        while (t != null) {
            sum2 = sum2 * 10 + t.data;
            t = t.next;
        }

        // reverse both sum
        sum1 = reverse(sum1);
        sum2 = reverse(sum2);

        int totalSum = sum1 + sum2;
        System.out.println("Total Sum: " + totalSum);

        Node resHead = null;
        t = resHead;

        while (totalSum > 0) {
            int digit = totalSum % 10;

            Node newNode = new Node(digit);
            
            if (resHead == null) {
                resHead = newNode;
                t = newNode;
            }
            else{
                t.next = newNode;
                t = newNode;
            }

            totalSum /= 10;       
        }

        return resHead;
    }

    public static int reverse(int num){
        int rev = 0;
        while (num > 0) {
            int digit = num % 10;
            rev = rev * 10 + digit;
            num /= 10;
        }
        return rev;
    }

    public static void printList(Node head){
        Node t = head;
        while (t != null) {
            System.out.print(t.data + " ");
            t = t.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head1 = new Node(2);
        head1.next = new Node(4);
        head1.next.next = new Node(3);

        Node h2 = new Node(5);
        h2.next = new Node(6);
        h2.next.next = new Node(4);

        sum(head1, h2);

        System.out.println("List 1:");
        printList(head1);

        System.out.println("List 2:");
        printList(h2);

        System.out.println("Result List:");
        printList(sum(head1, h2));
    }
}
