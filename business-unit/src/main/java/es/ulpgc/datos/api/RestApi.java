package es.ulpgc.datos.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.ulpgc.datos.datamart.Datamart;
import io.javalin.Javalin;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RestApi {

    private final Datamart datamart;

    public RestApi(Datamart datamart) {
        this.datamart = datamart;
    }

    public void start(int port) {
        Javalin app = Javalin.create().start(port);

        app.get("/partidos", ctx -> {
            JsonArray result = new JsonArray();
            ResultSet rs = datamart.queryAll();
            while (rs.next()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("homeTeam", rs.getString("home_team"));
                obj.addProperty("awayTeam", rs.getString("away_team"));
                obj.addProperty("matchDate", rs.getString("match_date"));
                obj.addProperty("city", rs.getString("city"));
                obj.addProperty("temperature", rs.getDouble("temperature"));
                obj.addProperty("humidity", rs.getInt("humidity"));
                obj.addProperty("description", rs.getString("description"));
                result.add(obj);
            }
            ctx.json(result.toString());
        });

        app.get("/partidos/{city}", ctx -> {
            String city = ctx.pathParam("city");
            JsonArray result = new JsonArray();
            ResultSet rs = datamart.queryByCity(city);
            while (rs.next()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("homeTeam", rs.getString("home_team"));
                obj.addProperty("awayTeam", rs.getString("away_team"));
                obj.addProperty("matchDate", rs.getString("match_date"));
                obj.addProperty("city", rs.getString("city"));
                obj.addProperty("temperature", rs.getDouble("temperature"));
                obj.addProperty("humidity", rs.getInt("humidity"));
                obj.addProperty("description", rs.getString("description"));
                result.add(obj);
            }
            ctx.json(result.toString());
        });

        System.out.println("API REST iniciada en http://localhost:" + port);
    }
}