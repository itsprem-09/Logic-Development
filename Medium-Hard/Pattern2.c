// 1
// 1 * 1
// 1 * 3 * 1
// 1 * 3 * 3 * 1

#include<stdio.h>

void main(){
    int n = 4;

    for (int i = 1; i <= n; i++)
    {
        int num = 1;

        for (int j = 1; j <= 2 * i - 1; j++)
        {
            if (j % 2 != 0)
            {
                printf("%d ", num);
            }
            else{
                printf("* ");
            }
            if (j < i)
            {
                num++;
            }
            else{
                num--;
            }
            
            
        }
        printf("\n");
    }
    

}