package es.ulpgc.datos.model;

import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatchMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public Match map(JsonObject m) {
        String homeTeam = m.getAsJsonObject("homeTeam").get("name").getAsString();
        String awayTeam = m.getAsJsonObject("awayTeam").get("name").getAsString();

        JsonObject fullTime = m.getAsJsonObject("score").getAsJsonObject("fullTime");
        int homeScore = fullTime.get("home").isJsonNull() ? 0 : fullTime.get("home").getAsInt();
        int awayScore = fullTime.get("away").isJsonNull() ? 0 : fullTime.get("away").getAsInt();

        String status = m.get("status").getAsString();
        String competition = m.getAsJsonObject("competition").get("name").getAsString();
        LocalDateTime matchDate = LocalDateTime.parse(m.get("utcDate").getAsString(), FORMATTER);

        return new Match(homeTeam, awayTeam, homeScore, awayScore, status, competition, matchDate);
    }
}