package DSA.Stack;

// Reverse the entire string and replace '(' with ')' and vice versa and then apply infix to postfix and reverse the result to get prefix

public class InfixToPrefix {
    public static String reverse(String str){
        StringBuilder res = new StringBuilder();

        for (int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);

            if (c == '(') {
                res.append(')');
            }
            else if (c == ')') {
                res.append('(');
            }
            else{
                res.append(c);
            }
        }

        return res.toString();
    }

    public static String infixToPrefix(String str){
        String reversed = reverse(str);
        String postfix = InfixToPostfix.infixToPostfix(reversed);
        return new StringBuilder(postfix).reverse().toString();
    }

    public static void main(String[] args) {
        String infix = "A+B*C";
        String prefix = infixToPrefix(infix);
        System.out.println("Prefix: " + prefix);
    }
}
