// 1 2 3 4 5
// 10 9 8 7 6
// 11 12 13 14 15
// 20 19 18 17 16
// 21 22 23 24 25

#include <stdio.h>

void main()
{
    int n = 5;

    for (int i = 1; i <= n; i++)
    {
        int num;
        if (i % 2 == 0)
        {
            num = n * i;
            for (int j = 1; j <= n; j++)
            {
                printf("%d ", num);
                num--;
            }
        }
        else
        {
            num = n * i - 4;
            for (int j = 1; j <= n; j++)
            {
                printf("%d ", num);
                num++;
            }
        }
        printf("\n");
    }
}