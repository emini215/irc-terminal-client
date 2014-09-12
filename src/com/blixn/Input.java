package com.blixn;

import java.util.Scanner;

/**
 * Created by emini on 12/09/14.
 * Copyright and stuff, you know!
 */
public class Input {

    private Scanner scanner;

    public Input() {
        scanner = new Scanner(System.in);
    }

    public String getTerminalInput() {
        return scanner.nextLine();
    }
}
