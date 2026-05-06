public class RepeatingString {

    public static boolean isRepeat(String s){
        String str = s+s;
        return str.substring(1, str.length()-1).contains(s);
    }

    public static void main(String[] args) {
        System.out.println(isRepeat("abcabc")); // true
        System.out.println(isRepeat("abcab"));  // false
    }
}
