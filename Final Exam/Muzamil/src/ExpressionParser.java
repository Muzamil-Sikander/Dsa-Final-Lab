public class ExpressionParser {

    private static class CustomStack<T> {
        private Object[] array;
        private int size;
        private static final int DEFAULT_CAPACITY = 10;

        public CustomStack() {
            this.array = new Object[DEFAULT_CAPACITY];
            this.size = 0;
        }

        public void push(T element) {
            ensureCapacity();
            array[size++] = element;
        }

        public T pop() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            T element = (T) array[--size];
            array[size] = null; // Avoid memory leak
            return element;
        }

        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return (T) array[size - 1];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        private void ensureCapacity() {
            if (size == array.length) {
                int newCapacity = array.length * 2;
                Object[] newArray = new Object[newCapacity];
                System.arraycopy(array, 0, newArray, 0, size);
                array = newArray;
            }
        }
    }

    public static int evaluateExpression(String expression) {
        CustomStack<Integer> values = new CustomStack<>();
        CustomStack<Character> operators = new CustomStack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (ch == ' ') {
                continue; // Skip spaces
            }

            if (Character.isDigit(ch)) {
                int num = 0;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num = num * 10 + (expression.charAt(i) - '0');
                    i++;
                }
                values.push(num);
                i--; // Move back one position to correctly handle the next character in the loop
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop(); // Pop '('
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-');
    }

    private static int applyOperator(char operator, int b, int a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    public static void main(String[] args) {
        String expression = "((2 + 6) * 5) - 5";
        int result = evaluateExpression(expression);
        System.out.println("Result: " + result);
    }
}
