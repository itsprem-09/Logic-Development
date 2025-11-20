// Take an Input in the form of Binary String that contains only 0’s and 1’s and convert this number into
// integer.
// Input: 101.110
// Output: 5.75

#include <stdio.h>
#include <math.h>
#include <string.h>

void main(){
    char binaryStr[] = "101.110";
    double decimal = 0.0;

    // Process integer part
    int dot = 0;
    for (int i = 0; binaryStr[i] != '\0'; i++)
    {
        if (binaryStr[i] == '.') {
            dot = i;
            break;
        }
    }
    
    // If there is no decimal point, treat the entire string as integer part
    if (dot == -1)
    {
        dot = strlen(binaryStr);
    }
    
    // Convert integer part
    for (int i = 0; i < dot; i++)
    {
        if (binaryStr[i] == '1')
        {
            decimal += pow(2, dot - i - 1);
        }    
    }

    // Convert fractional part
    for (int i = dot + 1; binaryStr[i] != '\0'; i++)
    {
        if (binaryStr[i] == '1')
        {
            decimal += pow(2, dot - i);
        }    
    }
    
    printf("Decimal value: %.6f\n", decimal);

}