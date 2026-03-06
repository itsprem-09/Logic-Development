public class Largest5DigitPrimeWithDigitTwice {
    
    public static boolean isPrime(int n){
        if (n < 2) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasTwoDigits(int n, int digit){
        int cnt = 0;

        while (n > 0) {
            int ld = n % 10;
            if (ld == digit) {
                cnt++;
            }
            n /= 10;
        }

        return cnt == 2;
    }

    public static int findNumber(int n){
        for (int i = 99999; i >= 10000; i--) {
            if (isPrime(i) && hasTwoDigits(i, n)) {
                return i;
            }
        }
        return -1;
    }
    
    public static void main(String[] args) {
        System.out.println(findNumber(7));
    }
    
}