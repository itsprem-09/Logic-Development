package Popular;

import java.util.*;

class Solution {
    // Function to solve the linear house robber problem
    static int solve(int[] arr){

        int n = arr.length;

        int[] dp = new int[n];

        dp[0] = arr[0];

        for(int i = 1; i < n; i++){

            int pick = arr[i];

            if(i > 1)
                pick += dp[i-2];

            int skip = dp[i-1];

            dp[i] = Math.max(pick, skip);
        }

        return dp[n-1];
    }

    // Function to solve the circular house robber problem
    public int rob(int nums[]) {
        int n = nums.length;

        if(n == 1)
            return nums[0];

        int[] case1 = Arrays.copyOfRange(nums,0,n-1);
        int[] case2 = Arrays.copyOfRange(nums,1,n);

        return Math.max(solve(case1), solve(case2));
    }
}


public class HouseRobber2 {

    public static void main(String[] args) {
        int[] arr = {1, 5, 2, 1, 6};
        int n = arr.length;

        // Create Solution object
        Solution sol = new Solution();

        // Output result
        System.out.println(sol.rob(arr));
    }
}
