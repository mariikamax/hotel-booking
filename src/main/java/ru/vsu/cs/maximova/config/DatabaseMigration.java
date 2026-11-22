package ru.vsu.cs.maximova.config;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

public class DatabaseMigration {

    public static void migrate(DataSource dataSource) {
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }
}