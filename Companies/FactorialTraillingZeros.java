public class FactorialTraillingZeros {

    public static int fact(int n){
        int ans = 1;
        for (int i = 1; i <= n; i++) {
            ans *= i;
        }
        return ans;
    }

    public static int countTrailing(int n){
        int factAns = fact(n);

        String str = Integer.toString(factAns);

        int count = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) != '0') {
                return count;
            }
            count++;
        }
        return count;
    }

    public static int countTrailingZerosBruteForce(int n) {
        int factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }

        int count = 0;
        while (factorial % 10 == 0) {
            count++;
            factorial /= 10;
        }
        return count;
    }

    // O(n) time complexity
    public static int countTrailingZerosNaive(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            int num = i;

            // Count how many times 5 divides current number
            while (num % 5 == 0) {
                count++;
                num /= 5;
            }
        }
        return count;
    }

    // Trailing zeros in n! = count of how many times 5 divides numbers from 1 to n
    public static int countTrailingZeros(int n) {
        int count = 0;
        for (int i = 5; n / i >= 1; i *= 5) {
            count += n / i;
        }
        return count;
    }

    public static void main(String[] args) {
        int n = 25;
        System.out.println("Number of trailing zeros in " + n + "! is: " + countTrailingZeros(n));   
        System.out.println("Number of trailing zeros in " + n + "! is: " + countTrailingZerosNaive(n));   
    }
}
