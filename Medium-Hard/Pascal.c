#include<stdio.h>

int fact(int n){
    int ans = 1;
    for (int i = 1; i <= n; i++)
    {
        ans *= i;
    }
    return ans;
}

int nCr(int n, int r){
    return fact(n) / (fact(r) * fact(n - r));
}

void main(){
    int n = 5;

    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n - i; j++)
        {
            printf(" ");
        }
        
        for (int j = 0; j <= i; j++)
        {
            printf("%d ", nCr(i, j));
        }
        
        printf("\n");
    }
    
}