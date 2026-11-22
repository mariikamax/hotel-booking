
package ru.vsu.cs.maximova.repository.jdbc;

import ru.vsu.cs.maximova.entity.Guest;
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

public class JdbcGuestRepository extends AbstractJdbcRepository<Guest> {

    private static final String SQL_SAVE =
            "INSERT INTO guests (id, name, email, phone) VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (id) DO UPDATE SET " +
                    "name = EXCLUDED.name, email = EXCLUDED.email, phone = EXCLUDED.phone";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, email, phone FROM guests WHERE id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT id, name, email, phone FROM guests";

    private static final String SQL_DELETE =
            "DELETE FROM guests WHERE id = ?";

    private static final String SQL_EXISTS =
            "SELECT EXISTS(SELECT 1 FROM guests WHERE id = ?)";

    public JdbcGuestRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Guest save(Guest guest) {
        validateEntity(guest);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {

            statement.setObject(1, guest.getId());
            statement.setString(2, guest.getName());
            statement.setString(3, guest.getEmail());
            statement.setString(4, guest.getPhone());

            statement.executeUpdate();
            return guest;

        } catch (SQLException e) {
            throw new AppException("Ошибка при сохранении гостя", e);
        }
    }

    @Override
    public Optional<Guest> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapGuest(resultSet));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new AppException("Ошибка при поиске гостя", e);
        }
    }

    @Override
    public List<Guest> findAll() {
        List<Guest> guests = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                guests.add(mapGuest(resultSet));
            }

            return guests;

        } catch (SQLException e) {
            throw new AppException("Ошибка при получении списка гостей", e);
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

    private Guest mapGuest(ResultSet resultSet) throws SQLException {
        return new Guest(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("phone")
        );
    }
}
