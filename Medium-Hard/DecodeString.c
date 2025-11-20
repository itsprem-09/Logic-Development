// Write a program that should decode the given pattern and print the resulting expanded string.
// Input: 2a3bc4dE5F2G7H
// Output: aabbbcddddEFFFFFGGHHHHHHH

#include <stdio.h>
#include <ctype.h>

void main(){
    char str[] = "2a3bc4dE5F2G12H";
    int index = 0;

    while (str[index] != '\0')
    {
        if (isdigit(str[index]))
        {
            int count = 0;
            while (isdigit(str[index]))
            {
                count = count * 10 + (str[index] - '0');
                index++;
            }
            
            char ch = str[index];
            for (int i = 0; i < count; i++)
            {
                printf("%c", ch);
            }

            index++;            
        }
        else{
            printf("%c", str[index]);
            index++;
        }  
    }
    
}