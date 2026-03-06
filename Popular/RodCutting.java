package Popular;

public class RodCutting {

    private static int cutRod(int[] price){
        int n = price.length;

        int[] dp = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {

            int max = Integer.MIN_VALUE;

            for (int j = 1; j <= i; j++) {
                max = Math.max(max, price[j - 1] + dp[i - j]);
            }

            dp[i] = max;
        }

        return dp[n];
    }

    public static void main(String[] args) {
        
    }
}
