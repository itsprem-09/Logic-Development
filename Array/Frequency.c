#include<stdio.h>

int getMax(int arr[], int n){
    int max = arr[0];
    for (int i = 1; i < n; i++)
    {
        if (arr[i] > max)
        {
            max = arr[i];
        }
    }
    return max;
}

void main(){
    int n = 10;
    int arr[] = {2, 23, 4, 2, 23, 4, 5, 2, 4, 5};

    int max = getMax(arr, n);
    int freq[max + 1];

    for (int i = 0; i < max + 1; i++)
    {
        freq[i] = 0;
    }

    for (int i = 0; i < n; i++)
    {
        freq[arr[i]]++;
    }

    for (int i = 0; i < max + 1; i++)
    {
        if (freq[i] > 0)
        {
            printf("%d is occured %d times\n", i, freq[i]);
        }  
    }
}