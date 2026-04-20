package es.ulpgc.datos.feeder;

import com.google.gson.*;
import es.ulpgc.datos.model.Match;
import es.ulpgc.datos.model.MatchMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;

public class FootballDataFeeder implements FootballFeeder {

    private static final String API_KEY = "f0f10c49cbc74e138b4f475318abdac1";
    private final String url;
    private final MatchMapper mapper = new MatchMapper();

    public FootballDataFeeder(String url) {
        this.url = url;
    }

    @Override
    public List<Match> fetchMatches() {
        List<Match> matches = new ArrayList<>();

        try {
            String json = fetchJson();
            JsonArray matchesArray = JsonParser.parseString(json)
                    .getAsJsonObject()
                    .getAsJsonArray("matches");

            for (JsonElement element : matchesArray)
                matches.add(mapper.map(element.getAsJsonObject()));

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al conectar con la API: " + e.getMessage());
        }

        return matches;
    }

    private String fetchJson() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Auth-Token", API_KEY)
                .timeout(java.time.Duration.ofSeconds(10))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}