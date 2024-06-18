package com.socket.io.manager;

import java.util.List;
import java.util.Map;

public class SocketManagerConfig {
    public Boolean forceNew;
    public Boolean multiplex;
    public String[] transports;
    public Boolean upgrade;
    public Boolean rememberUpgrade;
    public String path;
    public String query;
    public Map<String, List<String>> extraHeaders;
    public Boolean reconnection;
    public Integer reconnectionAttempts;
    public Long reconnectionDelay;
    public Long reconnectionDelayMax;
    public Double randomizationFactor;
    public Long timeout;
    public Map<String, String> auth;
}
