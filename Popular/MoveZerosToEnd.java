package Popular;

import java.util.Arrays;

public class MoveZerosToEnd {

    public static void moveZeros(int[] arr){
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            if (arr[i] == 0) {
                int j = i + 1;
                while (j < n && arr[j] == 0) {
                    j++;
                }
                if (j < n) {
                    int t = arr[i];
                    arr[i] = arr[j];
                    arr[j] = t;
                }
            }
        }
    }

    public static void moveZerosOther(int[] arr){
        int pos = 0;
        int n = arr.length;

        for (int i = 0; i < n; i++) {
            if (arr[i] != 0) {
                int t = arr[pos];
                arr[pos] = arr[i];
                arr[i] = t;
                pos++;
            }
        }
    }

    public static void moveZerosSimplest(int[] arr){
        int res[] = new int[arr.length];
        int index = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                res[index++] = arr[i];
            }
        }

        for (int i = index; i < arr.length; i++) {
            res[i] = 0;
        }

        for (int i = 0; i < arr.length; i++) {
            arr[i] = res[i];
        }
    }

    public static void main(String[] args) {
        int arr[] = {1, 2, 0, 4, 3, 0, 5, 0};

        moveZerosOther(arr);

        System.out.println(Arrays.toString(arr));
    }
}
