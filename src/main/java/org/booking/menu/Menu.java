package org.booking.menu;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Menu {

    private final MenuService menuService;

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

    public void handleUserInput(String input) {
        switch (Integer.parseInt(input)) {
            case 1 -> this.menuService.bookCar();
            case 2 -> this.menuService.handleDeleteBooking();
            case 3 -> this.menuService.viewAllBookedCars();
            case 4 -> this.menuService.viewAllBookings();
            case 5 -> this.menuService.viewAvailableCars();
            case 6 -> this.menuService.viewAvailableEletricCars();
            case 7 -> this.menuService.viewAllUsers();
            case 8 -> System.exit(0);
            default -> this.start();
        }
    }


}
