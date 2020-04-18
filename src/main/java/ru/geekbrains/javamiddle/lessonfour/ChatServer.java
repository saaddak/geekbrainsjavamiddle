package ru.geekbrains.javamiddle.lessonfour;

public class ChatServer {
    public void start(int port) {
        System.out.println("Сервер запущен на порту: " + port);
    }

    public void stop() {
        System.out.println("Сервер остановлен");
    }

}
