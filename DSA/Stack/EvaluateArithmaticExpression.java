package DSA.Stack;

import java.util.Stack;

public class EvaluateArithmaticExpression {
    public static int precedence(char op){
        if (op == '+' || op == '-') {
            return 1;
        }
        if (op == '*' || op == '/') {
            return 2;
        }
        return 0;
    }

    public static int applyOp(int a, int b, char op){
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
        }
        return 0;
    }

    public static int evalute(String str){
        Stack<Integer> nums = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            // skip spaces
            if (c == ' ') continue;

            else if (Character.isDigit(c)) {
                int n = 0;

                while (i < str.length() && Character.isDigit(str.charAt(i))) {
                    n = n * 10 + Integer.parseInt(str.charAt(i)+"");
                    i++;
                }

                nums.push(n);
                // index adjust
                i--;
            }

            else if (c == '(') {
                ops.push(c);
            }

            else if (c == ')') {
                while (ops.peek() != '(') {
                    int b = nums.pop();
                    int a = nums.pop();

                    nums.push(applyOp(a, b, ops.pop()));
                }
                ops.pop(); // remove ')'
            }

            // operator
            else{
                while (!ops.isEmpty() && precedence(c) <= precedence(ops.peek())) {
                    int b = nums.pop();
                    int a = nums.pop();

                    nums.push(applyOp(a, b, ops.pop()));
                }
                ops.push(c);
            }

        }

        // remaining operations
        while (!ops.isEmpty()) {
            int b = nums.pop();
            int a = nums.pop();
            nums.push(applyOp(a, b, ops.pop()));
        }

        return nums.pop();
    }
}
