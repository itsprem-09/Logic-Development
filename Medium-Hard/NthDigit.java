// Given an integer n, return the nth digit of the infinite integer sequence [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, …].
// Input: n = 11 => Output: 0
// Explanation: The 11th digit of the sequence 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... is a 0, which is part of the
// number 10.

public class NthDigit {

    public static int findNthDigit(int n){
        StringBuilder sb = new StringBuilder();
        int i = 1;
        while (sb.length() <= n) {
            sb.append(i);
            i++;
        }
        return sb.charAt(n - 1)-'0';
    }

    public static int findNthDigitOptimized(int n){
        if (n <= 9) {
            return n;
        }
        int digitLength = 1;
        int count = 9;
        int start = 1;

        while (n > digitLength * count) {
            n -= digitLength * count;
            digitLength++;
            count *= 10;
            start *= 10;
        }

        int num = start + (n - 1) / digitLength;
        String numStr = Integer.toString(num);

        int digitIndex = (n - 1) % digitLength;
        return numStr.charAt(digitIndex) - '0';
    }

    public static void main(String[] args) {
        int n = 11;
        System.out.println(findNthDigit(n));
        System.out.println(findNthDigitOptimized(n));
    }
    
}