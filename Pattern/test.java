package Pattern;
import java.util.Arrays;
import java.util.Scanner;

public class test {

    private static void leastSquare(){
        String str = "Ab,c,de!$";

        char[] strArray = str.toCharArray();

        int start = 0;
        int end = str.length()-1;
        while (start < end) {
            if (Character.isAlphabetic(strArray[start]) && Character.isAlphabetic(strArray[end])) {
                char c = strArray[start];
                strArray[start] = strArray[end];
                strArray[end] = c;
                start++;
                end--;
            }
            else if (!Character.isAlphabetic(strArray[start])) {
                start++;
            }
            else{
                end--;
            }
        }

        System.out.println(new String(strArray));
    }

    public static void printPattern(){
        int n = 5;

        for (int i = 1; i <= n; i++) {
            int val = i;
            for (int j = 1; j <= i; j++) {
                System.out.print((val)+" ");
                val += (n - j);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // int n = sc.nextInt();

        // leastSquare();
        printPattern();

    }
}
