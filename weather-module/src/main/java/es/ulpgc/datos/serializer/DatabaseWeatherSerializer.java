package es.ulpgc.datos.serializer;

import es.ulpgc.datos.model.Weather;

import java.sql.*;
import java.util.List;

public class DatabaseWeatherSerializer implements WeatherSerializer {

    private static final String DB_URL = "jdbc:sqlite:weather.db";

    public DatabaseWeatherSerializer() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS weather (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    city        TEXT NOT NULL,
                    country     TEXT,
                    temperature REAL,
                    feels_like  REAL,
                    humidity    INTEGER,
                    description TEXT,
                    captured_at TEXT NOT NULL
                );
                """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    @Override
    public void serialize(List<Weather> weatherList) {
        String sql = """
                INSERT INTO weather
                    (city, country, temperature, feels_like, humidity, description, captured_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Weather weather : weatherList) {
                pstmt.setString(1, weather.getCity());
                pstmt.setString(2, weather.getCountry());
                pstmt.setDouble(3, weather.getTemperature());
                pstmt.setDouble(4, weather.getFeelsLike());
                pstmt.setInt(5, weather.getHumidity());
                pstmt.setString(6, weather.getDescription());
                pstmt.setString(7, weather.getCapturedAt().toString());
                pstmt.executeUpdate();
            }

            System.out.println("Guardados " + weatherList.size() + " registros en la base de datos.");

        } catch (SQLException e) {
            System.err.println("Error al guardar en la base de datos: " + e.getMessage());
        }
    }
}
