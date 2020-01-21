package com.tsystems.javaschool.tasks.calculator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Calculator {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        String input = "5+2.1";
        System.out.println(calculator.evaluate(input));
    }

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        // шаг 0 - проверить на символы и убрать пробелы
        // шаг 1 - найти по порядку знаки умножения и деления
        // шаг 2 - заменить эти операции в тексте
        List<Double> numbers = Arrays.stream(statement.split("[\\-+/*]")).map(Double::parseDouble).collect(Collectors.toList());
        List<String> signs = Arrays.asList(statement.split("[\\d.]"));
        signs = signs.stream().filter(s -> Pattern.matches("[+-/*]", s)).collect(Collectors.toList());
        double result = numbers.get(0);
        for (int i = 1; i < signs.size(); i++) {
            result += calculate(signs.get(i-1), result, numbers.get(i));
        }
        System.out.println(result);
        return "";
    }

    private double calculate(String sign, double firstNum, double secondNum) {
        if (sign.equals("+")) {
            return firstNum + secondNum;
        } else if (sign.equals("-")) {
            return firstNum - secondNum;
        } else if (sign.equals("*")) {
            return firstNum * secondNum;
        } else if (sign.equals("/")) {
            return firstNum / secondNum;
        }
        return 0;
    }
}
