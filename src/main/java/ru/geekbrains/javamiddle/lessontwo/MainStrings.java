package ru.geekbrains.javamiddle.lessontwo;

import java.util.regex.Pattern;

public class MainStrings {
    public static void main(String[] args) {
        final String STRING_ONE = "10 3 1 2\n2 3 2 2\n5 6 7 1\n300 3 1 0";
        final String STRING_TWO = "10 3 1 2\n2 хей-лалей! 2 2\n5 6 7 1\n300 3 1 0";
        final String STRING_THREE = "10 3 1 2\n2 3 2 2\n5 6 7 1\n300 3 1 0\n300 3 1 0";
        final String STRING_FOUR = "10 3 1 2\n2 3 2 2 666\n5 6 7 1\n300 3 1 0";
        final int BOUNDS = 4;
        final int INCISION = 2;
        try {
            dummyConverter(STRING_ONE, "'First Source String' (STRING_ONE)", BOUNDS, INCISION);
            //dummyConverter(STRING_ONE, "'First Source String' (STRING_ONE)", BOUNDS, 0); // деление суммы элементов массива не на 2, а на 0
            //dummyConverter(STRING_TWO, "'Second Source String' (STRING_TWO)", BOUNDS, INCISION); // нечисловое значение в массиве
            //dummyConverter(STRING_THREE, "'First Source String' (STRING_ONE)", BOUNDS, INCISION); // неверное количество строк
            //dummyConverter(STRING_FOUR, "'First Source String' (STRING_ONE)", BOUNDS, INCISION);  // неверное количество столбцов
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private static void dummyConverter(String sourceString, String arrayName, int bounds, int incision) throws CustomException {
        if(incision == 0) {
            throw new CustomException("\n\nКараул, шеф! Заставляют делить на нуль!\n");
        }

        Pattern regexOne = Pattern.compile("\\n");
        Pattern regexTwo = Pattern.compile("\\s");
        String[] interimStrArray;
        int summary = 0;

        interimStrArray = regexOne.split(sourceString);
        if(interimStrArray.length != bounds) {
            throw new CustomException("\n\nШеф, всё пропало! Количество строк в " + arrayName + " не равно " + bounds + "!\n");
        }

        String[][] finalStrArray = new String[interimStrArray.length][];

        for (int i = 0; i < interimStrArray.length; i++) {
            finalStrArray[i] = regexTwo.split(interimStrArray[i]);
            if(finalStrArray[i].length != bounds) {
                throw new CustomException("\n\nШеф, всё пропало! Количество столбцов в строке " + (i + 1) + " из " + arrayName + " не равно " + bounds + "!\n");
            }
        }

        int[][] finalIntArray = new int[finalStrArray.length][finalStrArray[0].length];
        for(int i = 0; i < finalStrArray.length; i++) {
            for(int j = 0; j < finalStrArray[i].length; j++) {
                if(tryParseInt(finalStrArray[i][j])) {
                    finalIntArray[i][j] = Integer.parseInt(finalStrArray[i][j]);
                    summary += finalIntArray[i][j];
                } else {
                    throw new CustomException("\n\nВсё пропало, шеф! В массиве чисел есть нечисловое значение!\n");
                }
            }
        }
        summary /= incision;
        System.out.println("Результат суммирования всех элементов " + arrayName +" и деления их на " + incision + ": " + summary + ".\n");
    }

    private static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
