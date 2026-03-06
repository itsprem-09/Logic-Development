public class HappyNumber {

    public static int sumOfDigitSquare(int n){
        int sum = 0;

        while (n > 0) {
            int digit = n % 10;
            sum += digit * digit;
            n /= 10;
        }

        return sum;
    }

    public static boolean isHappy(int n){
        int slow = n;
        int fast = n;

        do{
            slow = sumOfDigitSquare(slow);
            fast = sumOfDigitSquare(sumOfDigitSquare(fast));
        }while(slow != fast);

        return slow == 1; 
    }

    public static void main(String[] args) {
        System.out.println(isHappy(19));
        System.out.println(isHappy(4));
    }
}
