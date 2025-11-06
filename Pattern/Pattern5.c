// 15
// 7 14
// 6 8 13
// 2 5 9 12
// 1 3 4 10 11

#include <stdio.h>

int main(void) {
    int n;
    printf("Enter. N = ");
    if (scanf("%d", &n) != 1 || n <= 0) return 0;

    // allocate (N+1)x(N+1) so we can use 1-based indexing
    int a[n + 1][n + 1];
    for (int i = 0; i <= n; ++i) {
        for (int j = 0; j <= n; ++j) {
            a[i][j] = 0;
        }
    }

    int num = 1;

    // Fill diagonals starting from the bottom row (column 1..N)
    // For odd c: fill bottom->top (up-left). For even c: fill top->bottom (down-right).
    for (int col = 1; col <= n; col++) {

        if (col % 2 == 1) {
            // Odd diagonal → bottom to top
            int i = n, j = col;
            while (i >= 1 && j >= 1)
                a[i--][j--] = num++;
        }
        else {
            // Even diagonal → top to bottom
            int i = n - (col - 1), j = 1;
            while (i <= n && j <= col)
                a[i++][j++] = num++;
        }
    }

    // Print the pattern
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= i; j++)
            printf("%d ", a[i][j]);
        printf("\n");
    }

    return 0;
}

// int main(void) {
//     int N;
//     printf("Enter. N = ");
//     if (scanf("%d", &N) != 1 || N <= 0) return 0;

//     for (int i = 1; i <= N; ++i) {
//         for (int j = 1; j <= i; ++j) {
//             // diagonal index for (i,j) measured from the bottom: 1..N
//             int c = j + (N - i);

//             // first number on diagonal c (sum of lengths of previous diagonals + 1)
//             int start = 1 + (c - 1) * c / 2;

//             // offset within the diagonal: odd c go bottom->top, even c go top->bottom
//             int k = (c % 2) ? (N - i) : (j - 1);

//             int val = start + k;

//             if (j > 1) printf(" ");
//             printf("%d", val);
//         }
//         printf("\n");
//     }
//     return 0;
// }
