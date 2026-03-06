import java.util.Stack;

public class BalancedBrackets {

    public static boolean isBalanced(String s){
        Stack<Character> st = new Stack<>();

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (ch == '(' || ch == '{' || ch == '[') {
                st.push(ch);
            }
            else{
                if (st.isEmpty()) {
                    return false;  // No opening bracket for the current closing bracket
                }

                char top = st.pop();
                if ((ch == ')' && top != '(') ||
                    (ch == '}' && top != '{') ||
                    (ch == ']' && top != '[')) {
                    return false;  // Mismatched brackets
                }
            }
        }
        return st.isEmpty();  // If stack is empty, all brackets were matched
    }

    public static void main(String[] args) {
        String s = "{[(])}";
        System.out.println(isBalanced(s));
    }
}
