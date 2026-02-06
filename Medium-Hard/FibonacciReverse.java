public class FibonacciReverse {

    private static int getFibonacciRecursion(int n){
        if (n == 0 || n == 1) {
            return n;
        }
        return getFibonacciRecursion(n-1) + getFibonacciRecursion(n-2);
    }

    private static void printReverseFibonacci(int n){
        if (n <= 0) {
            System.out.println("Please Enter valid input");
            return;
        }
        else if (n == 1) {
            System.out.println("0");
        }
        else if (n == 2) {
            System.out.println("1 0");
        }
        else{
            int first = 0;
            int sec = 1;
            StringBuilder str = new StringBuilder();
            str.append(first+" ");
            str.append(sec+" ");

            for (int i = 3; i <= n; i++) {
                int next = first + sec;
                StringBuilder t = new StringBuilder(Integer.toString(next));
                str.append(t.reverse()+" ");
                first = sec;
                sec = next;
            }

            System.out.println(str.reverse());
        }
    }

    public static void main(String[] args) {
        printReverseFibonacci(19);

        for (int i = 0; i < 5; i++) {
            System.out.println(getFibonacciRecursion(i));
        }
    }
}