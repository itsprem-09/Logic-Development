package Popular;

public class PangramString {

    public static boolean isPangram(String str){
        str = str.toLowerCase();
        boolean[] seen = new boolean[26];

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch >= 'a' && ch <= 'z') {
                seen[ch - 'a'] = true;
            }
        }

        for (int i = 0; i < seen.length; i++) {
            if (!seen[i]) {
                return false;
            }
        }

        return true;    
    }    

    public static void main(String[] args) {
        String str = "The quick brown fox jumps over the lazy dog";
        System.out.println(isPangram(str));
    }
}
