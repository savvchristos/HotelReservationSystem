package gr.ots.hotel.service;

import gr.ots.hotel.enums.RoomStatus;
import gr.ots.hotel.enums.RoomType;
import gr.ots.hotel.model.Customer;
import gr.ots.hotel.model.Reservation;
import gr.ots.hotel.model.Room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public List<Room> findAvailableRooms() {
        return rooms.stream()
                .filter(Room::isAvailable)
                .toList();
    }

    public List<Room> findRoomsByType(RoomType type) {
        return rooms.stream()
                .filter(r -> r.getType() == type)
                .toList();
    }

    public boolean isRoomAvailable(Room room, LocalDate start, LocalDate end) {
        return reservations.stream()
                .filter(r -> r.getRoom().getId().equals(room.getId()))
                .noneMatch(r ->
                        !r.getEndDate().isBefore(start) &&
                        !r.getStartDate().isAfter(end)
                );
    }

    public Reservation createReservation(Long id, Customer customer, Room room,
                                         LocalDate start, LocalDate end) {
        Reservation reservation = new Reservation(id, customer, room, start, end);
        reservations.add(reservation);
        room.setStatus(RoomStatus.OCCUPIED);
        return reservation;
    }

    public Reservation createReservationWithDateCheck(Long id, Customer customer, Room room,
                                                      LocalDate start, LocalDate end) {
        if (!isRoomAvailable(room, start, end)) {
            throw new IllegalArgumentException("Room not available for these dates");
        }
        return createReservation(id, customer, room, start, end);
    }

    public void cancelReservation(Long id) {
        reservations.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .ifPresent(r -> {
                    r.cancel();
                    r.getRoom().setStatus(RoomStatus.AVAILABLE);
                });
    }

    public void completeReservation(Long id) {
        reservations.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .ifPresent(Reservation::complete);
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    public List<Reservation> findReservationsByCustomer(Long customerId) {
        return reservations.stream()
                .filter(r -> r.getCustomer().getId().equals(customerId))
                .toList();
    }

    public List<Reservation> findReservationsByRoom(Long roomId) {
        return reservations.stream()
                .filter(r -> r.getRoom().getId().equals(roomId))
                .toList();
    }

    // 🔥 ΝΕΑ ΜΕΘΟΔΟΣ — Ζητούμενο 3
    public List<Reservation> findReservationsByDateRange(LocalDate start, LocalDate end) {
        return reservations.stream()
                .filter(r ->
                        !r.getEndDate().isBefore(start) &&
                        !r.getStartDate().isAfter(end)
                )
                .toList();
    }

    public double calculateReservationCost(Reservation reservation) {
        long days = reservation.getStartDate().until(reservation.getEndDate()).getDays();
        return days * reservation.getRoom().getPricePerNight();
    }

}
