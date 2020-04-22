package ru.geekbrains.javamiddle.lessonfive;

public class FreeThreader extends Thread {
    private float[] array;
    int delta;

    FreeThreader(float[] array, int delta) {
        this.array = array;
        this.delta = delta;
        start();
    }

    @Override
    public void run() {
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + (i + delta) / 5) * Math.cos(0.2f + (i + delta) / 5) * Math.cos(0.4f + (i + delta) / 2));
        }
    }
}
