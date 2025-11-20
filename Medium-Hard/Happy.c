// Write a program to check whether number is Happy number or not.
// A happy number are those number whose digit’s square summation eventually reaches to 1, if the
// sequence start repeating then it is not a happy number.
// E.g., 49 is a happy number whose sequence is 49 97 130 10 1.
// E.g., 50 is not a happy number whose sequence is 50 25 29 85 89 145 42 20 4 16 37 58 89 it should stop
// when 89 is detected 2nd time and print 50 is not a happy number.

#include<stdio.h>

int squareOfDigits(int n){
    int sum = 0;
    while (n > 0)
    {
        int ld = n % 10;
        sum += ld * ld;
        n /= 10;
    }
    return sum;
}

// void main(){
//     int n = 50;
    
//     int sum = squareOfDigits(n);

//     while (sum != 4 && sum != 1)
//     {
//         n = sum;
//         sum = squareOfDigits(n);
//     }
    
//     if (sum == 1)
//     {
//         printf("Happy Number");
//     }
//     else{
//         printf("Not a happy Number");
//     }
    
    
// }

void main(){
    int n = 13;

    int slow = n;
    int fast = n;
    
    do
    {
        slow = squareOfDigits(slow); // move one step
        fast = squareOfDigits(squareOfDigits(fast)); // move two steps
    } while (slow != fast);

    if (slow == 1)
    {
        printf("Happy Number");
    }
    else{
        printf("Not a happy Number");
    }
    
}
