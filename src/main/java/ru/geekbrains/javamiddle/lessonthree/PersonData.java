package ru.geekbrains.javamiddle.lessonthree;

import java.util.ArrayList;
import java.util.HashMap;

class PersonData {
    private String phone;
    private String email;
    PersonData(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    String getPhone() { return phone; }
    void setPhone(String region) { this.phone = phone; }

    String getEmail() { return email; }
    void setEmail(String email) { this.email = email; }
}
