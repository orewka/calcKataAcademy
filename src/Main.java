import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
public class Main {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        Scanner scanner = new Scanner(System.in);
        String input = null;
        while (true) {
            input = scanner.nextLine().replace(" ", "");
            if (input.equals("return")) break;
            System.out.println(main.calc(input));
        }
    }
    private String calc(String input) throws Exception {
        String[] operands = {"*","/","+","-"};
        HashSet<String> operandsSet = new HashSet<>(List.of(operands));
        return checkOperands(operandsSet, input);
    }
    private String checkOperands(HashSet<String> operandsSet, String input) throws Exception {
        String operand = null;
        int sumOperands = 0;
        String[] parts = input.split("");
        for (String op: parts) {
            if (operandsSet.contains(op)) {
                sumOperands++;
                operand = op;
            }
        }
        boolean minus = parts[0].equals("-");
        if (sumOperands < 1) {
            throw new Exception("Строка не является математической операцией");
        } else if (sumOperands > 1 & !minus) {
            throw new Exception("Формат не удовлетворяет заданию");
        } else if (sumOperands == 3) {
            return checkOperands(operandsSet, input.replace("-", ""));
        }
        return checkArabRom(input, operand, minus);
    }
    private String checkArabRom(String input,String operand,boolean minus) throws Exception {
        String[] rom = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        HashSet<String> romSet = new HashSet<>(List.of(rom));
        String[] splitInput = input.split("\\" + operand);
        String num1 = null;
        String num2 = null;
        if (minus && splitInput.length == 3) {
            num1 = "-" + splitInput[1].toUpperCase();
            num2 = splitInput[2].toUpperCase();
        } else {
            num1 = splitInput[0].toUpperCase();
            num2 = splitInput[1].toUpperCase();
        }
        String result;
        boolean numRom1 = false;
        boolean numRom2 = false;
        if (romSet.contains(num1)) numRom1 = true;
        if (romSet.contains(num2)) numRom2 = true;
        if (numRom1 && numRom2) {
            return arithmeticRom(num1, num2, rom, operand);
        }
        if ((numRom1 ^ numRom2) & (checkArab(num1) | checkArab(num2))) {
            throw new Exception("Использнуются разные системы счистления");
        }
        checkRom(num1,rom);
        checkRom(num2,rom);
        result = String.valueOf(arithmetic(parseInt(num1), parseInt(num2), operand));
        return result;
    }
    private int arithmetic(int num1, int num2, String operand) throws Exception {
        if (num1 > 10 || num2 > 10) throw new Exception("Введено число больше 10");
        return switch (operand) {
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            default -> 0;
        };
    }
    private String arithmeticRom(String num1, String num2, String[]rom, String operand) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        int numInt1 = 0;
        int numInt2 = 0;
        for (int i = 0; i < rom.length; i++) {
            if (rom[i].equals(num1)) numInt1 = i + 1;
            if (rom[i].equals(num2)) numInt2 = i + 1;
        }
        int result = arithmetic(numInt1,numInt2, operand);
        if (result < 1) throw new Exception("В римской системе нет отрицательных чисел");
        int first = result / 10;
        int last = result % 10;
        stringBuilder.append("X".repeat(first));
        if (last > 0) stringBuilder.append(rom[last-1]);
        return stringBuilder.toString();
    }
    private boolean checkArab(String num) {
        try {
            parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    private void checkRom(String num,String[] rom) throws Exception {
        if (checkArab(num)) return;
        String[] fail = {"IIIV","IIV","IIIX","IIX"};
        int x = 0;
        int y = 0;
        int i = 0;
        int v = 0;
        int p = 0;
        for (String ch: fail) {
            if (ch.equals(num)) throw new Exception("Введены не корректные римские цифры");
        }
        for (String ch: num.split("")) {
            switch (ch) {
                case "X" -> x += 10;
                case "I" -> i++;
                case "V" -> v += 5;
                default -> {
                    for (int j = 1; j < rom.length - 1; j++) {
                        if (ch.equals(rom[j])) {
                            y += j + 1;
                            break;
                        }
                    }
                    if (y == 0) p++;
                }
            }
        }
        if (i > 3 || v > 5 || p > 0) throw new Exception("Введены не корректные римские цифры");
        if (x + y + i + v > 10) throw new Exception("Введено число больше 10");
    }
}