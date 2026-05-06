public class MaximumStudentTakingExam {
    int max = 0;

    public int maxStudents(char[][] seats) {
        dfs(seats, 0, 0, 0);
        return max;
    }

    public void dfs(char[][] seats, int row, int col, int count){
        int m =  seats.length;
        int n = seats[0].length;

        // Move to next row
        if (col == n) {
            row++;
            col = 0;
        }

        // End condition
        if (row == m) {
            max = Math.max(max, count);
            return;
        }

        // Option 1: Skip this seat
        dfs(seats, row, col + 1, count);    
        
        // Option 2: Try placing student
        if (canPlace(seats, row, col)) {
            seats[row][col] = 'S';
            dfs(seats, row, col + 1, count + 1);
            seats[row][col] = '.';
        }
    }

    private boolean canPlace(char[][] seats, int row, int col){
        if (seats[row][col] != '.') {
            return false;
        }

        int m = seats.length;
        int n = seats[0].length;

        if (col > 0 && seats[row][col - 1] == 'S') {
            return false;
        }

        if (col < n - 1 && seats[row][col + 1] == 'S') {
            return false;
        }

        if (col > 0 && row > 0 && seats[row - 1][col - 1] == 'S') {
            return false;
        }

        if (row > 0 && col < n-1 && seats[row - 1][col + 1] == 'S') {
            return false;
        }


        return true;
    }

    
    public static void main(String[] args) {
        char[][] seats = {
            {'.', '#', '.', '.', '#'},
            {'.', '.', '#', '.', '.'},
            {'#', '.', '.', '#', '.'}
        };

        MaximumStudentTakingExam solution = new MaximumStudentTakingExam();
        System.out.println(solution.maxStudents(seats));
    }
}
