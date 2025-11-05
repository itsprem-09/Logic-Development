#include<stdio.h>

void main(){
    int n = 10;
    int arr[] = {1,2,3,4,2,4,4,5,5,6};
    
    int count = 0;
    for(int i = 0; i < n; i++) {
        for(int j = i + 1; j < n; j++) {
            if(arr[i] == arr[j]) {
                count++;
                break; // break to avoid counting same duplicate again
            }
        }
    }

    printf("Total duplicate count is: %d", count );
    
}