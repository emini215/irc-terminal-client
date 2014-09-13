package com.blixn;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by emini on 12/09/14.
 * Copyright and stuff, you know!
 */
public class ServerListener implements Runnable{

    private BufferedReader input;
    private Client client;

    public ServerListener(Client client, BufferedReader input) {
        this.client = client;
        this.input = input;
    }

    public void run() {
        try {
            while (true) {
                String message = input.readLine();

                if (message.startsWith("PING"))
                    client.pong(message.substring(6) + "\r\n");
                else
                    client.showMessage(decode(message));

            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private String decode(String message) {

        String author = "";

        //TODO: Format: :Blixn!~emini@cloaked-40880839.a163.priv.bahnhof.se PRIVMSG #zhg :Hm
        if (message.contains("PRIVMSG")) {
            author = message.substring(message.indexOf(":")+1, message.indexOf("!")) + ": ";
            message = message.substring(message.lastIndexOf(":")+1);
        }

        return author + message;
    }
}
