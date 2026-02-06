public class DecodeString {
    public static void main(String[] args) {
        String str = "2a3bc4dE5F2G7H";

        String ans = "";
        
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                String n = "";
                
                while (Character.isDigit(str.charAt(i))) {
                    n += str.charAt(i);
                    i++;
                }

                char ch = str.charAt(i);
                for (int j = 0; j < Integer.parseInt(n); j++) {
                    ans += ch;
                }
            }
            else{
                ans += str.charAt(i);
            }
        }

        System.out.println(ans);
    }
}
