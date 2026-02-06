package Pattern;
// 1
// 1 2 1
// 1 * 3 * 1
// 1 * * 4 * * 1
// 1 * * * 5 * * * 1
// 1 * * * * 6 * * * * 1

public class Pattern15 {
    public static void main(String[] args) {
        int rows = 6;

        for (int i = 1; i <= rows; i++) {

            // First 1
            System.out.print("1");

            if (i > 1) {
                // Left stars
                for (int j = 1; j < i - 1; j++) {
                    System.out.print(" *");
                }

                // Middle number
                System.out.print(" " + i);

                // Right stars
                for (int j = 1; j < i - 1; j++) {
                    System.out.print(" *");
                }

                // Last 1
                System.out.print(" 1");
            }

            System.out.println();
        }
    }
}
