package Gemini;

public class RunLengthEncoding {

    public static String encode(String str){
        StringBuilder sb = new StringBuilder();

        int count = 1;
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                count++;
            }
            else{
                sb.append(str.charAt(i - 1)).append(count);
                count = 1;
            }
        }
        
        // Last group of characters
        sb.append(str.charAt(str.length() - 1)).append(count);

        return sb.length() > str.length() ? str : sb.toString();
    }

    public static void main(String[] args) {
        String str = "aabcccccaaa";

        System.out.println(encode(str));
    }
}
