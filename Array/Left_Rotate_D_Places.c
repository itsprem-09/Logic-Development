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

    // // Rotate left by d places
    // for (int i = 0; i < d; i++)
    // {
    //     int t = arr[0]; // Store the first element

    //     for (int j = 0; j < n - 1; j++)
    //     {
    //         arr[j] =  arr[j + 1]; // Shift elements to the left
    //     }

    //     arr[n - 1] = t; // Place the first element at the end
    // }


    // Approach - 2 :

    // int temp[d];

    // // Store first d elements in temp array
    // for (int i = 0; i < d; i++)
    // {
    //     temp[i] = arr[i];
    // }

    // // Shift the remaining elements to the left
    // for (int i = d; i < n; i++)
    // {
    //     arr[i - d] = arr[i];
    // }

    // // Copy the temp elements to the end of the original array
    // int j = 0;
    // for (int i = n - d; i < n; i++)
    // {
    //     arr[i] = temp[j++];
    // }

    // Apporach - 3 : Reverse Method

    reverse(arr, 0, d - 1);         // Reverse first d elements
    reverse(arr, d, n - 1);         // Reverse remaining n-d elements
    reverse(arr, 0, n - 1);         // Reverse the whole array


    for (int i = 0; i < n; i++)
    {
        printf("%d ", arr[i]);
    }
    
}