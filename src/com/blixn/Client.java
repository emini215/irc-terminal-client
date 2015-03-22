package com.blixn;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;

/**
 * Created by emini on 12/09/14.
 * Copyright and stuff, you know!
 */
public class Client {

    private Socket connection;
    private PrintWriter output;
    private BufferedReader input;

    private String current_channel;

    public Client(String host, int port, User user) {

        try {
            setupConnection(host, port);
            setupStreams();
            registerConnection(user);
            ServerListener listener = new ServerListener(this, input);
            Thread thread = new Thread(listener);
            thread.start();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void setupConnection(String host, int port) throws IOException {
        connection = new Socket(host, port);
    }

    private void setupStreams() throws IOException {
        output = new PrintWriter(connection.getOutputStream());
        output.flush();
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private void registerConnection(User user) {
        System.out.println("Connecting to " + connection.getInetAddress().getHostName() + ".");
        sendPassMessage("somesecretpassword");
        sendNickMessage(user.getAlias());
        sendUserMessage(user.getAlias(), 0, user.getRealName());
        //joinChannel("#zhg");
    }

    private void sendPassMessage(String password) {
        sendMessage("PASS " + password);
    }

    public void sendNickMessage(String nick) {
        sendMessage("NICK " + nick);
    }

    private void sendUserMessage(String user, int mode, String realname) {
        sendMessage("USER " + user + " " + mode + " * :" + realname);
    }

    public void sendQuitMessage(String reason) {
        sendMessage("QUIT :" + reason);
    }

    public void sendPrivateMessage(String target, String message) {
        sendMessage("PRIVMSG " + target + " :" + message);
    }

    public void sendPrivateMessage(String message) {
        if (current_channel != null)
            sendPrivateMessage(current_channel, message);
        else
            System.out.println("You need a message-destination!");
    }

    public void sendNamesMessage() {
        sendMessage("NAMES");
    }

    public void sendNamesMessage(String channel) {
        sendMessage("NAMES " + channel);
    }

    public void sendMessage(String message) {
        output.write(message + "\r\n");
        output.flush();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void pong(String destination) {
        sendMessage("PONG :" + destination);
    }

    public void joinChannel(String channel) {
        sendMessage("JOIN " + channel);
        current_channel = channel;
    }

    public void leaveChannel() {
        if (current_channel != null)
            leaveChannel(current_channel);
        else
            System.out.println("You got no channel to leave!");
    }

    public void leaveChannel(String channel) {
        System.out.println("Leaving " + channel);
        sendMessage("PART " + channel);
    }

    public void closeConnections() {
        try {
            sendQuitMessage("Oops, I did it again!");
            output.flush();
            output.close();
            input.close();
            connection.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
