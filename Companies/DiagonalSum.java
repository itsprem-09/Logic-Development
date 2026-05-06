public class DiagonalSum {

    public static void findDiagonalSums(int[][] mat){
        int principal = 0;
        int secondary = 0;

        for (int i = 0; i < mat.length; i++) {
            principal += mat[i][i];
            secondary += mat[i][mat.length - 1 - i];
        }

        System.out.println("Principal diagonal sum: " + principal);
        System.out.println("Secondary diagonal sum: " + secondary);
    }

    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3, 4},
            {4, 3, 2, 1},
            {7, 8, 9, 6},
            {6, 5, 4, 3}
        };

        findDiagonalSums(matrix);
    }
}
