#include<stdio.h>
void main(){
    int n = 5;
    int arr[] = {1, 2, 3, 4, 5};

    int largest = arr[0];
    int second_largest = -123123123;

    for (int i = 0; i < n; i++)
    {
        if (arr[i] > largest)
        {
            second_largest = largest;
            largest = arr[i];
        }
        else if (arr[i] > second_largest && arr[i] != largest)
        {
            second_largest = arr[i];
        }
    }

    printf("Second largest element is: %d", second_largest);
    
}