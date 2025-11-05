#include<stdio.h>

void main(){
    int n = 8;
    int arr[] = {9, 2, 3, 2, 6, 3, 1, 9};

    // Remove duplicates
    for(int i = 0; i < n - 1; i++) {
        for(int j = i + 1; j < n; j++) {
            if(arr[i] == arr[j]) {
                // Shift elements left
                for(int k = j; k < n - 1; k++) {
                    arr[k] = arr[k + 1];
                }
                n--;   // Reduce size after deletion
                j--;   // Check same index again
            }
        }
    }

    // Print array after removing duplicates
    for(int i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }

}