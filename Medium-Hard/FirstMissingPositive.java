public class FirstMissingPositive {

    private static int firstMissingPositive(int[] arr){
        int n = arr.length;

        for (int i = 0; i < arr.length; i++) {
            while (arr[i] > 0 && arr[i] <= n && arr[arr[i] - 1] != arr[i]) {
                int t = arr[i];
                arr[i] = arr[t - 1];
                arr[t - 1] = t;
            }
        }

        // Step 2: Find the first missing positive
        for (int i = 0; i < n; i++) {
            if (arr[i] != i + 1)
                return i + 1;
        }
        return n+1;
    } 

    public static void main(String[] args) {
        System.out.println(firstMissingPositive(new int[]{1,2,0}));
    }
}
