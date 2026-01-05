package com.Info_intern.Hgs;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class EarlyHealthServer {

    public static void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(10000), 0);
            server.createContext("/health", exchange -> {
                String response = "OK";
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            });
            server.start();
        } catch (Exception ignored) {}
    }
}
