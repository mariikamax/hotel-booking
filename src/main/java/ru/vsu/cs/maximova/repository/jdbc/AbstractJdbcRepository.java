
package ru.vsu.cs.maximova.repository.jdbc;

import ru.vsu.cs.maximova.entity.AbstractEntity;
import ru.vsu.cs.maximova.ex.AppException;
import ru.vsu.cs.maximova.ex.ValidationException;
import ru.vsu.cs.maximova.repository.AbstractRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class AbstractJdbcRepository<T extends AbstractEntity>
        implements AbstractRepository<T> {

    protected final DataSource dataSource;

    protected AbstractJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    protected void validateEntity(T entity) {
        if (entity == null) {
            throw new ValidationException("Нельзя сохранить пустую сущность");
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        if (id == null) {
            return false;
        }

        String sql = getDeleteSql();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new AppException("Ошибка при удалении записи из базы данных", e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        if (id == null) {
            return false;
        }

        String sql = getExistsSql();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new AppException("Ошибка при проверке существования записи", e);
        }
    }

    protected abstract String getDeleteSql();

    protected abstract String getExistsSql();
}
