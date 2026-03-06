public class LEGOTower {

    // works on almost every cases
    public static int findMaxHeight(int[] red, int[] blue) {
        int redSum = 0;
        int blueSum = 0;

        for (int i = 0; i < red.length; i++) {
            if (i % 2 == 0) {
                redSum += red[i];
            } else {
                redSum += blue[i];
            }
        }

        for (int i = 0; i < blue.length; i++) {
            if (i % 2 == 0) {
                blueSum += blue[i];
            } else {
                blueSum += red[i];
            }
        }

        return redSum > blueSum ? redSum : blueSum;
    }

    public static int findMaxHeightEfficient(int[] red, int[] blue) {
        int n = red.length;

        int dpRed = red[0];
        int dpBlue = blue[0];

        for (int i = 1; i < n; i++) {

            int newRed = red[i] + dpBlue;
            int newBlue = blue[i] + dpRed;

            dpRed = newRed;
            dpBlue = newBlue;
        }

        return Math.max(dpRed, dpBlue);
    }

    public static void main(String[] args) {
        int[] red = { 2, 7, 4, 1 };
        int[] blue = { 3, 5, 6, 2 };

        System.out.println(findMaxHeight(red, blue));
        System.out.println(findMaxHeightEfficient(red, blue));
    }
}