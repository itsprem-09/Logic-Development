package Popular;

import java.util.Arrays;

public class MaximumProductOfTriplet {

    public static int maxProduct(int[] arr){
        Arrays.sort(arr);

        // two negative and one positive or three positive
        int case1 = arr[0] * arr[1] * arr[arr.length - 1];
        int case2 = arr[arr.length - 1] * arr[arr.length - 2] * arr[arr.length - 3];

        return Math.max(case1, case2);
    }

    public static int maxProduct2(int[] arr){
        int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE, max3 = Integer.MIN_VALUE;
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;

        for (int num : arr) {
            if (num > max1) {
                max3 = max2;
                max2 = max1;
                max1 = num;
            } else if (num > max2) {
                max3 = max2;
                max2 = num;
            } else if (num > max3) {
                max3 = num;
            }

            if (num < min1) {
                min2 = min1;
                min1 = num;
            } else if (num < min2) {
                min2 = num;
            }
        }

        return Math.max(max1 * max2 * max3, min1 * min2 * max1);
    }

    public static void main(String[] args) {
        int[] arr = {-10, -10, 5, 2};

        System.out.println(maxProduct(arr));
        System.out.println(maxProduct2(arr));
    }
}