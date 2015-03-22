package com.blixn;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    private static Map<String, String> client_codes = new HashMap<String, String>();
    static {
        //Bold all \e[1mMESSAGE
        //Pink
        client_codes.put("Vermin", "\033[95m");
    }

    private static ArrayList<String> colour_codes = new ArrayList<String>();
    static {
        colour_codes.add("\033[91m");//Light red
        colour_codes.add("\033[35m");//Magenta
        colour_codes.add("\033[36m");//Cyan
        colour_codes.add("\033[93m");//Light yellow
        colour_codes.add("\033[94m");//Light blue
    }

    private static String COLOUR_END = "\033[0m";

    private String getRandomColour() {
        return colour_codes.get(new Random().nextInt(colour_codes.size()-1));
    }

    private String decode(String message) {

        String author = "";

        //TODO: ANSI-colour-codes

        //TODO: Format: :Blixn!~emini@cloaked-40880839.a163.priv.bahnhof.se PRIVMSG #zhg :Hm
        if (message.contains("PRIVMSG")) {
            author = message.substring(message.indexOf(":")+1, message.indexOf("!")) + ": ";
            message = message.substring(message.lastIndexOf(":")+1);
            author = getRandomColour() + author + COLOUR_END;
        }

        //TODO: Format: :emini!~emini@cloaked-40880839.a163.priv.bahnhof.se QUIT :Quit:  Leaving
        if (message.contains("QUIT")) {
            author = message.substring(message.indexOf(":")+1, message.indexOf("!"));
            message = " quit. Saying: " + message.substring(message.lastIndexOf(":") + 1);
        }

        //TODO: Format: :emini!~emini@cloaked-40880839.a163.priv.bahnhof.se JOIN :#zhg
        if (message.contains("JOIN")) {
            author = message.substring(message.indexOf(":")+1, message.indexOf("!"));
            message = " joined channel " + message.substring(message.lastIndexOf(":") + 1);
        }

        return author + message;
    }
}
