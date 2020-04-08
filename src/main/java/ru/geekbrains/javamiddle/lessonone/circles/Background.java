package ru.geekbrains.javamiddle.lessonone.circles;

import ru.geekbrains.javamiddle.lessonone.common.GameCanvas;
import ru.geekbrains.javamiddle.lessonone.common.GameObject;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;

public class Background extends JPanel implements GameObject {
    private float time;
    private static final float AMPLITUDE = 255f / 2f;
    private Color color;

    @Override
    public void update(GameCanvas gameCanvas, float deltaTime) {
        time += deltaTime;
        int red = Math.round(AMPLITUDE + AMPLITUDE * (float) Math.sin(time * 2f));
        int green = Math.round(AMPLITUDE + AMPLITUDE * (float) Math.sin(time * 3f));
        int blue = Math.round(AMPLITUDE + AMPLITUDE * (float) Math.sin(time));
        color = new Color(red, green, blue);
    }

    @Override
    public void render(GameCanvas gameCanvas, Graphics g) {
        gameCanvas.setBackground(color);
        try {
            ImageIcon backgroundImage = new ImageIcon("caramella.gif");
            Image normalImage = backgroundImage.getImage();
            if(backgroundImage.getImageLoadStatus() == MediaTracker.ERRORED) {
                throw new IOException();
            }
            g.drawImage(normalImage, 0, 0, 800, 600, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
