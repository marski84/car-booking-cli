package org.booking;

import org.booking.menu.Menu;
import org.booking.menu.MenuService;
import org.booking.reader.ConsoleInputHandler;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();

        MenuService menuService = new MenuService();
        Menu menu = new Menu(menuService, consoleInputHandler);

        while (true) {
            menu.start();
            menu.promptUser("Select Option");
            String selectedOption = consoleInputHandler.readLine();
            menu.handleUserInput(selectedOption);
            menu.promptUser("Press enter to continue...");
            consoleInputHandler.readLine();
        }

    }
}