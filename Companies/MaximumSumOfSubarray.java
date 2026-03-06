public class MaximumSumOfSubarray {

    public static int findMaxSum(int[] arr, int k){
        int maxSum = 0;

        int n = arr.length;
        for (int i = 0; i <= n - k; i++) {
            int sum = 0;
            for (int j = 0; j < k; j++) {
                sum += arr[i + j];
            }
            maxSum = Math.max(maxSum, sum);
        }

        return maxSum;
    }

    public static void main(String[] args) {
        int arr[] = {1, 2, 3, 4, 5, 6};
        System.out.println(findMaxSum(arr, 2));
    }
}
