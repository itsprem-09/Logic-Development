import java.util.Arrays;

public class CoinChange {

    private static int minCoins(int[] coins, int target){
        int[] dp = new int[target+1];

        Arrays.fill(dp, target+1);

        dp[0] = 0;

        for (int i = 1; i <= target; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], 1 + dp[i - coin]);
                }
            }
        }

        return dp[target] > target ? -1 : dp[target];
    }

    public static void main(String[] args) {
        int[] coins1 = {25, 10, 5};
        System.out.println(minCoins(coins1, 30)); // 2

        int[] coins2 = {9, 6, 5, 1};
        System.out.println(minCoins(coins2, 19)); // 3
    }
}
