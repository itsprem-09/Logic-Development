#include<stdio.h>

void reverse(int arr[], int start, int end)
{
    while (start < end)
    {
        int temp = arr[start];
        arr[start] = arr[end];
        arr[end] = temp;
        start++;
        end--;
    }
}

void main(){

    int n = 7;
    int arr[] = {1, 2, 3, 4, 5, 6, 7};
    int d = 3; // number of positions to rotate

    if (d % n == 0)
    {
        // No rotation needed
        for (int i = 0; i < n; i++)
        {
            printf("%d ", arr[i]);
        }
        return;
    }

    // Normalize d if greater than n
    d = d % n;

    // Approach - 1 :

    // Rotate right by d places
    // for (int i = 0; i < d; i++)
    // {
    //     int t = arr[n - 1]; // Store the last element

    //     for (int j = n - 1; j >= 1; j--)
    //     {
    //         arr[j] =  arr[j - 1]; // Shift elements to the right
    //     }

    //     arr[0] = t; // Place the last element at the beginning
    // }

    // Approach - 2 :

    // int temp[d];

    // // Store last d elements in temp array
    // for (int i = 0; i < d; i++)
    // {
    //     temp[i] = arr[n - d + i];
    // }

    // // Shift the remaining elements to the right
    // for (int i = n - 1; i >= d; i--)
    // {
    //     arr[i] = arr[i - d];
    // }

    // // Copy the d elements from temp to the beginning
    // int j = 0;
    // for (int i = 0; i < d; i++)
    // {
    //     arr[i] = temp[j++];
    // }

    // Apporach - 3 : Reverse Method

    reverse(arr, 0, n - 1);         // Reverse the whole array
    reverse(arr, 0, d - 1);         // Reverse first d elements
    reverse(arr, d, n - 1);         // Reverse last d elements

    for (int i = 0; i < n; i++)
    {
        printf("%d ", arr[i]);
    }
}