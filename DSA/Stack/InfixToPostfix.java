package DSA.Stack;

import java.util.Stack;

public class InfixToPostfix {

    public static int precedence(char c){
        if (c == '^') {
            return 3;
        }
        else if (c == '*' || c == '/') {
            return 2;
        }
        else if (c == '+' || c == '-') {
            return 1;
        }
        return -1;
    }

    public static String infixToPostfix(String str){
        Stack<Character> st = new Stack<>();

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            // check if it is operand
            if (Character.isLetterOrDigit(c)) {
                res.append(c);
            }

            // check if it is opening bracket
            else if (c == '(') {
                st.push(c);
            }

            // remove everything until '('
            else if (c == ')') {
                while (!st.isEmpty() && st.peek() == '(') {
                    res.append(st.pop());
                }
            }

            else{
                // operator
                while (!st.isEmpty() && (precedence(c) <= precedence(st.peek()))) {
                    res.append(st.pop());
                }
                st.push(c);
            }
        }

        while (!st.isEmpty()) {
            res.append(st.pop());
        }

        return res.toString();
    }

    public static void main(String[] args) {
        String exp = "A+B*C";
        System.out.println(infixToPostfix(exp));
    }
}
