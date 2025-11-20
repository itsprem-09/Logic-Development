// You are given a 0-indexed array nums consisting of positive integers. You can choose two indices i and j,
// such that i != j, and the sum of digits of the number nums[i] is equal to that of nums[j]. Return the
// maximum value of nums[i] + nums[j] that you can obtain over all possible indices i and j that satisfy the
// conditions.
// Input: nums = [18,43,36,13,7] Output: 54
// Explanation: The pairs (i, j) that satisfy the conditions are:
// - (0, 2), both numbers have a sum of digits equal to 9, and their sum is 18 + 36 = 54.
// - (1, 4), both numbers have a sum of digits equal to 7, and their sum is 43 + 7 = 50.
// So the maximum sum that we can obtain is 54.

#include <stdio.h>

int digitSum(int num) {
    int sum = 0;
    while (num > 0) {
        sum += num % 10;
        num /= 10;
    }
    return sum;
}

void main(){
    int nums[] = {18, 43, 36, 13, 7};
    int n = 5;

    int maxSum = -1;
    for (int i = 0; i < n - 1; i++)
    {
        for (int j = i + 1; j < n; j++)
        {
            if (digitSum(nums[i]) == digitSum(nums[j])) {
                int currentSum = nums[i] + nums[j];
                if (currentSum > maxSum) {
                    maxSum = currentSum;
                }
            }
        }       
    }

    printf("Maximum sum: %d\n", maxSum);
    
}