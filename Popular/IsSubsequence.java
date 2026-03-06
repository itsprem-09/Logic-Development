package Popular;

public class IsSubsequence {

    public static boolean isSubsequence(String s, String t){
        int cnt = 0;
        int i = 0;
        int j = 0;

        while (i < s.length() && j < t.length()) {
            if (s.charAt(i) == t.charAt(j)) {
                cnt++;
                i++;
                j++;
            }
            else{
                j++;
            }
        }

        return cnt == s.length();
    }

    public static void main(String[] args) {
        String s = "abc", t = "ahbgdc";

        System.out.println(isSubsequence(s, t));
    }
}
