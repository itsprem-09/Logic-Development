// *        *
// **      **
// ***    ***
// ****  ****
// **********
// **********
// ****  ****
// ***    ***
// **      **
// *        *

#include <stdio.h>

int main(void) {
    int n;

    // Read size (number of rows in each half)
    printf("Enter size (e.g., 5): ");
    if (scanf("%d", &n) != 1 || n <= 0) {
        printf("Invalid input.\n");
        return 1;
    }

    // Top half
    for (int i = 1; i <= n; i++) {
        // Left wing
        for (int j = 1; j <= i; j++) printf("*");
        // Gap between wings
        for (int j = 1; j <= 2 * (n - i); j++) printf(" ");
        // Right wing
        for (int j = 1; j <= i; j++) printf("*");
        printf("\n");
    }

    // Bottom half (mirror)
    for (int i = n; i >= 1; i--) {
        // Left wing
        for (int j = 1; j <= i; j++) printf("*");
        // Gap between wings
        for (int j = 1; j <= 2 * (n - i); j++) printf(" ");
        // Right wing
        for (int j = 1; j <= i; j++) printf("*");
        printf("\n");
    }

    return 0;
}
