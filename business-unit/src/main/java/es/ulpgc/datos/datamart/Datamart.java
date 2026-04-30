package es.ulpgc.datos.datamart;

import java.sql.*;

public class Datamart {

    private static final String DB_URL = "jdbc:sqlite:datamart.db";

    public Datamart() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS match_weather (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    home_team   TEXT NOT NULL,
                    away_team   TEXT NOT NULL,
                    match_date  TEXT,
                    city        TEXT,
                    temperature REAL,
                    humidity    INTEGER,
                    description TEXT,
                    captured_at TEXT
                );
                """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Datamart inicializado.");
        } catch (SQLException e) {
            System.err.println("Error al crear el datamart: " + e.getMessage());
        }
    }

    public void insertMatchWeather(String homeTeam, String awayTeam, String matchDate,
                                   String city, double temperature, int humidity,
                                   String description, String capturedAt) {
        String sql = """
                INSERT INTO match_weather
                    (home_team, away_team, match_date, city, temperature, humidity, description, captured_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, homeTeam);
            pstmt.setString(2, awayTeam);
            pstmt.setString(3, matchDate);
            pstmt.setString(4, city);
            pstmt.setDouble(5, temperature);
            pstmt.setInt(6, humidity);
            pstmt.setString(7, description);
            pstmt.setString(8, capturedAt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar en datamart: " + e.getMessage());
        }
    }

    public ResultSet queryByCity(String city) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(
                "SELECT * FROM match_weather WHERE city = ? ORDER BY match_date DESC");
        pstmt.setString(1, city);
        return pstmt.executeQuery();
    }

    public ResultSet queryAll() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM match_weather ORDER BY match_date DESC");
    }
}