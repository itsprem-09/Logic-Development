public class SimqualStrings {

    public static boolean isSimqual(String s1, String s2){
        if (s1.length() != s2.length()) {
            return false;
        }

        int[] freq = new int[26];

        for (int i = 0; i < s1.length(); i++) {
            freq[s1.charAt(i) - 'a']++;
        }

        for (int i = 0; i < s2.length(); i++) {
            freq[s2.charAt(i) - 'a']--;
        }

        for (int val : freq) {
            if (val != 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String s1 = "listen";
        String s2 = "silent";

        System.out.println(isSimqual(s1, s2));
    }
}
