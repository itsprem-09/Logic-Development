package Gemini;

public class DecodeWays {

    public static int decodeWays(String str){
        if (str == null || str.length() == 0 || str.charAt(0) == '0') {
            return 0;
        }

        int n = str.length();
        int[] dp = new int[n + 1];
        dp[0] = 1; // Base case: there's one way to decode an empty string
        dp[1] = 1; // Base case: there's one way to decode a single character (if it's not '0')

        for (int i = 2; i < dp.length; i++) {
            // If the current character is not '0', we can decode it as a single digit
            if (str.charAt(i - 1) != '0') {
                dp[i] += dp[i - 1];
            }

            // If the previous two characters form a valid two-digit number (10-26), we can decode them together
            int twoDigit = Integer.parseInt(str.substring(i - 2, i));
            if (twoDigit >= 10 && twoDigit <= 26) {
                dp[i] += dp[i - 2];
            }
        }

        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println(decodeWays("12")); // Output: 2
        System.out.println(decodeWays("226")); // Output: 3
        System.out.println(decodeWays("0")); // Output: 0
        System.out.println(decodeWays("10")); // Output: 1
    }
}
