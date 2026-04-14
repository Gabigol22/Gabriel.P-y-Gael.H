package es.ulpgc.datos.serializer;

import es.ulpgc.datos.model.Match;
import java.sql.*;
import java.util.List;

public class DatabaseMatchSerializer implements MatchSerializer {

    private static final String DB_URL = "jdbc:sqlite:football.db";

    public DatabaseMatchSerializer() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS matches (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    home_team   TEXT NOT NULL,
                    away_team   TEXT NOT NULL,
                    home_score  INTEGER,
                    away_score  INTEGER,
                    status      TEXT,
                    competition TEXT,
                    match_date  TEXT,
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
    public void serialize(List<Match> matches) {
        String deleteSql = "DELETE FROM matches"; // <--- Línea mágica
        String insertSql = """
            INSERT INTO matches
                (home_team, away_team, home_score, away_score, status, competition, match_date, captured_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // 1. Limpiamos la tabla primero
            try (Statement deleteStmt = conn.createStatement()) {
                deleteStmt.execute(deleteSql);
            }

            // 2. Insertamos todo de nuevo
            try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                for (Match match : matches) {
                    insert.setString(1, match.getHomeTeam());
                    insert.setString(2, match.getAwayTeam());
                    insert.setInt(3, match.getHomeScore());
                    insert.setInt(4, match.getAwayScore());
                    insert.setString(5, match.getStatus());
                    insert.setString(6, match.getCompetition());
                    insert.setString(7, match.getMatchDate().toString());
                    insert.setString(8, match.getCapturedAt().toString());
                    insert.addBatch(); // Esto hace que sea mucho más rápido
                }
                insert.executeBatch();
            }
            System.out.println("Base de datos actualizada: " + matches.size() + " partidos guardados.");
        } catch (SQLException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }
}