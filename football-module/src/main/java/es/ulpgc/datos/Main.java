package es.ulpgc.datos;

import es.ulpgc.datos.feeder.FootballDataFeeder;
import es.ulpgc.datos.storer.DatabaseMatchSerializer;

public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller(
                new FootballDataFeeder(),
                new DatabaseMatchSerializer()
        );
        controller.start();
    }
}