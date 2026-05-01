package es.ulpgc.datos.history;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import es.ulpgc.datos.datamart.Datamart;

import java.io.IOException;
import java.nio.file.*;

public class HistoryLoader {

    private final Datamart datamart;
    private final String eventStorePath;

    public HistoryLoader(Datamart datamart, String eventStorePath) {
        this.datamart = datamart;
        this.eventStorePath = eventStorePath;
    }

    public void loadFootballHistory() {
        Path footballPath = Paths.get(eventStorePath, "Football", "football-feeder");
        loadFromPath(footballPath, "Football");
    }

    private void loadFromPath(Path basePath, String topic) {
        if (!Files.exists(basePath)) {
            System.out.println("No hay histórico en: " + basePath);
            return;
        }
        try {
            Files.walk(basePath)
                    .filter(p -> p.toString().endsWith(".events"))
                    .forEach(file -> loadFile(file, topic));
        } catch (IOException e) {
            System.err.println("Error al leer histórico: " + e.getMessage());
        }
    }

    private void loadFile(Path file, String topic) {
        try {
            Files.lines(file).forEach(line -> {
                if (line.isBlank()) return;
                JsonObject event = JsonParser.parseString(line).getAsJsonObject();
                if (topic.equals("Football")) {
                    String homeTeam = event.get("homeTeam").getAsString();
                    String awayTeam = event.get("awayTeam").getAsString();
                    String matchDate = event.get("matchDate").getAsString();
                    String ts = event.get("ts").getAsString();
                    String city = getCityForTeam(homeTeam);
                    int homeScore = event.get("homeScore").getAsInt();
                    int awayScore = event.get("awayScore").getAsInt();
                    datamart.insertMatchWeather(homeTeam, awayTeam, homeScore, awayScore, matchDate, city, 0, 0, "N/A", ts);
                }
            });
            System.out.println("Histórico cargado: " + file.getFileName());
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }

    private String getCityForTeam(String team) {
        return switch (team) {
            case "Real Madrid CF", "Club Atlético de Madrid", "Getafe CF", "Rayo Vallecano de Madrid" -> "Madrid";
            case "FC Barcelona", "RCD Espanyol de Barcelona" -> "Barcelona";
            case "Sevilla FC", "Real Betis Balompié" -> "Seville";
            case "Valencia CF", "Levante UD" -> "Valencia";
            case "Athletic Club" -> "Bilbao";
            case "Girona FC" -> "Girona";
            case "CA Osasuna" -> "Pamplona";
            case "RCD Mallorca" -> "Palma";
            case "Real Sociedad de Fútbol" -> "San Sebastian";
            case "Villarreal CF" -> "Villarreal";
            case "RC Celta de Vigo" -> "Vigo";
            case "Deportivo Alavés" -> "Vitoria";
            case "Elche CF" -> "Elche";
            case "Real Oviedo" -> "Oviedo";
            default -> "Unknown";
        };
    }
}
