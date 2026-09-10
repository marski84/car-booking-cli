package org.booking.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

abstract class InputHandler {

    protected final BufferedReader reader;

    protected InputHandler(Reader source) {
        this.reader = new BufferedReader(source);
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public int readInt(String prompt) throws IOException {
        System.out.print(prompt);
        return Integer.parseInt(readLine().trim());
    }

    public double readDouble(String prompt) throws IOException {
        System.out.print(prompt);
        return Double.parseDouble(readLine().trim());
    }
}