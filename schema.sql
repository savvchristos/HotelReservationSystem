CREATE TABLE Room (
    id BIGINT PRIMARY KEY,
    number VARCHAR(10) NOT NULL,
    type VARCHAR(20) NOT NULL,          -- SINGLE, DOUBLE, SUITE
    price_per_night INT NOT NULL,
    status VARCHAR(20) NOT NULL         -- AVAILABLE, OCCUPIED
);

CREATE TABLE Customer (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE Reservation (
    id BIGINT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,        -- ACTIVE, CANCELLED, COMPLETED

    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (room_id) REFERENCES Room(id)
);
