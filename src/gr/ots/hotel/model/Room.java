package gr.ots.hotel.model;

import gr.ots.hotel.enums.RoomStatus;
import gr.ots.hotel.enums.RoomType;

public class Room {

    private Long id;
    private String number;
    private RoomType type;
    private int pricePerNight;
    private RoomStatus status;

    public Room(Long id, String number, RoomType type, int pricePerNight) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.status = RoomStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public RoomType getType() {
        return type;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return this.status == RoomStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", pricePerNight=" + pricePerNight +
                ", status=" + status +
                '}';
    }
}
