package com.socket.io.manager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;


public class SocketManager {
    public final Socket socket;
    private ConnectionSuccessListener connectionSuccessListener;
    private ConnectionErrorListener connectionErrorListener;
    private DisconnectionListener disconnectionListener;

    public SocketManager(String serverUrl, SocketManagerConfig config) throws URISyntaxException {
        IO.Options options = IO.Options.builder()
                .setForceNew(getOrDefault(config.forceNew, false))
                .setMultiplex(getOrDefault(config.multiplex, true))
                .setTransports(getOrDefault(config.transports, new String[]{Polling.NAME, WebSocket.NAME}))
                .setUpgrade(getOrDefault(config.upgrade, true))
                .setRememberUpgrade(getOrDefault(config.rememberUpgrade, false))
                .setPath(getOrDefault(config.path, "/socket.io/"))
                .setQuery(config.query) // Query 可以直接为 null
                .setExtraHeaders(config.extraHeaders) // ExtraHeaders 也可以直接为 null
                .setReconnection(getOrDefault(config.reconnection, true))
                .setReconnectionAttempts(getOrDefault(config.reconnectionAttempts, Integer.MAX_VALUE))
                .setReconnectionDelay(getOrDefault(config.reconnectionDelay, 1_000L))
                .setReconnectionDelayMax(getOrDefault(config.reconnectionDelayMax, 5_000L))
                .setRandomizationFactor(getOrDefault(config.randomizationFactor, 0.5))
                .setTimeout(getOrDefault(config.timeout, 20_000L))
                .setAuth(config.auth) // Auth 也可以直接为 null
                .build();

        socket = IO.socket(URI.create(serverUrl), options);
        // 使用 socket 实例进行进一步的操作
        // 监听连接成功事件
        socket.on(Socket.EVENT_CONNECT, args -> {
            if (connectionSuccessListener != null) {
                connectionSuccessListener.onConnectionSuccess();
            }
        });

        // 监听连接错误事件
        socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            String errorMessage = "连接错误";
            if (args.length > 0 && args[0] instanceof Exception) {
                Exception e = (Exception) args[0];
                errorMessage = e.getMessage();
            }
            if (connectionErrorListener != null) {
                connectionErrorListener.onConnectionError(errorMessage);
            }
        });

        // 监听断开连接事件
        socket.on(Socket.EVENT_DISCONNECT, args -> {
            if (disconnectionListener != null) {
                disconnectionListener.onDisconnected();
            }
        });
    }

    // 一个简单的泛型方法，用于返回非 null 的配置值或默认值
    private <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }


    /**
     * 连接到服务器
     */
    public void connect() {
        if (socket != null) {
            socket.connect();
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (socket != null) {
            socket.disconnect();
        }
    }

    /**
     * 发送消息
     * @param event   事件名
     * @param message 消息内容
     */
    public void sendMessage(String event, String message) {
        if (socket != null) {
            socket.emit(event, message);
        }
    }

    /**
     * 监听消息
     * @param event    事件名
     * @param listener 消息监听器
     */
    public void onMessage(String event, Emitter.Listener listener) {
        if (socket != null) {
            socket.on(event, listener);
        }
    }

    /**
     * 设置连接成功监听器
     * @param listener
     */
    public void setConnectionSuccessListener(ConnectionSuccessListener listener) {
        this.connectionSuccessListener = listener;
    }

    /**
     * 设置连接错误监听器
     * @param listener
     */
    public void setConnectionErrorListener(ConnectionErrorListener listener) {
        this.connectionErrorListener = listener;
    }

    /**
     * 设置断开连接监听器
     * @param listener
     */
    public void setDisconnectionListener(DisconnectionListener listener) {
        this.disconnectionListener = listener;
    }

    /**
     *  获取socket实例
     *  @return socket
     */
    public Socket getSocket() {
        return socket;
    }

}
