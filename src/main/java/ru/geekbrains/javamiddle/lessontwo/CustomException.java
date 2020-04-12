package ru.geekbrains.javamiddle.lessontwo;

public class CustomException extends Exception {
    private String message;

    CustomException() {
        super();
    }
    CustomException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
