package com.tsystems.javaschool.tasks.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("IfCanBeSwitch")
public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        if (!checkStatement(statement)) {
            return null;
        }
        try {
            List<Double> parenthesesResults = new ArrayList<>();
            statement = processParentheses(statement, parenthesesResults);
            return statement == null
                    ? null
                    : formatOutput(calculateStatement(statement, parenthesesResults));
        } catch (DivisionByZeroException | NumberFormatException e) {
            return null;
        }
    }

    private boolean checkStatement(String statement) {
        if (statement == null || statement.isEmpty()) {
            return false;
        }
        int openParenthesesCount = (int) statement.chars().filter(c -> c == '(').count();
        int closeParenthesesCount = (int) statement.chars().filter(c -> c == ')').count();
        return openParenthesesCount == closeParenthesesCount;
    }

    private String processParentheses(String statement, List<Double> parenthesesResults) {
        int i = 0;
        while (statement.contains(")")) {
            int closeBracketIndex = statement.indexOf(")");
            if (!statement.substring(0, closeBracketIndex).contains("(")) {
                return null;
            }
            int openBracketIndex = statement.substring(0, closeBracketIndex).lastIndexOf("(");
            String subStatement = statement.substring(openBracketIndex + 1, closeBracketIndex);
            parenthesesResults.add(calculateStatement(subStatement, parenthesesResults));
            statement = String.format("%s{%d}%s",
                    statement.substring(0, openBracketIndex),
                    i++,
                    statement.substring(closeBracketIndex + 1));
        }
        return statement;
    }

    private double calculateStatement(String statement, List<Double> parenthesesResults) {
        List<Double> numbers = Arrays.stream(statement.split("[\\-+/*]")).map(s -> {
                if (s.contains("{")) {
                    int index = Integer.parseInt(s.replaceAll("[{}]", ""));
                    return parenthesesResults.get(index);
                }
                return Double.parseDouble(s);
        }).collect(Collectors.toList());
        List<String> signs = Arrays.stream(statement.replaceAll("[{}]", "").split("[\\d]"))
                .filter(s -> Pattern.matches("[\\-+/*]", s))
                .collect(Collectors.toList());
        processOperations(OperatorType.MUL_DIV, numbers, signs);
        processOperations(OperatorType.ADD_SUB, numbers, signs);
        return numbers.get(0);
    }

    private void processOperations(OperatorType operatorType, List<Double> numbers, List<String> signs) {
        int i = 0;
        Iterator<String> iterator = signs.iterator();
        while (iterator.hasNext()) {
            String sign = iterator.next();
            if ((operatorType == OperatorType.MUL_DIV && sign.equals("*") || sign.equals("/"))
                || (operatorType == OperatorType.ADD_SUB && sign.equals("+") || sign.equals("-"))) {
                numbers.set(i, singleOperation(sign, numbers.get(i), numbers.get(i + 1)));
                numbers.remove(i + 1);
                iterator.remove();
                i--;
            }
            i++;
        }
    }

    private Double singleOperation(String sign, double firstNum, double secondNum) {
        if (sign.equals("+")) {
            return firstNum + secondNum;
        } else if (sign.equals("-")) {
            return firstNum - secondNum;
        } else if (sign.equals("*")) {
            return firstNum * secondNum;
        } else if (sign.equals("/")) {
            if (secondNum == 0) {
                throw new DivisionByZeroException();
            }
            return firstNum / secondNum;
        }
        return 0d;
    }

    private String formatOutput(double result) {
        return result % 1 == 0
                ? String.valueOf((int) result)
                : String.valueOf(result);
    }

    enum OperatorType {
        MUL_DIV,
        ADD_SUB
    }
}
