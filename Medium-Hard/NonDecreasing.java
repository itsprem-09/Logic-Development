public class NonDecreasing {
    public static boolean checkPossibility(int[] nums) {

        for (int i = 0; i < nums.length; i++) {
            if (isNonDecreasingAfterRemoval(nums, i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNonDecreasingAfterRemoval(int[] nums, int skip) {
        int prev = Integer.MIN_VALUE;

        for (int i = 0; i < nums.length; i++) {
            if (i == skip) continue;

            if (nums[i] < prev) return false;
            prev = nums[i];
        }
        return true;
    }
}
