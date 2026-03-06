import java.util.Arrays;
import java.util.Stack;

public class NextGreaterElement {

    public static int[] nextGreater(int[] arr){
        int[] ans = new int[arr.length];

        // initialized with -1
        for (int i = 0; i < ans.length; i++) {
            if (ans[i] == 0) {
                ans[i] = -1;
            }
        }

        for (int i = 0; i < ans.length - 1; i++) {
            for (int j = i + 1; j < ans.length; j++) {
                if (arr[j] > arr[i]) {
                    ans[i] = arr[j];
                    break;
                }
            }
        }


        return ans;
    }

    public static int[] nextGreaterElementOptimized(int[] arr){
        int[] ans = new int[arr.length];
        Stack<Integer> st = new Stack<>();
        Arrays.fill(ans, -1);

        for (int i = arr.length - 1; i >= 0; i--) {
            while (!st.isEmpty() && st.peek() <= arr[i]) {
                st.pop();
            }

            if (!st.isEmpty()) {
                ans[i] = st.peek();
            }

            st.push(arr[i]);
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] arr = {6, 8, 0, 1, 3};

        System.out.println(Arrays.toString(nextGreater(arr)));
    }
}
