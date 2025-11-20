// WAP for given N digits, arrange them to form the largest number divisible by 3.
// For example, let us consider 555, it is divisible by 3 because sum of digits is 5 + 5 + 5 = 15, which is divisible by 3. If a sum of digits is not divisible by 3 then the remainder should be either 1 or 2. 
// If we get remainder either '1' or '2', we have to remove maximum two digits to make a number that is divisible by 3: 
// If remainder is '1' : We have to remove single digit that have remainder '1' or we have to remove two digit that have remainder '2' ( 2 + 2 => 4 % 3 => '1')
// If remainder is '2' : .We have to remove single digit that have remainder '2' or we have to remove two digit that have remainder '1' ( 1 + 1 => 2 % 3 => 2 ).

#include <stdio.h>

int main() {
    int n = 5;
    int digit[] = { 8, 1, 7, 6, 0 };
    int freq[10] = {0};
    int sum = 0;

    for (int i = 0; i < n; i++) {
        freq[digit[i]]++;
        sum += digit[i];
    }

    int mod = sum % 3;

    // Remove smallest digit with (digit % 3 == mod)
    if (mod != 0) {
        int removed = 0;

        // First try to remove ONE smallest digit
        for (int d = mod; d <= 9; d += 3) {
            if (freq[d] > 0) {
                freq[d]--;
                removed = 1;
                break;
            }
        }

        // If not removed, remove TWO from the other group
        if (!removed) {
            int need = 2;
            int other = (mod == 1 ? 2 : 1); // opposite group

            for (int d = other; d <= 9 && need > 0; d += 3) {
                while (freq[d] > 0 && need > 0) {
                    freq[d]--;
                    need--;
                }
            }

            if (need > 0) { // not possible
                printf("Not possible\n");
                return 0;
            }
        }
    }

    // Edge case: all digits removed
    int allzero = 1;
    for (int d = 1; d <= 9; d++)
        if (freq[d] > 0) allzero = 0;

    if (allzero) {  // Only zeros left
        printf("0\n");
        return 0;
    }

    // Print in descending order to form largest number
    printf("Largest number divisible by 3: ");
    for (int d = 9; d >= 0; d--) {
        while (freq[d] > 0) {
            printf("%d", d);
            freq[d]--;
        }
    }

    printf("\n");
    return 0;
}
