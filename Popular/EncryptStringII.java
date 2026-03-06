package Popular;

public class EncryptStringII {

    public static String encrypt(String str){
        int count = 1;

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < str.length(); i++) {
            if (i < str.length() && str.charAt(i) == str.charAt(i - 1)) {
                count++;
            }
            else{
                char ch = str.charAt(i - 1);

                String hexCount = Integer.toHexString(count);

                sb.append(ch).append(hexCount);

                count = 1;
            }
        }

        return sb.reverse().toString();

    }

    public static void main(String[] args) {
        System.out.println(encrypt("abc"));           // 1c1b1a
        System.out.println(encrypt("aaaaaaaaaaa"));   // ba
    }
}
