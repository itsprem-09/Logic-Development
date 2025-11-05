#include<stdio.h>
void main(){
    int n1 = 4;
    int arr1[] = {1, 3, 5, 7};
    int n2 = 5;
    int arr2[] = {2, 4, 6, 8, 10};

    int mergedSize = n1 + n2;
    int mergedArr[mergedSize];

    for (int i = 0; i < n1; i++)
    {
        mergedArr[i] = arr1[i];
    }

    for (int i = 0; i < n2; i++)
    {
        mergedArr[n1 + i] = arr2[i];
    }

    for (int i = 0; i < mergedSize; i++)
    {
        printf("%d ", mergedArr[i]);
    }
    
}