package ru.geekbrains.javamiddle.lessonone.common;

import java.awt.*;

public interface GameCanvasListener {
    void onCanvasRepainted(GameCanvas canvas, Graphics g, float deltaTime);
}
