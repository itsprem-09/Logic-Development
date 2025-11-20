#include<stdio.h>

// void main(){
//     int n = 254;

//     int oct = 0;
//     int place = 1;

//     while (n > 0)
//     {
//         int rem = n % 8;
//         oct += rem * place;
//         place *= 10;
//         n /= 8;
//     }

//     printf("%d", oct);
// }

void main(){
    int n = 254;

    char octalDigits[] = "01234567";
    char octal[20];
    int index = 0;

    while (n > 0)
    {
        int rem = n % 8;
        octal[index] = octalDigits[rem];
        index++;
        n /= 8;
    }

    octal[index] = '\0';

    // Print in reverse order
    for (int i = index - 1; i >= 0; i--)
    {
        printf("%c", octal[i]);
    }
    
}