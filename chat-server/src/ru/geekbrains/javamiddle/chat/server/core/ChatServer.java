package ru.geekbrains.javamiddle.chat.server.core;

import ru.geekbrains.javamiddle.network.ServerSocketThread;
import ru.geekbrains.javamiddle.network.ServerSocketThreadListener;
import ru.geekbrains.javamiddle.network.SocketThread;
import ru.geekbrains.javamiddle.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {

    ServerSocketThread server;

    public void start(int port) {
        if (server != null && server.isAlive())
            putLog("Сервер уже запущен.");
        else
            server = new ServerSocketThread(this, "Server", port, 2000);
    }

    public void stop() {
        if (server == null || !server.isAlive()) {
            putLog("Сервер уже остановлен.");
        } else {
            server.interrupt();
        }
    }

    private void putLog(String msg) {
        System.out.println(msg);
    }

    /**
     * Server Socket Thread Listener methods
     * */

    @Override
    public void onServerStarted(ServerSocketThread thread) {
        putLog("Поток сервера запущен.");
    }

    @Override
    public void onServerCreated(ServerSocketThread thread, ServerSocket server) {
        putLog("Сервер сокетов запущен.");
    }

    @Override
    public void onServerTimeout(ServerSocketThread thread, ServerSocket server) {
        //putLog("Server timeout");
    }

    @Override
    public void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Клиент подсоединился.");
        String name = "SocketThread " + socket.getInetAddress() + ":" + socket.getPort();
        new SocketThread(this, name, socket);
    }

    @Override
    public void onServerException(ServerSocketThread thread, Throwable throwable) {
        putLog("Ошибка сервера.");
        throwable.printStackTrace();
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Сервер сокетов остановлен.");
    }

    /**
     * Socket Thread Listener methods
     * */

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Сокет запущен.");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        putLog("Сокет остановлен.");
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Сокет готов.");
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        thread.sendMessage("echo: " + msg);
    }

    @Override
    public void onSocketException(SocketThread thread, Throwable throwable) {
        throwable.printStackTrace();
    }
}

