//    1
//   212
//  32123
// 4321234

#include<stdio.h>
void main(){
    int n = 4;
    for (int i = 1; i <= n; i++)
    {
        for (int j = 1; j <= 2 * (n - i); j++)
        {
            printf(" ");
        }
        for (int j = i; j >= 1; j--)
        {
            printf("%d ", j);
        }
        for (int j = 2; j <= i; j++)
        {
            printf("%d ", j);
        }    
        printf("\n");      
    }
    
}