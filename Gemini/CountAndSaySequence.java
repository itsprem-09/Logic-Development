package Gemini;

public class CountAndSaySequence {

    public static String countAndSay(int n){
        String res = "1";

        for (int i = 2; i <= n; i++) {
            StringBuilder next = new StringBuilder();
            int cnt = 1;

            for (int j = 1; j < res.length(); j++) {
                if (res.charAt(j) == res.charAt(j - 1)) {
                    cnt++;
                }
                else{
                    next.append(cnt).append(res.charAt(j - 1));
                    cnt = 1;
                }
            }

            next.append(cnt).append(res.charAt(res.length() - 1));

            res = next.toString();
        }

        return res;
    }

    public static void main(String[] args) {
        
    }
}
