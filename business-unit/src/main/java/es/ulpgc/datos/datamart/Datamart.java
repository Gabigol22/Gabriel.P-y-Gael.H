package es.ulpgc.datos.datamart;

import java.sql.*;

public class Datamart {

    private final Connection conn;

    public Datamart(String databasePath) {
        try {
            String url = "jdbc:sqlite:" + databasePath + "?busy_timeout=5000";
            conn = DriverManager.getConnection(url);

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL");
                stmt.execute("PRAGMA synchronous=NORMAL");
            }

            createTableIfNotExists();
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con SQLite: " + e.getMessage());
        }
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS match_weather (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    home_team   TEXT NOT NULL,
                    away_team   TEXT NOT NULL,
                    home_score  INTEGER,
                    away_score  INTEGER,
                    match_date  TEXT,
                    city        TEXT,
                    temperature REAL,
                    humidity    INTEGER,
                    description TEXT,
                    captured_at TEXT
                );
                """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creando tabla: " + e.getMessage());
        }
    }

    public synchronized void insertMatchWeather(String homeTeam, String awayTeam, int homeScore, int awayScore,
                                                String matchDate, String city, double temperature, int humidity,
                                                String description, String capturedAt) {
        String checkSql = "SELECT COUNT(*) FROM match_weather WHERE home_team = ? AND away_team = ? AND match_date = ?";
        String insertSql = """
                INSERT INTO match_weather
                    (home_team, away_team, home_score, away_score, match_date, city, temperature, humidity, description, captured_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setString(1, homeTeam);
                check.setString(2, awayTeam);
                check.setString(3, matchDate);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) return;
                }
            }
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, homeTeam);
                pstmt.setString(2, awayTeam);
                pstmt.setInt(3, homeScore);
                pstmt.setInt(4, awayScore);
                pstmt.setString(5, matchDate);
                pstmt.setString(6, city);
                pstmt.setDouble(7, temperature);
                pstmt.setInt(8, humidity);
                pstmt.setString(9, description);
                pstmt.setString(10, capturedAt);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error insertando datos: " + e.getMessage());
        }
    }

    public synchronized void updateWeather(String city, double temperature, int humidity, String description) {
        String sql = "UPDATE match_weather SET temperature = ?, humidity = ?, description = ? WHERE city = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, temperature);
            pstmt.setInt(2, humidity);
            pstmt.setString(3, description);
            pstmt.setString(4, city);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualizando clima: " + e.getMessage());
        }
    }

    public synchronized ResultSet queryByCity(String city) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM match_weather WHERE city = ? ORDER BY match_date DESC");
        pstmt.setString(1, city);
        return pstmt.executeQuery();
    }

    public synchronized ResultSet queryByTeam(String team) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM match_weather WHERE home_team = ? OR away_team = ? ORDER BY match_date DESC");
        pstmt.setString(1, team);
        pstmt.setString(2, team);
        return pstmt.executeQuery();
    }

    public synchronized ResultSet queryWeatherByCity(String city) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT DISTINCT city, temperature, humidity, description FROM match_weather WHERE city = ? LIMIT 1");
        pstmt.setString(1, city);
        return pstmt.executeQuery();
    }

    public synchronized ResultSet queryRainyMatches() throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM match_weather WHERE description LIKE '%rain%' OR description LIKE '%drizzle%' ORDER BY match_date DESC");
    }

    public synchronized ResultSet queryAll() throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM match_weather ORDER BY match_date DESC");
    }
}