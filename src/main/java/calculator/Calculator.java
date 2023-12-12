package calculator;

public class Calculator {
    public static int calculate(int leftOperand, int rightOperand, String operator) {

        switch (operator) {
            case "PLUS" -> {
                return leftOperand + rightOperand;
            }
            case "MINUS" -> {
                return leftOperand - rightOperand;
            }
            case "MULTIPLY" -> {
                return leftOperand * rightOperand;
            }
            case "DIVIDE" -> {
                if (rightOperand != 0) {
                    return leftOperand / rightOperand;
                } else {
                    System.err.println("Error: Division by zero");
                    return Integer.MIN_VALUE;
                }
            }
            default -> {
                System.err.println("Error: Unsupported operator");
                return Integer.MIN_VALUE;
            }
        }
    }
}
