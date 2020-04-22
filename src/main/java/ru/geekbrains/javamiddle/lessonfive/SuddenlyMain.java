package ru.geekbrains.javamiddle.lessonfive;

import java.util.Arrays;

public class SuddenlyMain {
    static float[] arr1 = new float[10_000_000];
    static float[] arr2 = new float[10_000_000];

    public static void main(String[] args) {
        final int size = 10_000_000;
        final int halfSize = size / 2;
        oneFlow(size);
        twoFlows(size, halfSize);

        if (Arrays.equals(arr1, arr2)) {
            System.out.println("Совпадают");
        }
        else {
            System.out.println("Не совпадают");
        }
    }

    private static void oneFlow(int size) {
        //float[] arrgh = new float[size];
        Arrays.fill(arr1, 1.0f);
        long startPoint = System.currentTimeMillis();
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = (float)(arr1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));;
        }
        System.out.printf("Время выполнения однопоточного метода: %d мс.%n", (System.currentTimeMillis()) - startPoint);
    }

    private static void twoFlows(int size, int halfSize) {
        float[] firstPart = new float[halfSize];
        float[] secondPart = new float[halfSize];
        //float[] arrgh = new float[size];
        Arrays.fill(arr2, 1.0f);
        long startPoint = System.currentTimeMillis();
        System.arraycopy(arr2, 0, firstPart, 0, halfSize);
        System.arraycopy(arr2, halfSize, secondPart, 0, halfSize);
        FreeThreader frTr1 = new FreeThreader(firstPart, 0);
        FreeThreader frTr2 = new FreeThreader(secondPart, halfSize);
        try {
            frTr1.join();
            frTr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.arraycopy(firstPart, 0, arr2, 0, halfSize);
        System.arraycopy(secondPart, 0, arr2, halfSize, halfSize);
        System.out.printf("Время выполнения двухпоточного метода: %d мс.%n", (System.currentTimeMillis()) - startPoint);
    }
}
