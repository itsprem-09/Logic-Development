public class EarliestRepeat {

    private static char earliestRepeatBetter(String str) {
        boolean[] visited = new boolean[256];

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (visited[ch])
                return ch;

            visited[ch] = true;
        }
        return '\0'; // no repetition
    }

    private static char earliestRepeat(String str) {

        int minIndex = Integer.MAX_VALUE;
        char ch = '\0';

        for (int i = 0; i < str.length() - 1; i++) {
            for (int j = i + 1; j < str.length(); j++) {
                if (str.charAt(i) == str.charAt(j)) {
                    if (j < minIndex) {
                        minIndex = j;
                        ch = str.charAt(i);
                    }
                    break;
                }
            }
        }
        return ch;
    }

    public static void main(String[] args) {
        earliestRepeat("sfsd");
        earliestRepeatBetter("daasd");
    }

}