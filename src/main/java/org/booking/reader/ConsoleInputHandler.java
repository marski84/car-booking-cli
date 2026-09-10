package org.booking.reader;

import java.io.InputStreamReader;

public class ConsoleInputHandler extends InputHandler {

    public ConsoleInputHandler() {
        super(new InputStreamReader(System.in));
    }
}