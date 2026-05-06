package DSA.Stack;

import java.util.Stack;

// traverse from right to left and push operand in stack and pop 2 operand when operator comes and perform operation and push result back in stack

public class EvalutePrefix {
    public static int evaluatePrefix(String str){
        Stack<Integer> st = new Stack<>();

        for (int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);

            // if operand add into stack
            if (Character.isDigit(c)) {
                st.push(Integer.parseInt(c+""));
            }
            else{
                // operator take 2 operand perform operation and add into stack
                if (c == '+') {
                    int a = st.pop();
                    int b = st.pop();
                    st.push(a+b);
                }
                else if (c == '-') {
                    int a = st.pop();
                    int b = st.pop();
                    st.push(a - b);
                }
                else if (c == '*') {
                    int a = st.pop();
                    int b = st.pop();
                    st.push(a * b);
                }
                else if (c == '/') {
                    int a = st.pop();
                    int b = st.pop();
                    st.push(a / b);
                }
            }

        }
        return st.pop();
    }
}
