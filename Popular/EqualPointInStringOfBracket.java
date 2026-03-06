package Popular;

public class EqualPointInStringOfBracket {

    public static int equalPoint(String s){
        for (int i = 0; i < s.length(); i++) {
            int leftCnt = 0;
            int rightCnt = 0;

            // count '(' before i
            for (int j = 0; j < i; j++) {
                if (s.charAt(j) == '(') {
                    leftCnt++;
                }
            }

            // count ')' after i
            for (int j = i; j < s.length(); j++) {
                if (s.charAt(j) == ')') {
                    rightCnt++;
                }
            }
            if (leftCnt == rightCnt) {
                return i;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        String str = "(())))(";
        System.out.println(equalPoint(str));
    }
}
