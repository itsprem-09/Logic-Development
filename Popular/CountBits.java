package Popular;

// approach : 
// If you right shift a number:

// i >> 1

// You remove the last bit.

// Example:

// 5 (101)
// 5 >> 1 = 2 (10) last bit is removed

// So:

// number_of_1s(5)
// = number_of_1s(2) + last_bit_of_5

// And last bit is:

// i % 2  (or i & 1) because if number is even last bit is 0 and if number is odd last bit is 1

public class CountBits {

    public static int[] countBits(int n){
        int[] ans = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            ans[i] = ans[i >> 1] + (i % 2);
        }
        return ans;
    }

    public static void main(String[] args) {
        int n = 5;
        int[] ans = countBits(n);

        for (int i : ans) {
            System.out.print(i + " ");
        }
    }
}
