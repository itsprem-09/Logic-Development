// WAP to find weather given number is Kaprekar or not.
// A Kaprekar number is a non-negative integer that, when squared, can be split into two parts whose sum
// equals the original number.
// For E.g. 45 is a Kaprekar number because 45 squared (2025) can be split into 20 and 25, and 20 + 25 = 45.

#include <stdio.h>

void main(){
    int n = 10;
    
    int sq = n * n;
    int count = 0;
    int temp = n;
    while (temp > 0)
    {
        count++;
        temp /= 10;
    }
    int divisor = 1;
    for (int i = 0; i < count; i++)
    {
        divisor *= 10;
    }
    int rightPart = sq % divisor;
    int leftPart = sq / divisor;
    if (leftPart + rightPart == n)
    {
        printf("Kaprekar Number");
    }
    else
    {
        printf("Not Kaprekar Number");
    }
}