import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
public class Main {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        Scanner scanner = new Scanner(System.in);
        String input;
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
        if (sumOperands < 1) {
            throw new Exception("Строка не является математической операцией");
        } else if (sumOperands > 1) {
            throw new Exception("Формат не удовлетворяет заданию");
        }
        return checkArabRom(input, operand);
    }
    private String checkArabRom(String input,String operand) throws Exception {
        String[] rom = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        String[] splitInput = input.split("\\" + operand);
        boolean numRom1 = false;
        boolean numRom2 = false;
        String num1 = splitInput[0].toUpperCase();
        String num2 = splitInput[1].toUpperCase();
        if (checkRom(num1, rom)) numRom1 = true;
        if (checkRom(num2, rom)) numRom2 = true;
        if (numRom1 && numRom2) return arithmeticRom(num1, num2, rom, operand);
        if ((numRom1 ^ numRom2) & (checkArab(num1) | checkArab(num2))) {
            throw new Exception("Использнуются разные системы счистления");
        }
        return String.valueOf(arithmeticArab(parseInt(num1), parseInt(num2), operand));
    }
    private int arithmeticArab(int num1, int num2, String operand) throws Exception {
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
        int numInt1 = 0;
        int numInt2 = 0;
        for (int i = 0; i < rom.length; i++) {
            if (rom[i].equals(num1)) numInt1 = i + 1;
            if (rom[i].equals(num2)) numInt2 = i + 1;
        }
        int result = arithmeticArab(numInt1, numInt2, operand);
        if (result < 0) throw new Exception("В римской системе нет отрицательных чисел");
        else if (result < 1) throw new Exception("В римской системе нет нуля");
        return arabToRom(result, rom);
    }
    private String arabToRom(int result, String[] rom) {
        int[] cxclxlxvi = {0,0,0,0,0,0};
        cxclxlxvi[0] = result / 100;
        cxclxlxvi[1] = (result %= 100) / 90;
        cxclxlxvi[2] = (result %= 90) / 50;
        cxclxlxvi[3] = (result %= 50) / 40;
        cxclxlxvi[4] = (result %= 40) / 10;
        cxclxlxvi[5] = result % 10;
        return buildRom(cxclxlxvi, rom);
    }
    private String buildRom(int[] cxclxlxvi,String[] rom) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("C".repeat(cxclxlxvi[0]))
                     .append("XC".repeat(cxclxlxvi[1]))
                     .append("L".repeat(cxclxlxvi[2]))
                     .append("XL".repeat(cxclxlxvi[3]))
                     .append("X".repeat(cxclxlxvi[4]));
        if (cxclxlxvi[5] > 0) stringBuilder.append(rom[cxclxlxvi[5] - 1]);
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
    private boolean checkRom(String num, String[] rom) throws Exception {
        if (checkArab(num)) return false;
        HashSet<String> romSet = new HashSet<>(List.of(rom));
        if (romSet.contains(num)) return true;
        else throw new Exception("Введена не корректная римская цифра");
    }
}