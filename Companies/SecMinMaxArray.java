public class SecMinMaxArray {
    public static void main(String[] args) {
        int arr[] = {10, 5, 20, 8, 15};

        int min =  Integer.MAX_VALUE;
        int secMin = arr[0];
        int max = Integer.MIN_VALUE;
        int secMax = arr[0];

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                secMax = max;
                max = arr[i];
            }
            else if (arr[i] > secMax && arr[i] != max) {
                secMax = arr[i];
            }
        }

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                secMin = min;
                min = arr[i];
            }
            else if (arr[i] < secMax && arr[i] != max) {
                secMin = arr[i];
            }
        }

        System.out.println("Max : "+max);
        System.out.println("Min : "+min);
        System.out.println("Second Max : "+secMax);
        System.out.println("Second Min : "+secMin);
    }
}
