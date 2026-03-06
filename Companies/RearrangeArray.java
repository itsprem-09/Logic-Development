import java.util.Arrays;

public class RearrangeArray {

    public static int[] rearrangeArrayWithConstantSpace(int[] arr){
        int mid = arr.length / 2;

        for (int i = 0; i < mid; i++) {
            int element = arr[mid + i];

            // shift element from mid
            for (int j = mid + i; j > 2 * i + 1; j--) {
                arr[j] = arr[j - 1];
            }

            arr[2 * i + 1] = element;
        }

        return arr;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6};

        rearrangeArrayWithConstantSpace(arr);

        System.out.println(Arrays.toString(arr));
    }
}
