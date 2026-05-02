package es.ulpgc.datos;

import es.ulpgc.datos.api.RestApi;
import es.ulpgc.datos.consumer.EventConsumer;
import es.ulpgc.datos.datamart.Datamart;
import es.ulpgc.datos.history.HistoryLoader;

public class Main {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Uso: Main <broker-url> <eventstore-path> <datamart-path>");
            return;
        }

        String brokerUrl = args[0];
        String eventStorePath = args[1];
        String datamartPath = args[2];

        Datamart datamart = new Datamart(datamartPath);

        System.out.println("Cargando histórico...");
        HistoryLoader loader = new HistoryLoader(datamart, eventStorePath);
        loader.loadFootballHistory();

        System.out.println("Suscribiendo a eventos en tiempo real...");
        EventConsumer consumer = new EventConsumer(brokerUrl, datamart);
        consumer.subscribe("Football");
        consumer.subscribe("Weather");

        System.out.println("Iniciando API REST...");
        RestApi api = new RestApi(datamart);
        api.start(7070);
    }
}