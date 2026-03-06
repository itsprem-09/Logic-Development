public class SqrtFunction {

    public static int mySqrtBruteForce(int n) {
        int ans = 0;
        for (int i = 1; i * i <= n; i++) {
            ans = i;
        }
        return ans;
    }

    // log n time complexity
    public static int mySqrt(int n){
        if (n < 2) {
            return n;
        }

        int left = 1, right = n, ans = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;

            long square = (long) mid * mid;  // avoid overflow

            if (square == n) {
                return mid;
            }
            else if (square < n) {
                ans = mid;      // store possible answer
                left = mid + 1;
            }
            else {
                right = mid - 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int n = 16;
        
        System.out.println("Square root of " + n + " is: " + mySqrt(n));
        System.out.println("Square root of " + n + " is: " + mySqrtBruteForce(n));
    }
}
