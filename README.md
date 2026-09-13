# car-booking-cli

A console-based car booking application written in Java. Data (users, cars, bookings) is stored using a **fake
file-based database** — plain text files acting as a stand-in for a real database.

## Features

On startup, the application displays a menu with the following options:

1. **Book Car** – book a car: select a user and an available car, then provide a start and end date for the rental (
   format `YYYY-MM-DD`, with format validation). The booking price is calculated automatically based on the car's daily
   rate and the number of days.
2. **Delete Booking** – cancel an existing booking *(not yet implemented)*.
3. **View All User Booked Cars** – lists cars that are currently booked (status `ACTIVE`).
4. **View All Bookings** – lists all bookings, regardless of status.
5. **View Available Cars** – lists cars that are not currently booked.
6. **View Available Electric Cars** – same as above, filtered to electric vehicles.
7. **View All Users** – lists all registered users.
8. **Exit** – closes the application.

Invalid input (an out-of-range menu option, a badly formatted date) is detected and reported to the user without
crashing the application.

## Architecture

The project is split into domain packages (`car`, `user`, `booking`), each of which exposes only a **facade** (
`CarFacade`, `UserFacade`, `BookingFacade`) and public data models to the outside world — repositories, mappers, and
file read/write details stay hidden inside the package. The `menu` layer (`Menu`, `MenuService`) handles user
interaction and depends only on the facades.

Data persistence is a **fake database**: each domain reads from and writes to its own plain text file (
`cars_inline_db.txt`, `users_inline_db.txt`, `bookings_inline_db.txt`) instead of a real database engine.

## Technologies

- Java 21
- Maven
- Lombok
- JUnit 5, Mockito, AssertJ (testing)

## Running

The project has no command-line run plugin configured — the simplest way to run it is to execute `org.booking.Main`
directly from your IDE (e.g. in IntelliJ: right-click `Main.java` → Run).

## Tests

```bash
mvn test
```

## TODO

- **Delete Booking** — menu option exists, but `MenuService.handleDeleteBooking()` is currently an empty stub.
