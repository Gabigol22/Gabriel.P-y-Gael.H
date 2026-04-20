package es.ulpgc.datos.storer;

import java.io.IOException;
import java.nio.file.*;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class EventStore {

    private final String baseDir;
    private static final DateTimeFormatter OUTPUT = DateTimeFormatter.ofPattern("yyyyMMdd")
            .withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter INPUT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm")
            .optionalStart().appendPattern(":ss").optionalEnd()
            .optionalStart().appendLiteral('Z').optionalEnd()
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter()
            .withZone(ZoneOffset.UTC);

    public EventStore() {
        this.baseDir = "eventstore";
    }

    public EventStore(String baseDir) {
        this.baseDir = baseDir;
    }

    public void store(String topic, String ss, String ts, String json) {
        try {
            String date = OUTPUT.format(INPUT.parse(ts));
            Path dir = Paths.get(baseDir, topic, ss);
            Files.createDirectories(dir);

            Path file = dir.resolve(date + ".events");
            Files.writeString(file, json + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

            System.out.println("Evento guardado en: " + file);

        } catch (IOException e) {
            System.err.println("Error al guardar evento: " + e.getMessage());
        }
    }
}