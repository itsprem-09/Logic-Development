package Popular;

public class MaxConsecutiveOnesOrZeros {

    public static int maxOnesOrZero(int[] arr){
        int maxOne = 0;
        int maxZero = 0;

        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] == 1) {
                int left = i;
                int right = left + 1;
                int count = 1;

                while (right < arr.length && arr[right] == 1) {
                    count++;
                    right++;
                }

                maxOne = Math.max(maxOne, count);
            }
            else {
                int left = i;
                int right = left + 1;
                int count = 1;

                while (right < arr.length && arr[right] == 0) {
                    count++;
                    right++;
                }

                maxZero = Math.max(maxZero, count);
            }
        }
        return Math.max(maxOne, maxZero);
    }

    public static int maxOnesOrZerosEfficient(int[] arr){
        int max = 0;
        int count = 1;
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            if (arr[i] == arr[i - 1]) {
                count++;
            }
            else{
                max = Math.max(max, count);
                count = 1;
            }
        }
        return Math.max(max, count);
    }

    public static void main(String[] args) {
        int arr[] = {0, 0, 0, 0};

        System.out.println(maxOnesOrZero(arr));
    }
}
