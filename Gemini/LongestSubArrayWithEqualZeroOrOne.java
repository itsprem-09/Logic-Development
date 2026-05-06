package Gemini;

public class LongestSubArrayWithEqualZeroOrOne {

    public static int longestSubArray(int[] arr){
        int n = arr.length;
        int maxLen = 0;

        for (int i = 0; i < arr.length; i++) {
            int zeros = 0;
            int ones = 0;
            for (int j = i; j < arr.length; j++) {
                if (arr[j] == 0) {
                    zeros++;
                }
                else{
                    ones++;
                }

                if (zeros == ones) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }
        return maxLen; 
    }

    public static void main(String[] args) {
        
    }
}
