package es.ulpgc.datos.feeder;

import com.google.gson.*;
import es.ulpgc.datos.model.Match;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FootballDataFeeder implements FootballFeeder {

    private static final String API_KEY = "f0f10c49cbc74e138b4f475318abdac1";
    private static final String URL = "https://api.football-data.org/v4/competitions/PD/matches?status=FINISHED";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public List<Match> fetchMatches() {
        List<Match> matches = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("X-Auth-Token", API_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonArray matchesArray = root.getAsJsonArray("matches");

            for (JsonElement element : matchesArray) {
                JsonObject m = element.getAsJsonObject();

                String homeTeam = m.getAsJsonObject("homeTeam").get("name").getAsString();
                String awayTeam = m.getAsJsonObject("awayTeam").get("name").getAsString();

                JsonObject score = m.getAsJsonObject("score")
                        .getAsJsonObject("fullTime");

                int homeScore = score.get("home").isJsonNull() ? 0 : score.get("home").getAsInt();
                int awayScore = score.get("away").isJsonNull() ? 0 : score.get("away").getAsInt();

                String status = m.get("status").getAsString();
                String competition = m.getAsJsonObject("competition").get("name").getAsString();
                String dateStr = m.get("utcDate").getAsString();
                LocalDateTime matchDate = LocalDateTime.parse(dateStr, FORMATTER);

                matches.add(new Match(homeTeam, awayTeam, homeScore, awayScore,
                        status, competition, matchDate));
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al conectar con la API: " + e.getMessage());
        }

        return matches;
    }
}