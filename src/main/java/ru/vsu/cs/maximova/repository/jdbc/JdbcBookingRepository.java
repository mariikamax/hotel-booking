
package ru.vsu.cs.maximova.repository.jdbc;

import ru.vsu.cs.maximova.entity.Booking;
import ru.vsu.cs.maximova.entity.BookingStatus;
import ru.vsu.cs.maximova.entity.Guest;
import ru.vsu.cs.maximova.entity.Room;
import ru.vsu.cs.maximova.ex.AppException;
import ru.vsu.cs.maximova.ex.EntityNotFoundException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcBookingRepository extends AbstractJdbcRepository<Booking> {

    private static final String SQL_SAVE =
            "INSERT INTO bookings " +
                    "(id, guest_id, room_id, check_in, check_out, guests_count, total_price, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (id) DO UPDATE SET " +
                    "guest_id = EXCLUDED.guest_id, " +
                    "room_id = EXCLUDED.room_id, " +
                    "check_in = EXCLUDED.check_in, " +
                    "check_out = EXCLUDED.check_out, " +
                    "guests_count = EXCLUDED.guests_count, " +
                    "total_price = EXCLUDED.total_price, " +
                    "status = EXCLUDED.status";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, guest_id, room_id, check_in, check_out, " +
                    "guests_count, total_price, status FROM bookings WHERE id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT id, guest_id, room_id, check_in, check_out, " +
                    "guests_count, total_price, status FROM bookings";

    private static final String SQL_DELETE =
            "DELETE FROM bookings WHERE id = ?";

    private static final String SQL_EXISTS =
            "SELECT EXISTS(SELECT 1 FROM bookings WHERE id = ?)";

    private final JdbcGuestRepository guestRepository;
    private final JdbcRoomRepository roomRepository;

    public JdbcBookingRepository(
            DataSource dataSource,
            JdbcGuestRepository guestRepository,
            JdbcRoomRepository roomRepository
    ) {
        super(dataSource);
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public Booking save(Booking booking) {
        validateEntity(booking);

        if (booking.getGuest() == null || booking.getRoom() == null) {
            throw new AppException("У бронирования должны быть указаны гость и номер");
        }

        if (!guestRepository.existsById(booking.getGuest().getId())) {
            throw new EntityNotFoundException("Гость не найден в базе данных");
        }

        if (!roomRepository.existsById(booking.getRoom().getId())) {
            throw new EntityNotFoundException("Номер не найден в базе данных");
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {

            statement.setObject(1, booking.getId());
            statement.setObject(2, booking.getGuest().getId());
            statement.setObject(3, booking.getRoom().getId());
            statement.setDate(4, Date.valueOf(booking.getCheckIn()));
            statement.setDate(5, Date.valueOf(booking.getCheckOut()));
            statement.setInt(6, booking.getGuestsCount());
            statement.setBigDecimal(7, booking.getTotalPrice());
            statement.setString(8, booking.getStatus().name());

            statement.executeUpdate();
            return booking;

        } catch (SQLException e) {
            throw new AppException("Ошибка при сохранении бронирования", e);
        }
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapBooking(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new AppException("Ошибка при поиске бронирования", e);
        }
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bookings.add(mapBooking(resultSet));
            }

            return bookings;

        } catch (SQLException e) {
            throw new AppException("Ошибка при получении списка бронирований", e);
        }
    }

    @Override
    protected String getDeleteSql() {
        return SQL_DELETE;
    }

    @Override
    protected String getExistsSql() {
        return SQL_EXISTS;
    }

    private Booking mapBooking(ResultSet resultSet) throws SQLException {
        UUID guestId = resultSet.getObject("guest_id", UUID.class);
        UUID roomId = resultSet.getObject("room_id", UUID.class);

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Гость бронирования не найден"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Номер бронирования не найден"));

        return new Booking(
                resultSet.getObject("id", UUID.class),
                guest,
                room,
                resultSet.getDate("check_in").toLocalDate(),
                resultSet.getDate("check_out").toLocalDate(),
                resultSet.getInt("guests_count"),
                resultSet.getBigDecimal("total_price"),
                BookingStatus.valueOf(resultSet.getString("status"))
        );
    }
}
