public class Rotate90 {
    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3, 4},
            {5, 6, 7, 8}
        };

        int n = matrix.length;  // number of rows
        int m = matrix[0].length;  // number of columns

        int[][] rotated = new int[m][n];  // new matrix for rotated version
        int[][] rotatedAntiClockwise = new int[m][n];  // new matrix for anti-clockwise rotation
        int[][] rotated180 = new int[n][m];  // new matrix for 180 degree rotation
        int[][] rotated180AntiClockwise = new int[n][m];  // new matrix for 180 degree anti-clockwise rotation

        // clock wise rotation: rotated[j][n - 1 - i] = matrix[i][j]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }

        // anti-clockwise rotation: rotatedAntiClockwise[m - 1 - j][i] = matrix[i][j]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotatedAntiClockwise[m - 1 - j][i] = matrix[i][j];
            }
        }

        // for 180 degree rotation, we can use the same logic as 90 degree rotation twice, or we can directly calculate the position of each element in the rotated matrix using the formula:
        // 180 degree rotation: rotated180[n - 1 - i][m - 1 - j] = matrix[i][j]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotated180[n - 1 - i][m - 1 - j] = matrix[i][j];
            }
        }

        // for 180 degree anti-clockwise rotation, we can use the same logic as 90 degree anti-clockwise rotation twice, or we can directly calculate the position of each element in the rotated matrix using the formula:
        // 180 degree anti-clockwise rotation: rotated180AntiClockwise[n - 1 - i][m - 1 - j] = matrix[i][j]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotated180AntiClockwise[n - 1 - i][m - 1 - j] = matrix[i][j];
            }
        }

        // Print the rotated matrix
        for (int i = 0; i < rotated.length; i++) {
            for (int j = 0; j < rotated[0].length; j++) {
                System.out.print(rotated[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Anti-clockwise rotation:");
        for (int i = 0; i < rotatedAntiClockwise.length; i++) {
            for (int j = 0; j < rotatedAntiClockwise[0].length; j++) {
                System.out.print(rotatedAntiClockwise[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("180 degree rotation:");
        for (int i = 0; i < rotated180.length; i++) {
            for (int j = 0; j < rotated180[0].length; j++) {
                System.out.print(rotated180[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("180 degree anti-clockwise rotation:");
        for (int i = 0; i < rotated180AntiClockwise.length; i++) {
            for (int j = 0; j < rotated180AntiClockwise[0].length; j++) {
                System.out.print(rotated180AntiClockwise[i][j] + " ");
            }
            System.out.println();
        }
    }
}
