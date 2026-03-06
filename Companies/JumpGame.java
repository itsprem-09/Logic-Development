public class JumpGame {

    public static boolean isReachable(int[] nums){
        int maxReach = 0;  // maximum index we can reach so far

        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) {
                return false; // can;t reach this index
            }

            maxReach = Math.max(maxReach, i + nums[i]);

            if (maxReach >= nums.length - 1) {
                return true; // already can reach last index
            }
        }
        
        return true;
    }

    public static void main(String[] args) {
        int[] nums = {3, 2, 1, 0, 4};

        System.out.println(isReachable(nums));
    }
}
