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
        if (!checkStatements(statement)) {
            return null;
        }
        try {
            List<Double> parenthesesResults = new ArrayList<>();
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
                        i,
                        statement.substring(closeBracketIndex + 1));
                i++;
            }
            parenthesesResults.forEach(System.out::println);
            System.out.println(statement);
            return formatOutput(calculateStatement(statement, parenthesesResults));
        } catch (DivisionByZeroException | NumberFormatException e) {
            return null;
        }
    }

    private boolean checkStatements(String statement) {
        if (statement == null || statement.isEmpty() || statement.contains(",")) {
            return false;
        }
        int openParenthesesCount = 0;
        int closeParenthesesCount = 0;
        for (char c : statement.toCharArray()) {
            if (c == '(') {
                openParenthesesCount++;
            }
            if (c == ')') {
                closeParenthesesCount++;
            }
        }
        if (openParenthesesCount != closeParenthesesCount) {
            return false;
        }
        return true;
    }

    private double calculateStatement(String statement, List<Double> parenthesesResults) {
        List<Double> numbers = Arrays.stream(statement.split("[\\-+/*]")).map(s -> {
                if (s.contains("{")) {
                    int index = Integer.parseInt(s.substring(1, s.length()-1));
                    return parenthesesResults.get(index);
                }
                return Double.parseDouble(s);
        }).collect(Collectors.toList());
        List<String> signs = Arrays.stream(statement.replace("{", "").replace("}","")
                .split("[\\d.]"))
                .filter(s -> Pattern.matches("[\\-+/*]", s)).collect(Collectors.toList());
        calculateOperations(OperatorType.MUL_DIV, numbers, signs);
        calculateOperations(OperatorType.ADD_SUB, numbers, signs);
        return numbers.get(0);
    }

    private String formatOutput(double result) {
        return result % 1 == 0
                ? String.valueOf((int) result)
                : String.valueOf(result);
    }

    private void calculateOperations(OperatorType operatorType, List<Double> numbers, List<String> signs) {
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
        return null;
    }

    enum OperatorType {
        MUL_DIV,
        ADD_SUB
    }
}
