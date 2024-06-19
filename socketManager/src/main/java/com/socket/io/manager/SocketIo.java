package com.socket.io.manager;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketIo {
    // 私有静态实例，类加载时不初始化
    private static SocketIo instance;
    private static Socket socket;
    // 私有构造方法，防止外部通过new创建实例
    private SocketIo() {
    }
    // 公共的静态方法，返回唯一实例，使用双重检查锁定确保线程安全
    public static SocketIo getInstance() {
        if (instance == null) {
            synchronized (SocketIo.class) {
                if (instance == null) {
                    instance = new SocketIo();
                }
            }
        }
        return instance;
    }
    // 初始化方法，接受配置参数
    public static void initialize(String serverUrl, IO.Options options) throws URISyntaxException {
        if (socket == null) { // 确保socket只被初始化一次
            socket = IO.socket(serverUrl, options);
        }
    }

    // 提供一个方法来获取socket实例，确保外部可以使用socket进行操作
    public Socket getSocket() {
        return socket;
    }
}
