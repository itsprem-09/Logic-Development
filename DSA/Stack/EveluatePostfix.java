package DSA.Stack;

import java.util.Stack;

public class EveluatePostfix {

    public static int evaluatePostfix(String str){
        Stack<Integer> st = new Stack<>();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            // if operand add into stack
            if (Character.isDigit(c)) {
                st.push(Integer.parseInt(c+""));
            }
            else{
                // operator take 2 operand perform operation and add into stack
                if (c == '+') {
                    int b = st.pop();
                    int a = st.pop();
                    st.push(a+b);
                }
                else if (c == '-') {
                    int b = st.pop();
                    int a = st.pop();
                    st.push(a - b);
                }
                else if (c == '*') {
                    int b = st.pop();
                    int a = st.pop();
                    st.push(a * b);
                }
                else if (c == '/') {
                    int b = st.pop();
                    int a = st.pop();
                    st.push(a / b);
                }
            }

        }
        return st.pop();
    }

    public static void main(String[] args) {
        String postfix = "23*54*+9-";
        int result = evaluatePostfix(postfix);
        System.out.println("Result: " + result);
    }
}
