package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.GuitarChord;
import service.DialogHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class ChordRepository {
    private final String filePath;

    public ChordRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<GuitarChord> loadChords() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream chordStream = new FileInputStream(filePath)) {
            return mapper.readValue(chordStream, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<GuitarChord> updateChord(GuitarChord newChord) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        List<GuitarChord> chords;

        try (InputStream chordsStream = new FileInputStream(filePath)) {
            chords = objectMapper.readValue(chordsStream, new TypeReference<>() {});
        }

        boolean duplicate = chords.stream().anyMatch(existing ->
                        Objects.equals(existing.getRoot(), newChord.getRoot()) &&
                        Objects.equals(existing.getType(), newChord.getType()) &&
                        Objects.equals(existing.getModifier(), newChord.getModifier()) &&
                        Objects.equals(existing.getBass(), newChord.getBass()) &&
                        Objects.equals(existing.getFingering(), newChord.getFingering())
        );
        if (duplicate) {
            DialogHandler.showAlert("Hiba", "Az akkord már létezik ezzel a lefogással.");
            throw new IllegalStateException();
        }

        chords.add(newChord);
        objectMapper.writeValue(new File(filePath), chords);
        return chords;
    }
}
