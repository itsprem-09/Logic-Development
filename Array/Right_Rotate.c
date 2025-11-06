#include<stdio.h>

void main(){
    int n = 7;

    int arr[] = {1, 2, 3, 4, 5, 6, 7};

    int t = arr[n - 1]; // Store the last element

    for (int i = n - 1; i >= 1; i--)
    {
        arr[i] =  arr[i - 1]; // Shift elements to the right
    }

    arr[0] = t; // Place the last element at the beginning

    for (int i = 0; i < n; i++)
    {
        printf("%d ", arr[i]);
    }
    
}