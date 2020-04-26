package ru.geekbrains.javamiddle.chat.client;

import ru.geekbrains.javamiddle.chat.common.Library;
import ru.geekbrains.javamiddle.network.SocketThread;
import ru.geekbrains.javamiddle.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top", true);
    private final JTextField tfLogin = new JTextField("ivan");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final JList<String> userList = new JList<>();
    private boolean shownIoErrors = false;
    private SocketThread socketThread;

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
        userList.setListData(new String[]{"user1", "user2", "user3", "user4",
                "user5", "user6", "user7", "user8", "user9",
                "user-with-exceptionally-long-name-in-this-chat"});
        JScrollPane scrUser = new JScrollPane(userList);
        JScrollPane scrLog = new JScrollPane(log);
        scrUser.setPreferredSize(new Dimension(100, 0));
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setEditable(false);
        cbAlwaysOnTop.addActionListener(this);
        tfMessage.addActionListener(this);
        btnSend.addActionListener(this);
        btnLogin.addActionListener(this);
        btnDisconnect.addActionListener(this);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        panelBottom.setVisible(false);

        add(scrUser, BorderLayout.EAST);
        add(scrLog, BorderLayout.CENTER);
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if (src == btnSend || src == tfMessage) {
            sendMessage();
        } else if (src == btnLogin) {
            connect();
        } else if (src == btnDisconnect) {
            socketThread.close();
        } else {
            throw new RuntimeException("Unknown source:" + src);
        }
    }

    private void connect() {
        try {
            Socket socket = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
            socketThread = new SocketThread(this, "Client", socket);
        } catch (IOException e) {
            showException(Thread.currentThread(), e);
        }
    }

    private void sendMessage() {
        String msg = tfMessage.getText();
        String username = tfLogin.getText();
        if ("".equals(msg)) return;
        tfMessage.setText(null);
        tfMessage.requestFocusInWindow();
        socketThread.sendMessage(msg);
    }

    private void wrtMsgToLogFile(String msg, String username) {
        try (FileWriter out = new FileWriter("log.txt", true)) {
            out.write(username + ": " + msg + System.lineSeparator());
            out.flush();
        } catch (IOException e) {
            if (!shownIoErrors) {
                shownIoErrors = true;
                showException(Thread.currentThread(), e);
            }
        }
    }

    private void putLog(String msg) {
        if ("".equals(msg)) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + System.lineSeparator());
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                    t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
            JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        showException(t, e);
        System.exit(1);
    }

    /**
     * Socket Thread Listener methods
     * */

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Start");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        putLog("Stop");
        panelBottom.setVisible(false);
        panelTop.setVisible(true);
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Ready");
        panelBottom.setVisible(true);
        panelTop.setVisible(false);
        String login = tfLogin.getText();
        String password = new String(tfPassword.getPassword());
        thread.sendMessage(Library.getAuthRequest(login, password));

        //thread.sendMessage(parseRequest(Library.getAuthRequest(login, password)));
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        //putLog(msg);
        putLog(parseRequest(msg));
    }

    @Override
    public void onSocketException(SocketThread thread, Throwable throwable) {
        showException(thread, throwable);
    }

    private String parseRequest(String sourceRequest) {
        Pattern requestRegex = Pattern.compile("±");
        String[] parsedArray = requestRegex.split(sourceRequest);
        try {
            switch (parsedArray[0]) {
                case "/auth_request":
                    return "!> запрос идентификации от пользователя " + parsedArray[1] + " с паролем " + parsedArray[2];
                case "/auth_accept":
                    return "!> идентификация пройдена для пользователя " + parsedArray[1];
                case "/auth_denied":
                    return "!> в доступе отказано";
                case "/msg_format_error":
                    return "!> возникла ошибка: " + parsedArray[1];
                case "/broadcast":
                    Date currentDate = new Date(Long.parseLong(parsedArray[1]));
                    return "> подключение состоялось в " + currentDate + " к серверу " + parsedArray[2] + " с сообщением " + parsedArray[3];
                default:
                    return sourceRequest;
            }
        } catch (IndexOutOfBoundsException e) {
            onSocketException(socketThread, e);
        }
        return "!> неизвестное действие пользователя";
    }
}
