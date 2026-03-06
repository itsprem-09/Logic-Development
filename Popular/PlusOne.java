package Popular;

public class PlusOne {

    public static int[] addOne(int[] arr){
        int n = arr.length;

        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] < 9) {
                arr[i]++;
                return arr;
            }
            arr[i] = 0;
        }

        // If all digits are 9, we need to add a new digit at the beginning
        int[] newArr = new int[n + 1];
        newArr[0] = 1; // Set the first digit to 1, the rest will be 0 by default
        return newArr;
    }

    public static void main(String[] args) {
        int arr[] = {9, 9, 9};

        System.out.println(addOne(arr));
    }
}
