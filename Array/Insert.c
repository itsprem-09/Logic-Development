// Write a program to insert an element at a specific position in an array.
#include<stdio.h>
void main(){
    int n = 6;
    int arr[7] = {4,2,5,1};
    int pos = 5;
    int element = 10;

    for (int i = n; i > pos; i--)
    {
        arr[i] = arr[i-1];
    }
    
    arr[pos] = element;
    
    
    for (int i = 0; i < 7; i++)
    {
        printf("%d ", arr[i]);
    }
    

}