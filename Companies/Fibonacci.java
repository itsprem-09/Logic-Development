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

    public static void printFibonacciReverse(int n){
        int first = 0;
        int sec = 1;

        if (n == 1) {
            System.out.println(first);
            return;
        }
        else if (n == 2) {
            System.out.println(sec);
            System.out.println(first);
            return;
        }

        int[] fib = new int[n];
        fib[0] = first;
        fib[1] = sec;

        for (int i = 2; i < n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }

        for (int i = n - 1; i >= 0; i--) {
            System.out.println(fib[i]);
        }
    }

    public static void main(String[] args) {
        printFibonacci(2);
        System.out.println("Reverse:");
        printFibonacciReverse(2);
    }
}
