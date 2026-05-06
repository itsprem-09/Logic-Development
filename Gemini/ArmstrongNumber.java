package Gemini;

public class ArmstrongNumber {

    public static int countDigits(int n){
        int cnt = 0;
        while (n > 0) {
            cnt++;
            n /= 10;
        }
        return cnt;
    }

    public static boolean isArmstrong(int n){
        int digits = countDigits(n);

        int sum = 0;
        int temp = n;

        while (n > 0) {
            int ld = n % 10;
            sum += Math.pow(ld, digits);
            n /= 10;
        }

        return sum == temp;
    }

    public static void main(String[] args) {
        System.out.println(isArmstrong(153));
    }
}
