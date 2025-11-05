#include <stdio.h>

int main() {
    int n1 = 8, n2 = 6, k = 0;

    int a[] = {1, 2, 3, 4, 5, 6, 7, 8};
    int b[] = {5, 6, 7, 8, 9, 10};
    int unionArr[n1 + n2]; // maximum possible size

    for(int i = 0; i < n1; i++) {
        unionArr[k++] = a[i]; // add to union array
    }

    for(int i = 0; i < n2; i++) {
        // check if this element already exists in union array
        int exists = 0;
        for(int j = 0; j < k; j++) {
            if(unionArr[j] == b[i]) {
                exists = 1;
                break;
            }
        }

        // if not exists, add it
        if(!exists) {
            unionArr[k++] = b[i];
        }
    }

    printf("\nUnion of arrays:\n");
    for(int i = 0; i < k; i++) {
        printf("%d ", unionArr[i]);
    }

    return 0;
}
