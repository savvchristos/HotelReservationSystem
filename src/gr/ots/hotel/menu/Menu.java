package gr.ots.hotel.menu;

import gr.ots.hotel.enums.RoomType;
import gr.ots.hotel.model.Customer;
import gr.ots.hotel.model.Reservation;
import gr.ots.hotel.model.Room;
import gr.ots.hotel.service.ReservationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private ReservationService service = new ReservationService();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        seedData();

        int choice;
        do {
            printMenu();
            choice = readInt("Επιλογή: ");

            switch (choice) {
                case 1 -> showAvailableRooms();
                case 2 -> searchRoomsByType();
                case 3 -> createReservation();
                case 4 -> cancelReservation();
                case 5 -> showReservationsByCustomer();
                case 6 -> showReservationsByRoom();
                case 7 -> calculateReservationCost();
                case 8 -> showReservationsByDateRange(); // 🔥 ΝΕΟ
                case 0 -> System.out.println("Έξοδος...");
                default -> System.out.println("Μη έγκυρη επιλογή.");
            }

        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("\n===== HOTEL RESERVATION MENU =====");
        System.out.println("1. Προβολή διαθέσιμων δωματίων");
        System.out.println("2. Αναζήτηση δωματίων ανά τύπο");
        System.out.println("3. Δημιουργία κράτησης");
        System.out.println("4. Ακύρωση κράτησης");
        System.out.println("5. Προβολή κρατήσεων πελάτη");
        System.out.println("6. Προβολή κρατήσεων δωματίου");
        System.out.println("7. Υπολογισμός κόστους κράτησης");
        System.out.println("8. Προβολή κρατήσεων ανά χρονικό διάστημα"); // 🔥 ΝΕΟ
        System.out.println("0. Έξοδος");
    }

    private void seedData() {
        service.addRoom(new Room(1L, "101", RoomType.SINGLE, 50));
        service.addRoom(new Room(2L, "102", RoomType.DOUBLE, 80));
        service.addRoom(new Room(3L, "201", RoomType.SUITE, 150));
    }

    private void showAvailableRooms() {
        printRooms(service.findAvailableRooms());
    }

    private void searchRoomsByType() {
        System.out.println("Τύποι: SINGLE, DOUBLE, SUITE");
        String typeStr = readString("Εισάγετε τύπο: ");
        RoomType type = RoomType.valueOf(typeStr.toUpperCase());
        printRooms(service.findRoomsByType(type));
    }

    private void createReservation() {
        Long id = readLong("ID κράτησης: ");
        Long customerId = readLong("ID πελάτη: ");
        String first = readString("Όνομα: ");
        String last = readString("Επώνυμο: ");
        String email = readString("Email: ");

        Customer customer = new Customer(customerId, first, last, email);

        Long roomId = readLong("ID δωματίου: ");
        Room room = findRoomById(roomId);

        LocalDate start = readDate("Ημερομηνία έναρξης (YYYY-MM-DD): ");
        LocalDate end = readDate("Ημερομηνία λήξης (YYYY-MM-DD): ");

        try {
            Reservation r = service.createReservationWithDateCheck(id, customer, room, start, end);
            System.out.println("Κράτηση ολοκληρώθηκε: " + r);
        } catch (Exception e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        Long id = readLong("ID κράτησης: ");
        service.cancelReservation(id);
        System.out.println("Η κράτηση ακυρώθηκε.");
    }

    private void showReservationsByCustomer() {
        Long id = readLong("ID πελάτη: ");
        printReservations(service.findReservationsByCustomer(id));
    }

    private void showReservationsByRoom() {
        Long id = readLong("ID δωματίου: ");
        printReservations(service.findReservationsByRoom(id));
    }

    private void calculateReservationCost() {
        Long id = readLong("ID κράτησης: ");
        Reservation r = service.getAllReservations().stream()
                .filter(res -> res.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (r == null) {
            System.out.println("Δεν βρέθηκε κράτηση.");
            return;
        }

        double cost = service.calculateReservationCost(r);
        System.out.println("Κόστος: " + cost + "€");
    }

    // 🔥 ΝΕΑ ΜΕΘΟΔΟΣ — Ζητούμενο 3
    private void showReservationsByDateRange() {
        LocalDate start = readDate("Ημερομηνία έναρξης (YYYY-MM-DD): ");
        LocalDate end = readDate("Ημερομηνία λήξης (YYYY-MM-DD): ");

        List<Reservation> results = service.findReservationsByDateRange(start, end);
        printReservations(results);
    }

    private Room findRoomById(Long id) {
        return service.getAllRooms().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    private int readInt(String msg) {
        System.out.print(msg);
        return Integer.parseInt(scanner.nextLine());
    }

    private Long readLong(String msg) {
        System.out.print(msg);
        return Long.parseLong(scanner.nextLine());
    }

    private String readString(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    private LocalDate readDate(String msg) {
        System.out.print(msg);
        return LocalDate.parse(scanner.nextLine());
    }

    private void printRooms(List<Room> rooms) {
        rooms.forEach(System.out::println);
    }

    private void printReservations(List<Reservation> reservations) {
        reservations.forEach(System.out::println);
    }
}
