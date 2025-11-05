#include<stdio.h>
void main(){
    int n = 6;
    int arr[6] = {4,2,5,1,8,7};
    int pos = 2; // Position to delete (0-based index)

    for (int i = pos; i < n-1; i++)
    {
        arr[i] = arr[i+1];
    }
    arr[n-1] = 0; // Optional: Clear the last element
   
    // Print the modified array
    for (int i = 0; i < n - 1; i++)
    {
        printf("%d ", arr[i]);
    }
}