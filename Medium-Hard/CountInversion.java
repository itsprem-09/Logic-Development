// This problem asks to count the number of inversions in an array. An inversion in an array is a pair of indices (i, j) such that i < j and arr[i] > arr[j]. The goal is to count how many such pairs exist in the given array.

// Example :

// Given the array [1, 20, 6, 4, 5], we count the number of inversions where a larger number appears before a smaller one.
// The inversions are: (20, 6), (20, 4), (20, 5), (6, 4), and (6, 5), totaling 5 inversions.
// Each inversion is counted whenever a pair (i, j) with i < j and arr[i] > arr[j] is found. Thus, the result is 5.

public class CountInversion {

    public static int countInversion(int arr[]){
        int cnt = 0;

        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    cnt++;
                }
            }
        }

        return cnt;
    }


    public static void main(String[] args) {
        System.out.println(countInversion(new int[]{1, 20, 6, 4, 5}));
    }
}
