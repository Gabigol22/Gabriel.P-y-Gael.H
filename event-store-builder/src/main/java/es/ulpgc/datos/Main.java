package es.ulpgc.datos;

import es.ulpgc.datos.listener.EventStoreListener;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        EventStoreListener listener = new EventStoreListener();
        listener.subscribe("Football");
        listener.subscribe("Weather");

        System.out.println("Event Store Builder iniciado. Esperando eventos...");

        while (true) {
            Thread.sleep(5000);
            System.out.println("Sigo esperando...");
        }
    }
}