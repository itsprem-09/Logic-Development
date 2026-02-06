public class IsInArrayRecursion {

    private static int isInArray(int[] a, int m ){
        // check if array is empty
        if (a.length == 0) {
            return 0;
        }

        // check the first element
        if (a[0] == m) {
            return 1;
        }

        // create a smaller array
        int[] smaller = new int[a.length - 1];
        for (int i = 1; i < smaller.length; i++) {
            smaller[i - 1] = a[i];
        }

        return isInArray(smaller, m);
    }

    public static void main(String[] args) {
        System.out.println(isInArray(new int[]{1, 2, 3, 4}, 5));
    }
}
