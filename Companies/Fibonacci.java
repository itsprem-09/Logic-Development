public class Fibonacci {

    public static void printFibonacci(int n){
        int first = 0;
        int sec = 1;

        if (n == 1) {
            System.out.println(first);
            return;
        }
        else if (n == 2) {
            System.out.println(first);
            System.out.println(sec);
            return;
        }

        System.out.println(first);
        System.out.println(sec);
        for (int i = 3; i <= n; i++) {
            int next = first + sec;
            System.out.println(next);
            first = sec;
            sec = next;
        }
    }

    public static void main(String[] args) {
        printFibonacci(5);
    }
}
