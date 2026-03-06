package Popular;

import java.util.Arrays;

public class FrogJump {

    public static int frogJump(int[] heights){
        int n = heights.length;

        int[] dp = new int[n];
        dp[0] = 0;

        for (int i = 1; i < n; i++) {
            int jumpOne = dp[i - 1] + Math.abs(heights[i] - heights[i - 1]);

            int jumpTwo = Integer.MAX_VALUE;

            if (i > 1) {
                jumpTwo = dp[i-2] + Math.abs(heights[i] - heights[i-2]);
            }

            dp[i] = Math.min(jumpOne, jumpTwo);
        }

        return dp[n - 1];
    }

    public static int frogJumpWithK(int[] heights, int k){
        int n = heights.length;

        int[] dp = new int[n];

        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= k; j++) {
                if (i - j >= 0) {
                    int cost = dp[i - j] + Math.abs(heights[i] - heights[j]);
                    dp[i] = Math.min(dp[i], cost);
                }
            }
        }

        return dp[n - 1];
    }

    public static void main(String[] args) {
        int[] heights = {10, 30, 40, 20};
        System.out.println(frogJump(heights));
    }
}
