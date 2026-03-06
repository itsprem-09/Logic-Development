public class RepeatedNumber {

    public static int findRepeat(int[] nums){
        boolean[] visited = new boolean[nums.length];

        for (int i = 0; i < nums.length; i++) {
            if (visited[nums[i]]) {
                return nums[i];   // return duplicate number
            }

            visited[nums[i]] = true;
        }

        return -1;
    }

    // this is O(n) time and O(1) space solution
    public static int findDuplicate(int[] nums) {
        int slow = nums[0];
        int fast = nums[0];

        // Find the intersection point in the cycle
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);

        // Find the entrance to the cycle
        slow = nums[0];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }

        return slow;
    }

    // this is also O(n) time and O(1) space solution using negative marking
    public static int findDuplicateNegativeMarking(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int index = Math.abs(nums[i]);

            if (nums[index] < 0) {
                return index;   // return duplicate number
            }

            nums[index] = -nums[index];  // mark as visited
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {3, 1, 3, 4, 2};

        System.out.println(findRepeat(nums));
        System.out.println(findDuplicate(nums));
        System.out.println(findDuplicateNegativeMarking(nums));
    }
}
