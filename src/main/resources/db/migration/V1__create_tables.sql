
CREATE TABLE guests (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(30) NOT NULL
);

CREATE TABLE rooms (
    id UUID PRIMARY KEY,
    number INTEGER NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    price_per_night NUMERIC(10, 2) NOT NULL CHECK (price_per_night >= 0),
    capacity INTEGER NOT NULL CHECK (capacity > 0)
);

CREATE TABLE bookings (
    id UUID PRIMARY KEY,
    guest_id UUID NOT NULL,
    room_id UUID NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    guests_count INTEGER NOT NULL CHECK (guests_count > 0),
    total_price NUMERIC(10, 2) NOT NULL CHECK (total_price >= 0),
    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_bookings_guest
        FOREIGN KEY (guest_id) REFERENCES guests(id),

    CONSTRAINT fk_bookings_room
        FOREIGN KEY (room_id) REFERENCES rooms(id),

    CONSTRAINT chk_booking_dates
        CHECK (check_out > check_in),

    CONSTRAINT chk_booking_status
        CHECK (status IN ('ACTIVE', 'CANCELLED', 'COMPLETED'))
);
