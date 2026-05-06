package Gemini;

public class FirstUniqueCharacter {

    public static int firstUnique(String str){
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            String right = str.substring(i + 1, str.length());
            if (!right.contains(ch+"")) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String str = "leetcode";

        System.out.println(firstUnique(str));
    }
}
