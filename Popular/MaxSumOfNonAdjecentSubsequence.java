package Popular;

public class MaxSumOfNonAdjecentSubsequence {

    public static int maxSum(int[] arr){
        int n = arr.length;

        int[] dp = new int[n];
        dp[0] = arr[0];

        for (int i = 1; i < n; i++) {
            int pick = arr[i];

            if (i > 1) {
                pick += arr[i - 2];
            }

            int notPick = arr[i - 1];

            dp[i] = Math.max(pick, notPick);
        }

        return dp[n -1];

    }

    public static void main(String[] args) {
        
    }
}
