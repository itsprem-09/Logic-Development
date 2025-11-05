#include<stdio.h>

void main(){
    int n = 10;
    int arr[] = {1,2,2,3,4,4,4,5,5,6};
   
    for (int i = 0; i < n ; i++)
    {
        int count = 0;
        for (int j = 0; j < n; j++)
        {
            if (arr[i] == arr[j])
            {
                count++;
            }
        }
        if (count== 1)
        {
            printf("%d ", arr[i]);
        }
        
    }
    
    
}