import java.util.Arrays;

public class CoinChange {

    public static int minCoins(int target, int[] coins){
        int dp[] = new int[target + 1];

        Arrays.fill(dp, target+1);
        dp[0] = 0;

        for (int i = 1; i <= target; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[target] > target ? -1 : dp[target];
    }

    // this approach does not work for all cases, it fails for cases like target = 6 and coins = [1, 3, 4]
    public static int minCoinsGreedy(int target, int[] coins){
        int count = 0;

        for (int i = coins.length - 1; i >= 0; i--) {
            if (coins[i] <= target) {
                int numCoins = target / coins[i];  // how many coins we can take
                count += numCoins;  // add to count
                target -= numCoins * coins[i];  // reduce the target by the amount covered by
            }
        }

        if (target != 0) {
            return -1;  // if target is not 0, it means we couldn't make change
        }

        return count;
    }

    public static void main(String[] args) {
        int amount = 27;
        int[] denominations = {1, 2, 5, 10};

        System.out.println(minCoins(amount, denominations));

    }
}
