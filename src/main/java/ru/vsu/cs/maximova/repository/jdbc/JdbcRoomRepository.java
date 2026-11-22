
package ru.vsu.cs.maximova.repository.jdbc;

import ru.vsu.cs.maximova.entity.Room;
import ru.vsu.cs.maximova.entity.RoomType;
import ru.vsu.cs.maximova.ex.AppException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcRoomRepository extends AbstractJdbcRepository<Room> {

    private static final String SQL_SAVE =
            "INSERT INTO rooms (id, number, type, price_per_night, capacity) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT (id) DO UPDATE SET " +
                    "number = EXCLUDED.number, " +
                    "type = EXCLUDED.type, " +
                    "price_per_night = EXCLUDED.price_per_night, " +
                    "capacity = EXCLUDED.capacity";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, number, type, price_per_night, capacity " +
                    "FROM rooms WHERE id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT id, number, type, price_per_night, capacity FROM rooms";

    private static final String SQL_DELETE =
            "DELETE FROM rooms WHERE id = ?";

    private static final String SQL_EXISTS =
            "SELECT EXISTS(SELECT 1 FROM rooms WHERE id = ?)";

    public JdbcRoomRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Room save(Room room) {
        validateEntity(room);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {

            statement.setObject(1, room.getId());
            statement.setInt(2, room.getNumber());
            statement.setString(3, room.getType().name());
            statement.setBigDecimal(4, room.getPricePerNight());
            statement.setInt(5, room.getCapacity());

            statement.executeUpdate();
            return room;

        } catch (SQLException e) {
            throw new AppException("Ошибка при сохранении номера", e);
        }
    }

    @Override
    public Optional<Room> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRoom(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new AppException("Ошибка при поиске номера", e);
        }
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                rooms.add(mapRoom(resultSet));
            }

            return rooms;

        } catch (SQLException e) {
            throw new AppException("Ошибка при получении списка номеров", e);
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

    private Room mapRoom(ResultSet resultSet) throws SQLException {
        return new Room(
                resultSet.getObject("id", UUID.class),
                resultSet.getInt("number"),
                RoomType.valueOf(resultSet.getString("type")),
                resultSet.getBigDecimal("price_per_night"),
                resultSet.getInt("capacity")
        );
    }
}
