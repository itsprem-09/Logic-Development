package Gemini;

public class MinWindowSubstring {

    public static String minWindow(String s, String t){
        int minLen = Integer.MAX_VALUE;
        String res = "";

        for (int i = 0; i < s.length(); i++) {
            int[] freq = new int[256];

            for (char c : t.toCharArray()) {
                freq[c]++;
            }

            for (int j = i; j < s.length(); j++) {
                freq[s.charAt(j)]--;

                if (isValid(freq)) {
                    if (j - i + 1 < minLen) {
                        minLen = j - i + 1;
                        res = s.substring(i, j + 1);
                    }
                    break;
                }
            }
        }

        return res;
    }

    private static boolean isValid(int[] freq) {
        for (int count : freq) {
            if (count > 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        
    }
}
