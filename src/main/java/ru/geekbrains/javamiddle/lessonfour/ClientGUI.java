package ru.geekbrains.javamiddle.lessonfour;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ClientGUI extends JFrame implements ActionListener, KeyListener,Thread.UncaughtExceptionHandler {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;
    private static String userName = "vasisualiy";
    private static String userPassword = "qwerty_123";
    private static String userIP = "127.0.0.1";
    private static String userPort = "8189";

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField(userIP);
    private final JTextField tfPort = new JTextField(userPort);
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Поверх других окон", true);
    private final JTextField tfLogin = new JTextField(userName);
    private final JPasswordField tfPassword = new JPasswordField(userPassword);
    private final JButton btnLogin = new JButton("Войти");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Отключиться</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Отправить");

    ClientIO phoronida = new ClientIO(userName, userName + "Output.txt");

    private final JList<String> userList = new JList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setAlwaysOnTop(true);
        userList.setListData(new String[]{"first-good-user", "second-bad-user", "third-ugly-user", "fourth-strange-user",
                "fifth-lazy-user", "sixth-self-reproach-user", "seventh-happy-user", "eighth-rude-user", "vasya-pupkin",
                "pablo-diego-jose-francisco-de-paula-juan-nepomuceno-maria-de-los-remedios-cipriano-de-la-santisima-trinidad-ruiz-y-picasso"});
        JScrollPane scrUser = new JScrollPane(userList);
        JScrollPane scrLog = new JScrollPane(log);
        scrUser.setPreferredSize(new Dimension(150, 0));
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setEditable(false);
        log.append(phoronida.readFile());
        cbAlwaysOnTop.addActionListener(this);
        btnSend.addActionListener(this);
        tfMessage.addKeyListener(this);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);

        add(scrUser, BorderLayout.EAST);
        add(scrLog, BorderLayout.CENTER);
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);
        setVisible(true);
        tfMessage.grabFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if (src == btnSend) {
            messageSender();
        } else {
            throw new RuntimeException("Неизвестный источник: " + src);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // приходится реализовывать все методы ради одного keyPressed...
    };

    @Override
    public void keyPressed(KeyEvent e) {
        Object src = e.getSource();
        if(src == tfMessage) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                messageSender();
            }
        } else {
            throw new RuntimeException("Неизвестный источник: " + src);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // жизнь - боль...
    };


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

    void messageSender() {
        log.append(userName + ": " + tfMessage.getText() + "\n"); // сперва видим у себя
        phoronida.writeFile(log.getText()); // затем отсылаем в журнал
        tfMessage.setText("");
        tfMessage.grabFocus();
    }
}
