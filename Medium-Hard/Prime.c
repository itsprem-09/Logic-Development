#include<stdio.h>
#include<math.h>

int isPrime(int n){
    if (n <= 1)
    {
        return 0; // 1 is not a prime number
    }

    for (int i = 2; i <= sqrt(n); i++)
    {
        if (n % i == 0)
        {
            return 0; // n is divisible by i, so it's not prime
        }        
    }
    return 1; // n is prime  
}

void main(){
    int n = 0; // Example number to check
    if (isPrime(n))
    {
        printf("%d is a prime number\n", n);
    }
    else
    {
        printf("%d is not a prime number\n", n);
    }
}