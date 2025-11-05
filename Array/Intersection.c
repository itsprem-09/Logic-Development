#include <stdio.h>

int main() {
    int a[] = {1, 3, 4, 5, 6, 7, 5, 8, 9, 10};
    int b[] = {5, 6, 7, 11, 12, 13, 14};
    int n1 = 10, n2 = 7;
    
    printf("Intersection of arrays:\n");
    
    for(int i = 0; i < n1; i++) {
        for(int j = 0; j < n2; j++) {
            if(a[i] == b[j]) {
                
                // Check if already printed
                int alreadyPrinted = 0;
                for(int k = 0; k < i; k++) {
                    if(a[k] == a[i]) {
                        alreadyPrinted = 1;
                        break;
                    }
                }
                
                if(!alreadyPrinted) {
                    printf("%d ", a[i]);
                }
                break;
            }
        }
    }
    return 0;
}
