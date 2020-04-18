package ru.geekbrains.javamiddle.lessonfour;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClientIO {
    private String userName;
    private String fileName;
    ClientIO(String userName, String fileName) {
        this.userName = userName;
        this.fileName = fileName;
    }

    String getFileName() { return fileName; }
    void setFileName(String fileName) { this.fileName = fileName; }

    void writeFile(String userLog) {
        byte[] byteText = userLog.getBytes();
        try {
            PrintStream epson = new PrintStream(new FileOutputStream(fileName));
            epson.write(byteText);
            epson.flush();
            epson.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String readFile() {
        try {
            BufferedReader mustek = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            int currentChar;
            StringBuilder sb = new StringBuilder();
            while ((currentChar = mustek.read()) != -1) {
                sb.append((char) currentChar);
            }
            mustek.close();
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
