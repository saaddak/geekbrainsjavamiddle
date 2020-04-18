package ru.geekbrains.javamiddle.lessonfive;

public class FreeThreader extends Thread {
    private float[] array;

    FreeThreader(float[] array) {
        this.array = array;
        start();
    }

    @Override
    public void run() {
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + i / 5.0f) * Math.cos(0.2f + i / 5.0f) * Math.cos(0.4f + i / 2.0f));
        }
    }
}
