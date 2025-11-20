// Given an integer n, return the nth digit of the infinite integer sequence [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, …].
// Input: n = 11 => Output: 0
// Explanation: The 11th digit of the sequence 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... is a 0, which is part of the
// number 10.

#include <stdio.h>

int findNthDigit(int n){
    char str[12];
    int count = 0, num = 1;
    while(count < n){
        sprintf(str, "%d", num);
        for(int i = 0; str[i] != '\0'; i++){
            count++;
            if(count == n){
                return str[i] - '0';
            }
        }
        num++;
    }
    return -1; // This line should never be reached
}

int findNthDigitOptimized(int n) {
    int digit_length = 1;
    long long count = 9;
    int start = 1;

    while (n > digit_length * count) {
        n -= digit_length * count;
        digit_length++;
        count *= 10;
        start *= 10;
    }

    start += (n - 1) / digit_length;
    char str[12];
    sprintf(str, "%d", start);
    return str[(n - 1) % digit_length] - '0';
}

int main() {
    int n = 189;
    printf("%d\n", findNthDigit(n));
    return 0;
}
