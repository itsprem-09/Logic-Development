package Popular;

import java.util.Arrays;

public class WaveFormArray {

    public static void sortArrayInWaveForm(int[] arr){
        int n = arr.length;

        for (int i = 0; i < n - 1; i+=2) {
            int t = arr[i];
            arr[i] = arr[i + 1];
            arr[i + 1] = t;
        }
    }

    public static void main(String[] args) {
        int arr[] = {1, 2, 3, 4, 5, 6};

        sortArrayInWaveForm(arr);

        System.out.println(Arrays.toString(arr));
    }
}
