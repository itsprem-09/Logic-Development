public class MaxTopOfPile {

    public static int maximumTop(int[] nums, int k){
        int n = nums.length;

        // single element
        if (n == 1) {
            // if k is odd we can't replace that element again into the array -> -1
            // if k is even we can replace that element into the array -> nums[0]
            return (k % 2 == 1) ? -1 : nums[0]; 
        }

        // If k == 0, no moves
        if (k == 0) {
            return nums[0];
        }

        int max = 0;
        int limit = Math.min(k, n - 1);

        for (int i = 0; i <= limit; i++) {
            max = Math.max(max, nums[i]);
        }
        return max;
    }

    public static void main(String[] args) {
        
    }
}
