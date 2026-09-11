package org.booking;

import org.booking.menu.Menu;
import org.booking.menu.MenuService;
import org.booking.reader.ConsoleInputHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {

        final Path path = Path.of("src/main/resources/cars_inline_db.txt");
        URL usersDbUrl = Main.class.getResource("/users_inline_db.txt");
        final Path userDb = Path.of(usersDbUrl.toURI());

        ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();

        MenuService menuService = new MenuService();
        Menu menu = new Menu(menuService);

        while (true) {
            menu.start();
            menu.promptUser("Select Option");

            String selectedOption = consoleInputHandler.readLine();
            menu.handleUserInput(selectedOption);
        }

    }
}