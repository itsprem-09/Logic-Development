// Given an integer n, return the least number of perfect square numbers that sum to n.
// Input: n = 12 Output: 3 Explanation: 12 = 4 + 4 + 4.
// Input: n = 13 Output: 2 Explanation: 13 = 4 + 9.

#include <stdio.h>

int min(int a, int b) {
    return (a < b) ? a : b;
}

int numSquares(int n) {
    int dp[n + 1];
    dp[0] = 0;

    for (int i = 1; i <= n; i++) {
        dp[i] = n; // Initialize with a large number
        for (int j = 1; j * j <= i; j++) {
            dp[i] = min(dp[i], dp[i - j * j] + 1);
        }
    }

    return dp[n];
}

int main() {
    int n = 13;
    printf("Least number of perfect square numbers summing to %d is %d\n", n, numSquares(n));
    return 0;
}