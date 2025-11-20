// WAP for following Scenario.
// A child is running up a staircase with n steps and can hop either 1 step, 2 steps, or 3 steps at a time. The
// task is to implement a method to count how many possible ways the child can run up the stairs

#include <stdio.h>


// Approach - 1 : Using Recursion
// a person can reach nth stair by taking 1 step from (n-1)th stair or
// 2 steps from (n-2)th stair or 3 steps from (n-3)th stair.
int countWays(int n) {
    if (n < 0) {
        return 0;
    }
    if (n == 0) {
        return 1;
    }
    return countWays(n - 1) + countWays(n - 2) + countWays(n - 3);
}

// Approach - 2 : Using Dynamic Programming
int countWaysDP(int n) {
    int ways[n + 1];
    ways[0] = 1; // 1 way to stay at the ground (do nothing)
    ways[1] = 1; // 1 way to reach the first step
    ways[2] = 2; // 2 ways to reach the second step (1+1, 2)

    for (int i = 3; i <= n; i++) {
        ways[i] = ways[i - 1] + ways[i - 2] + ways[i - 3];
    }

    return ways[n];
}

// Approach - 3 : Using Space Optimization
int countWaysOptimized(int n) {
    if (n == 0) return 1;
    if (n == 1) return 1;
    if (n == 2) return 2;

    int prev3 = 1; // ways to reach (n-3)th step
    int prev2 = 1; // ways to reach (n-2)th step
    int prev1 = 2; // ways to reach (n-1)th step
    int totalWays = 0;

    for (int i = 3; i <= n; i++) {
        totalWays = prev1 + prev2 + prev3;
        prev3 = prev2;
        prev2 = prev1;
        prev1 = totalWays;
    }

    return totalWays;
}
    
void main() {
    int n = 5; // Number of steps

    // Using Recursion
    int waysRecursion = countWays(n);
    printf("Number of ways (Recursion): %d\n", waysRecursion);

    // Using Dynamic Programming
    int waysDP = countWaysDP(n);
    printf("Number of ways (Dynamic Programming): %d\n", waysDP);

    // Using Space Optimization
    int waysOptimized = countWaysOptimized(n);
    printf("Number of ways (Space Optimized): %d\n", waysOptimized);
}