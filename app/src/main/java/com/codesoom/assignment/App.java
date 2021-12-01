package com.codesoom.assignment;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress(8001);
            HttpServer httpServer = HttpServer.create(address, 0);
            httpServer.createContext("/");
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(new App().getGreeting());
    }
}
