package Popular;

public class StringAreRotationOfEachOther {

    public static boolean isRotation(String s1, String s2){
        if (s1.length() != s2.length()) {
            return false;
        }

        String str = s1 + s1;
        return str.contains(s2);
    }

    public static void main(String[] args) {
        System.out.println(isRotation("abcd","cdab")); // true
        System.out.println(isRotation("aab","aba"));   // true
        System.out.println(isRotation("abcd","acbd")); // false
    }
}
