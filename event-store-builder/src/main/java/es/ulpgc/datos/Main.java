package es.ulpgc.datos;

import es.ulpgc.datos.listener.EventStoreListener;
import es.ulpgc.datos.store.EventStore;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Uso: Main <broker-url>");
            return;
        }

        String brokerUrl = args[0];

        EventStore eventStore = new EventStore();
        EventStoreListener listener = new EventStoreListener(eventStore, brokerUrl);
        listener.subscribe("Football");
        listener.subscribe("Weather");

        System.out.println("Event Store Builder iniciado. Esperando eventos...");

        while (true) {
            Thread.sleep(5000);
        }
    }
}