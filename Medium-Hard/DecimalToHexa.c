#include<stdio.h>

void main(){
    int n = 254;

    char hexaDigits[] = "0123456789ABCDEF";
    char hexa[20];
    int index = 0;

    while (n > 0)
    {
        int rem = n % 16;
        hexa[index] = hexaDigits[rem];
        index++;
        n /= 16;
    }

    hexa[index] = '\0';

    // Print in reverse order
    for (int i = index - 1; i >= 0; i--)
    {
        printf("%c", hexa[i]);
    }
    
}