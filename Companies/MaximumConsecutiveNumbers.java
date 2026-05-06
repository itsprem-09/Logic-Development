import java.util.Arrays;
import java.util.HashSet;

public class MaximumConsecutiveNumbers {

    public static int usingSorting(int[] arr){
        Arrays.sort(arr);

        int maxLength = 1;
        int currLength = 1;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1]) {
                continue; // skip duplicates
            }

            else if (arr[i] == arr[i - 1] + 1) {
                currLength++;
            }

            else{
                maxLength = Math.max(maxLength, currLength);
                currLength = 1;
            }
        }

        return Math.max(maxLength, currLength); // check last sequense also
    }

    public static int usingSet(int[] arr){
        HashSet<Integer> set = new HashSet<>();

        for (int i = 0; i < arr.length; i++) {
            set.add(arr[i]);
        }

        int maxLen = 1;

        for (int num : set) {
            
            // start only when n-1 not exists
            if (!set.contains(num - 1)) {
                int len = 1;
                int curr = num;

                while (set.contains(curr + 1)) {
                    curr++;
                    len++;
                }

                maxLen = Math.max(maxLen, len);
            }   
        }

        return maxLen;
    }

    public static void main(String[] args) {
        int[] arr = {1, 94, 93, 1000, 5, 92, 78};
        System.out.println(usingSorting(arr)); // Output: 3
    }
}
