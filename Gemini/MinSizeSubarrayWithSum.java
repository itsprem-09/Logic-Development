package Gemini;

public class MinSizeSubarrayWithSum {

    public static int minSubArray(int[] arr, int target){
        int n = arr.length;
        int minLen = Integer.MAX_VALUE;

        for (int start = 0; start < n; start++) {
            int sum = 0;
            for (int stop = start; stop < n; stop++) {
                sum += arr[stop];

                if (sum >= target) {
                    minLen = Math.min(minLen, stop - start + 1);
                    break;
                }
            }
        }

        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    // this is o(n) solution
    public static int minSubArray2(int[] arr, int target){
        int minLen = Integer.MAX_VALUE;
        int left = 0;
        int sum = 0;
        
        for (int right = 0; right < arr.length; right++) {
            sum += arr[right];

            while (sum >= target) {
                minLen = Math.min(minLen, right - left + 1);
                sum -= arr[left];
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    public static void main(String[] args) {
        
    }
}
