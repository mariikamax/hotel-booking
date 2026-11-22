package ru.vsu.cs.maximova.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {

    private static final String DEFAULT_URL =
            "jdbc:postgresql://127.0.0.1:5434/hotel_booking";

    private static final String DEFAULT_USERNAME = "hotel_user";
    private static final String DEFAULT_PASSWORD = "hotel_password";

    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(getEnvironmentVariable("DB_URL", DEFAULT_URL));
        config.setUsername(getEnvironmentVariable("DB_USERNAME", DEFAULT_USERNAME));
        config.setPassword(getEnvironmentVariable("DB_PASSWORD", DEFAULT_PASSWORD));

        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }

    private static String getEnvironmentVariable(String name, String defaultValue) {
        String value = System.getenv(name);
        return value != null && !value.isEmpty() ? value : defaultValue;
    }
}
