import java.util.Scanner;
import java.util.TreeMap;

public class Calculator {
    private final static TreeMap<Integer, String> map = new TreeMap<>();
    static {
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }
    private final String input;
    String operator;
    private int firstNumber;
    private int secondNumber;
    boolean isFirstExpressionRoman;
    boolean isSecondExpressionRoman;
    private final String[] romanDictionary = {"I","II", "III", "IV", "V", "VI","VII", "VIII", "IX", "X"};

    public static void main(String[] args) throws Exception {
        new Calculator(null);
    }
    public Calculator(String input) throws Exception {
        if (input == null) {
            input = readInput();
        }
        this.input = input;

        operator = operator();
        try {
            firstNumber = Integer.parseInt(input.split(operator)[0].replaceAll(" ", ""));
            isFirstExpressionRoman = false;
        } catch (NumberFormatException e) {
            convertRomanExpressionToInt(input.split(operator)[0],input.split(operator)[1]);
            isFirstExpressionRoman = true;
        }
        try {
            secondNumber = Integer.parseInt(input.split(operator)[1].replaceAll(" ", ""));
            isSecondExpressionRoman = false;
        } catch (NumberFormatException e) {
            convertRomanExpressionToInt(input.split(operator)[0],input.split(operator)[1]);
            isSecondExpressionRoman = true;
        }
        try {
            isNumbersOkay();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (isFirstExpressionRoman && isSecondExpressionRoman) {
            System.out.print(intToRoman(processOfCalculating(firstNumber,secondNumber)));
        } else if (!isFirstExpressionRoman && !isSecondExpressionRoman) {
            System.out.print(processOfCalculating(firstNumber,secondNumber));
        } else throw new Exception("Одновременно используются разные системы счисления");
    }
    private void isNumbersOkay() throws Exception {
        if ((firstNumber<1 || firstNumber>10) || (secondNumber<1 || secondNumber>10)) {
            throw new Exception("Одно из чисел не находится в диапазоне от 1 до 10 включительно");
        }
        String[] operators = new String[] {"-","+","/","*"};
        for (String op : operators) {
            if (count(input, op)>1) {
                throw new Exception("Формат математической операции не удовлетворяет заданию - " +
                        "два операнда и один оператор (+, -, /, *)");
            }
        }
    }
    private int count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }
    public String readInput() {
        //ввод выражения с клавиатуры
        System.out.println("Напишите математическое выражение: ");
        return new Scanner(System.in).nextLine().replace(" ", "");
    }
    private String operator() {
        String[] operators = new String[] {"-"," +","/"," *"};
        for (String op : operators) {
            if (input.replaceAll("\\+", " +").replaceAll("\\*", " *").contains(op)) {
                if (op.equals(" *") || op.equals(" +")) {
                    return "["+op.replaceAll(" ","")+"]";
                }
                return op;
            }
        }
        try {
            throw new Exception("Нет операторов");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private int romanToInt(String string) throws Exception {
        string = string.replaceAll(" ", "");
        for (int i = 0; i < romanDictionary.length; i++) {
            if(romanDictionary[i].equals(string)) {
                return i+1;
            }
        }
        throw new Exception("Римских чисел не найдено");
    }
    private static String intToRoman(int number) throws Exception {
        int l;
        try {
            l =  map.floorKey(number);
        } catch (NullPointerException e) {
            throw new Exception("В римской системе счисления нет отрицательных чисел и нуля");
        }
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + intToRoman(number-l);
    }
    private void convertRomanExpressionToInt(String first, String second) {
        try {
            firstNumber = romanToInt(first);
            secondNumber = romanToInt(second);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    int processOfCalculating(int a, int b) {
        int result = 0;
        String[] ops = new String[] {"-"," +","/"," *"};
        for (String op : ops) {
            if (input.replaceAll("\\+", " +").replaceAll("\\*", " *").contains(op)) {
                switch (op) {
                    case " +" -> result = a + b;
                    case "-" -> result = a - b;
                    case " *" -> result = a * b;
                    case "/" -> result = a / b;
                }
            }
        }
        return result;
    }
}
