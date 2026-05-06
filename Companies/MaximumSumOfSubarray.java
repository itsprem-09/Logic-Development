public class MaximumSumOfSubarray {

    public static int maxSubArraySum(int[] arr, int k){
        int maxSum = Integer.MIN_VALUE;

        for (int start = 0; start <= arr.length - k; start++) {

            int sum = 0;

            for (int stop = start; stop < start + k; stop++) {
                sum += arr[stop];
            }

            maxSum = Math.max(maxSum, sum);
        }

        return maxSum;
    }

    public static void main(String[] args) {
        int arr[] = {1, 2, 3, 4, 5, 6};
        System.out.println(maxSubArraySum(arr, 2));
    }
}
