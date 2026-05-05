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

        getMatches(app);

        app.get("/partidos/{city}", ctx -> {
            String city = ctx.pathParam("city");
            ctx.json(resultSetToJson(datamart.queryByCity(city)).toString());
        });

        app.get("/partidos/equipo/{team}", ctx -> {
            String team = ctx.pathParam("team");
            ctx.json(resultSetToJson(datamart.queryByTeam(team)).toString());
        });

        app.get("/tiempo/{city}", ctx -> {
            String city = ctx.pathParam("city");
            ResultSet rs = datamart.queryWeatherByCity(city);
            JsonObject obj = new JsonObject();
            if (rs.next()) {
                obj.addProperty("city", rs.getString("city"));
                obj.addProperty("temperature", rs.getDouble("temperature"));
                obj.addProperty("humidity", rs.getInt("humidity"));
                obj.addProperty("description", rs.getString("description"));
            } else {
                obj.addProperty("message", "No hay datos para " + city);
            }
            ctx.json(obj.toString());
        });

        app.get("/alertas/lluvia", ctx -> {
            ctx.json(resultSetToJson(datamart.queryRainyMatches()).toString());
        });

        System.out.println("API REST iniciada en http://localhost:" + port);
    }

    private void getMatches(Javalin app) {
        app.get("/partidos", ctx -> {
            ctx.json(resultSetToJson(datamart.queryAll()).toString());
        });
    }

    private JsonArray resultSetToJson(ResultSet rs) throws SQLException {
        JsonArray result = new JsonArray();
        while (rs.next()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("homeTeam", rs.getString("home_team"));
            obj.addProperty("awayTeam", rs.getString("away_team"));
            obj.addProperty("homeScore", rs.getInt("home_score"));
            obj.addProperty("awayScore", rs.getInt("away_score"));
            obj.addProperty("matchDate", rs.getString("match_date"));
            obj.addProperty("city", rs.getString("city"));
            obj.addProperty("temperature", rs.getDouble("temperature"));
            obj.addProperty("humidity", rs.getInt("humidity"));
            obj.addProperty("description", rs.getString("description"));
            result.add(obj);
        }
        return result;
    }
}