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
        String sql = """
                INSERT INTO matches
                    (home_team, away_team, home_score, away_score, status, competition, match_date, captured_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Match match : matches) {
                pstmt.setString(1, match.getHomeTeam());
                pstmt.setString(2, match.getAwayTeam());
                pstmt.setInt(3, match.getHomeScore());
                pstmt.setInt(4, match.getAwayScore());
                pstmt.setString(5, match.getStatus());
                pstmt.setString(6, match.getCompetition());
                pstmt.setString(7, match.getMatchDate().toString());
                pstmt.setString(8, match.getCapturedAt().toString());
                pstmt.executeUpdate();
            }

            System.out.println("Guardados " + matches.size() + " partidos en la base de datos.");

        } catch (SQLException e) {
            System.err.println("Error al guardar en la base de datos: " + e.getMessage());
        }
    }
}