package com.blixn;

/**
 * Created by emini on 12/09/14.
 * Copyright and stuff, you know!
 */
public class Main {

    public static final int MAXIMUM_ALIAS_LENGTH = 9;
    private Input input;
    private User user;
    private Client client;

    public static void main(String args[]) {

        Main main = new Main();
        main.listenForUserInput();
    }

    public Main() {
        System.out.println("Welcome to emini's amazing irc-client!");
        input = new Input();
        createUser();
    }

    private void listenForUserInput() {
        System.out.println("Start spamming!");
        while(true) {
            String message = input.getTerminalInput();

            if (message.startsWith("/"))
                checkCommands(message.substring(1));
            else if (client != null)
                client.sendPrivateMessage(message);
        }
    }

    private void checkCommands(String command) {
        if (command.equals("quit")) {
            System.out.println("Thank you for using superduperchat!");
            client.closeConnections();
            System.exit(0);
        } else if (command.equals("connect")) {
            connect();
        } else if (command.startsWith("connect") && command.length() > "connect".length()) {
            String[] params = command.substring("connect".length()+1).split(":");
            if (params.length != 2 || !isInteger(params[1])) {
                System.out.println("Invalid parameters for \"/connect\".");
            } else {
                connect(params[0], Integer.parseInt(params[1]));
            }
        } else if (command.equals("help")) {
            System.out.println("Available commands:\n  /quit\n  /connect [<host:port>]\n  /join <channel>\n  /leave [<channel>]\n  /help");
        } else if (command.startsWith("join") && command.length() > "join".length()) {
            String channel = command.substring("join".length()+1);
            if (channel.length() > 1)
                client.joinChannel(channel);
            else
                System.out.println("Invalid parameters for join. Channels must be prefixed with \"#\".");
        } else if (command.startsWith("leave")) {
            if (command.equals("leave"))
                client.leaveChannel();
            String channel = command.substring("leave".length()+1);
            if (channel.length() > 1)
                client.leaveChannel(channel);
            else
                System.out.println("Invalid parameters for leave. Channels must be prefixed with \"#\".");
        } else if (command.startsWith("names")) {
            if (command.length() == "names".length())
                client.sendNamesMessage();
            else
                client.sendNamesMessage(command.substring("names".length()+1));
        } else if (command.startsWith("nick")) {
		client.sendNickMessage(command.substring("nick".length()+1));
	} else {
            System.out.println("Invalid command. Try \"/help\" for instructions.");
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void createUser() {
        String alias = "";
        String realname = "";

        //Get valid alias
        while (alias.equals("") || alias.length() > MAXIMUM_ALIAS_LENGTH){
            System.out.print("Choose an alias: ");
            alias = input.getTerminalInput();

            if (alias.equals(""))
                System.out.println("You must have an alias.");

            if (alias.length() > MAXIMUM_ALIAS_LENGTH)
                System.out.println("Alias cannot be longer than 9 characters.");
        }

        while (realname.equals("")) {
            System.out.print("Enter your name: ");
            realname = input.getTerminalInput();

            if (realname.equals(""))
                System.out.println("You need to enter your name!");
        }
        user = new User(alias, realname);
    }

    private void connect() {
        String host = "";
        int port = 0;

        while (host.equals("")) {
            System.out.print("Enter host-address: ");
            host = input.getTerminalInput();

            if (host.equals(""))
                System.out.println("You must enter an address to connect.");
        }

        while (port == 0) {
            System.out.print("Enter host-port: ");
            port = Integer.parseInt(input.getTerminalInput());

            if (port == 0)
                System.out.println("You must enter a port number.");
        }

        connect(host, port);
    }

    private void connect(String host, int port) {
        client = new Client(host, port, user);
        System.out.println("You are now connected to " + host + ":" + port + ".");
    }
}
