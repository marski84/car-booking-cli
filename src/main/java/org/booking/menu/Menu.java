package org.booking.menu;

import lombok.AllArgsConstructor;
import org.booking.reader.ConsoleInputHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@AllArgsConstructor
public class Menu {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final MenuService menuService;
    private final ConsoleInputHandler consoleInputHandler;

    public void start() {
        String menu = """
                1 - Book Car
                2 - Delete Booking
                3 - View All User Booked Cars
                4 - View All Bookings
                5 - View Available Cars
                6 - View Available Electric Cars
                7 - View All Users
                8 - Exit
                """;
        System.out.println(menu);
    }

    public void promptUser(String msg) {
        System.out.println(msg);
    }

    public void handleUserInput(String input) throws IOException {
        int option;
        try {
            option = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid option: \"" + input + "\". Please enter a number between 1 and 8.");
            return;
        }

        switch (option) {
            case 1 -> this.handleBookCar();
            case 2 -> this.handleDeleteBooking();
            case 3 -> this.menuService.viewAllBookedCars();
            case 4 -> this.menuService.viewAllBookings();
            case 5 -> this.menuService.viewAvailableCars();
            case 6 -> this.menuService.viewAvailableElectricCars();
            case 7 -> this.menuService.viewAllUsers();
            case 8 -> System.exit(0);
            default -> {
                System.out.println("Invalid option: \"" + option + "\". Please enter a number between 1 and 8.");
                this.start();
            }
        }
    }

    private void handleDeleteBooking() throws IOException {
        this.menuService.viewAllBookings();
        Optional<Long> bookingId = readLong("Input booking id you want to delete");
        if (bookingId.isEmpty()) {
            return;
        }

        if (!menuService.bookingsIds().contains(bookingId.get())) {
            promptUser("Booking either not found or in invalid status");
            return;
        }

        this.menuService.deleteBooking(bookingId.get());

    }

    private void handleBookCar() throws IOException {
        this.menuService.viewAllUsers();
        promptUser("Input user id");
        String userId = consoleInputHandler.readLine();

        this.menuService.viewAvailableCars();
        promptUser("Input car id");
        String carId = consoleInputHandler.readLine();

        Optional<Long> parsedCarId = parseLong(carId);
        if (parsedCarId.isEmpty()) {
            return;
        }

        if (menuService.activeBookedCarIds().contains(parsedCarId.get())) {
            promptUser("Car with id already booked");
            return;
        }

        Optional<LocalDate> startDate = readDate("Input booking start date in format: YYYY-MM-DD");
        if (startDate.isEmpty()) {
            return;
        }

        Optional<LocalDate> endDate = readDate("Input booking end date in format: YYYY-MM-DD");
        if (endDate.isEmpty()) {
            return;
        }

        this.menuService.bookCar(userId, carId, startDate.get(), endDate.get());
    }

    private Optional<LocalDate> readDate(String promptMessage) throws IOException {
        promptUser(promptMessage);
        String rawDate = consoleInputHandler.readLine();
        try {
            return Optional.of(LocalDate.parse(rawDate.trim(), DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: \"" + rawDate + "\". Expected format: YYYY-MM-DD.");
            return Optional.empty();
        }
    }

    private Optional<Long> readLong(String promptMessage) throws IOException {
        promptUser(promptMessage);
        String raw = consoleInputHandler.readLine();
        return parseLong(raw);
    }

    private Optional<Long> parseLong(String raw) {
        try {
            return Optional.of(Long.parseLong(raw.trim()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid id: \"" + raw + "\". Expected a number.");
            return Optional.empty();
        }
    }
}
