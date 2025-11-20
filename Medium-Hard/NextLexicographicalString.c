#include <stdio.h>
#include <string.h>

// Swap two characters
void swap(char *a, char *b) {
    char t = *a;
    *a = *b;
    *b = t;
}

// Reverse substring from l to r
void reverse(char s[], int l, int r) {
    while (l < r) {
        swap(&s[l], &s[r]);
        l++;
        r--;
    }
}

int main() {
    char s[100];
    printf("Enter string: ");
    scanf("%s", s);

    int n = strlen(s);
    int i;

    // Step 1: Find pivot (first from right where s[i] < s[i+1])
    for (i = n - 2; i >= 0; i--) {
        if (s[i] < s[i + 1])
            break;
    }

    // No pivot → no next permutation (already highest order)
    if (i < 0) {
        printf("No higher string exists\n");
        return 0;
    }

    // Step 2: Find smallest character on the right > s[i]
    int j = n - 1;
    while (s[j] <= s[i])
        j--;

    // Step 3: Swap pivot and the found character
    swap(&s[i], &s[j]);

    // Step 4: Reverse the right part (makes it smallest possible)
    reverse(s, i + 1, n - 1);

    printf("Next string: %s\n", s);

    return 0;
}
