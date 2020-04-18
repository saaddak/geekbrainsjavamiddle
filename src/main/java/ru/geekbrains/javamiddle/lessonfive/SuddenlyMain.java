package ru.geekbrains.javamiddle.lessonfive;

import java.util.Arrays;

public class SuddenlyMain {
    public static void main(String[] args) {
        final int size = 10_000_000;
        final int halfSize = size / 2;
        oneFlow(size);
        twoFlows(size, halfSize);
    }

    private static void oneFlow(int size) {
        float[] arrgh = new float[size];
        Arrays.fill(arrgh, 1.0f);

        long startPoint = System.currentTimeMillis();
        for (int i = 0; i < arrgh.length; i++) {
            arrgh[i] = (float)(arrgh[i] * Math.sin(0.2f + i / 5.0f) * Math.cos(0.2f + i / 5.0f) * Math.cos(0.4f + i / 2.0f));;
        }

        System.out.printf("Время выполнения однопоточного метода: %d мс.%n", (System.currentTimeMillis()) - startPoint);
    }

    private static void twoFlows(int size, int halfSize) {
        float[] firstPart = new float[halfSize];
        float[] secondPart = new float[halfSize];
        float[] arrgh = new float[size];
        Arrays.fill(arrgh, 1.0f);

        long startPoint = System.currentTimeMillis();
        System.arraycopy(arrgh, 0, firstPart, 0, halfSize);
        System.arraycopy(arrgh, halfSize, secondPart, 0, halfSize);

        FreeThreader frTr1 = new FreeThreader(firstPart);
        FreeThreader frTr2 = new FreeThreader(secondPart);
        try {
            frTr1.join();
            frTr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.arraycopy(firstPart, 0, arrgh, 0, halfSize);
        System.arraycopy(secondPart, 0, arrgh, halfSize, halfSize);

        System.out.printf("Время выполнения двухпоточного метода: %d мс.%n", (System.currentTimeMillis()) - startPoint);
    }
}
