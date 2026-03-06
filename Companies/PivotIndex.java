public class PivotIndex {

    public static int findPivot(int[] arr){        
        for (int i = 0; i < arr.length; i++) {
            int leftSum = 0;
            int rightSum = 0;

            for (int j = 0; j < i; j++) {
                leftSum += arr[j];
            }

            for (int j = i + 1; j < arr.length; j++) {
                rightSum += arr[j];
            }

            if (leftSum == rightSum) {
                return i;
            }
        }

        return -1;
    }

    // this is efficient solution with O(n) time complexity and O(1) space complexity
    public static int findPivotEfficient(int[] arr) {
        int totalSum = 0;
        for (int num : arr) {
            totalSum += num;
        }

        int leftSum = 0;
        for (int i = 0; i < arr.length; i++) {
            int rightSum = totalSum - leftSum - arr[i];

            if (leftSum == rightSum) {
                return i;
            }

            leftSum += arr[i];
        }

        return -1;
    }

    public static void main(String[] args) {
        int arr[] = {1, 7, 3, 6, 5, 6};

        System.out.println(findPivotEfficient(arr));
    }
}
