// WAP to find weather given number is Automorphic or not.
// An automorphic number is a number whose square ends with the same digits as the number itself.
// For example, 5 is automorphic because 5² = 25, which ends in 5.
// Similarly, 76 is automorphic because 76² = 5776, which ends in 76

#include <stdio.h>
void main(){
    int n = 76;

    int sq = n * n;
    int temp = n;
    int count = 0;

    // Count number of digits in n
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

    // Get the last 'count' digits of the square
    int lastDigits = sq % divisor;

    if (lastDigits == n)
    {
        printf("Automorphic Number");
    }
    else
    {
        printf("Not Automorphic Number");
    }
}