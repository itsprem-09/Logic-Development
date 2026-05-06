import java.util.Arrays;
import java.util.HashSet;


public class IntersectionOfArray {

    public static int[] intersection(int[] nums1, int[] nums2){
        HashSet<Integer> set = new HashSet<>();
        HashSet<Integer> res = new HashSet<>();

        for (int i : nums1) {
            set.add(i);
        }

        for (int i : nums2) {
            if(set.contains(i)){
                res.add(i);
            }
        }

        int[] ans = new int[res.size()];
        int k = 0;
        for (int i : res) {
            ans[k++] = i;
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 2, 1};
        int[] nums2 = {2, 2};

        System.out.println(Arrays.toString(intersection(nums1, nums2)));

    }
}
