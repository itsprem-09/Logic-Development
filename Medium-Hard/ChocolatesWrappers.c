// WAP for following Scenario.
// Given n rupees and a chocolate price of m for each chocolate, with a wrapper exchange offer of 1
// chocolate per k wrappers, calculate the total number of chocolates you can eat with n rupees. 

#include <stdio.h>

int main() {
    int n = 15, m = 2, k = 3;

    int chocolates = n / m;      // initial chocolates
    int wrappers = chocolates;   // wrappers from them

    while (wrappers >= k) {
        int newChoc = wrappers / k;   // chocolates from wrappers
        chocolates = chocolates + newChoc;
        wrappers = wrappers % k + newChoc;
    }

    printf("Total chocolates = %d", chocolates);

    return 0;
}
