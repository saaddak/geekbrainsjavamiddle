package ru.geekbrains.javamiddle.lessonfour;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {
    private static final int POS_X = 1000;
    private static final int POS_Y = 550;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;

    private final ChatServer server;
    private final JButton btnStart = new JButton("Запустить");
    private final JButton btnStop = new JButton("Остановить");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
//        throw new RuntimeException("Main died!");
        System.out.println("Main завершился");
    }

    ServerGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Чат-сервер");
        setAlwaysOnTop(true);
        setLayout(new GridLayout(1, 2));
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);

        add(btnStart);
        add(btnStop);
        server = new ChatServer();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnStart) {
            server.start(8189);
        } else if (src == btnStop) {
//            server.stop();
            throw new RuntimeException("Hello from EDT!");
        } else {
            throw new RuntimeException("Неизвестный источник:" + src);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        msg = String.format("Исключение в \"%s\" %s: %s\n\tв %s",
                t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
        JOptionPane.showMessageDialog(this, msg, "Исключение", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
