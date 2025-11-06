#include<stdio.h>

void main(){
    int n = 7;

    int arr[] = {1, 2, 3, 4, 5, 6, 7};

    int t = arr[0]; // Store the first element

    for (int i = 0; i < n - 1; i++)
    {
        arr[i] =  arr[i + 1]; // Shift elements to the left
    }

    arr[n - 1] = t; // Place the first element at the end

    for (int i = 0; i < n; i++)
    {
        printf("%d ", arr[i]);
    }
    
    
}