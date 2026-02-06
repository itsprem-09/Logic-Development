import java.util.Arrays;
import java.util.TreeSet;

public class KthSmallestElement {

    public static int kthDistinctSmallest2(int[] arr, int k) {
        Arrays.sort(arr);

        int distinctCount = 0;
        int prev = Integer.MIN_VALUE;

        for (int num : arr) {
            if (num != prev) {   // new distinct element
                distinctCount++;
                prev = num;

                if (distinctCount == k) {
                    return num;
                }
            }
        }

        return -1; // if k > number of distinct elements
    }

    public static int kthDistinctSmallest(int[] nums, int k){
        int n = nums.length;
        TreeSet<Integer> set = new TreeSet<>();
        
        for (int num : nums) {
            set.add(num);
        }

        for (Integer dist : set) {
            k--;
            if (k == 0) {
                return dist;
            }
        }

        return -1;

    }

    public static void main(String[] args) {
        
    }
}
