package Gemini;

public class MoveZerosToEnd {

    private static void moveZeros(int[] arr){
        int n = arr.length;
        int k = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                arr[k] = arr[i];
                k++;
            }
        }

        for (int i = k; i < n; i++) {
            arr[i] = 0;
        }
    }

    public static void main(String[] args) {
        
    }
}
