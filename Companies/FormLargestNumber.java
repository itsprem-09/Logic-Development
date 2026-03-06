import java.util.Arrays;

public class FormLargestNumber {

    private static String formLargestNumber(int[] nums){
        String[] strNums = new String[nums.length];

        for (int i = 0; i < nums.length; i++) {
            strNums[i] = nums[i] + "";  // convert each number to string
        }

        // sort the string array based on the custom comparator
        Arrays.sort(strNums, (a, b) -> (b + a).compareTo(a + b));  // sort in descending order based on the concatenated string
        
        // if the largest number is 0, return "0"
        if (strNums[0].equals("0")) {
            return "0";
        }

        StringBuilder largestNumber = new StringBuilder();
        for (String str : strNums) {
            largestNumber.append(str);
        }
        return largestNumber.toString();
    }

    public static void main(String[] args) {
        int[] nums = {12, 121, 9, 34, 3};
        System.out.println(formLargestNumber(nums));
    }
}
