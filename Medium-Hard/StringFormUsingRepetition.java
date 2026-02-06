public class StringFormUsingRepetition {

    public static String[] splitStringByLength(String str, int length) {
        int strLength = str.length();
        // Calculate the number of substrings needed
        int numSubstrings = (int) Math.ceil((double) strLength / length);
        String[] result = new String[numSubstrings];

        for (int i = 0; i < numSubstrings; i++) {
            int startIndex = i * length;
            int endIndex = Math.min(startIndex + length, strLength); // Use Math.min to handle the last part if it's
                                                                     // shorter
            result[i] = str.substring(startIndex, endIndex);
        }

        return result;
    }

    private static boolean isAllPartsSame(String[] strings) {
        for (int i = 1; i < strings.length; i++) {
            if (!strings[i].equals(strings[i - 1])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isRepeatating(String str) {
        for (int i = str.length() / 2; i >= 1; i--) {
            String[] parts = splitStringByLength(str, i);

            if (isAllPartsSame(parts)) {
                return true;
            }
        }
        return false;
    }

    // Easiest Way
    private static boolean repeatedSubstringPattern(String s) {
        int n = s.length();

        // n/2 because we need the string to repeat atleast 2 times so it can be said
        // repeated
        for (int len = 1; len <= n / 2; len++) {
            // length must divide total length
            if (n % len != 0)
                continue;

            String substr = s.substring(0, len);
            StringBuilder sb = new StringBuilder();

            int times = n / len;
            for (int i = 0; i < times; i++) {
                sb.append(substr);
            }

            // check if it matches with orignal
            if (sb.toString().equals(s)) {
                return true;
            }
        }
        return false;
    }

    // Optimal Way
    // If a string is made by repeating a substring, then:
    // s exists inside (s + s).substring(1, 2n - 1)

    // s = "abcabc"
    // s+s = "abcabcabcabc"
    // Trim = "bcabcabcab"
    // Contains original? ✔

    public static boolean repeatedSubstringPatternOptimal(String s) {
        String doubled = s + s;
        return doubled.substring(1, doubled.length() - 1).contains(s);
    }

    public static void main(String[] args) {
        System.out.println(isRepeatating("abcdabc"));
        System.out.println(repeatedSubstringPattern("aabaabaabaab"));
        System.out.println(repeatedSubstringPatternOptimal("aabaabaabaab"));
    }
}
