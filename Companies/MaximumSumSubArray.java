public class MaximumSumSubArray {

    public static int findMaxSumSubArray(int[] arr, int k){
        int n = arr.length;
        int maxSum = 0;

        for (int i = 0; i <= n - k; i++) {
            int sum = 0;
            for (int j = 0; j < k; j++) {
                sum += arr[i+j];
            }
            maxSum = Math.max(maxSum, sum);
        }

        return maxSum;
    }

    public static void main(String[] args) {
        
        int arr[] = {1, 2, 3, 4, 5, 6};

        System.out.println(findMaxSumSubArray(arr, 2));
    }
}
