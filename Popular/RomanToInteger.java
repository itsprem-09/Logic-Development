package Popular;

public class RomanToInteger {

    public static int value(char r){
        if (r == 'I')
            return 1;
        if (r == 'V')
            return 5;
        if (r == 'X')
            return 10;
        if (r == 'L')
            return 50;
        if (r == 'C')
            return 100;
        if (r == 'D')
            return 500;
        if (r == 'M')
            return 1000;
        return -1;
    }

    public static int romanToInt(String str){
        int ans = 0;

        for (int i = 0; i < str.length(); i++) {
            int s1 = value(str.charAt(i));

            if (i + 1 < str.length()) {
                int s2 = value(str.charAt(i + 1));

                // If current value is greater or equal, 
                // add it to result
                if (s1 >= s2) {
                    ans += s1;
                }
                else {
                    // else, add the difference and skip 
                    // next symbol
                    ans += (s2 - s1);
                    i++;
                }
            }
            else{
                ans += s1;
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        String str = "IX";
        System.out.println(romanToInt(str));
    }
}
