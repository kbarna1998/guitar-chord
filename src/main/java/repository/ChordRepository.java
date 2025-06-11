package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.GuitarChord;

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
            return mapper.readValue(chordStream, new TypeReference<List<GuitarChord>>() {});
        }
    }

    public List<GuitarChord> updateChord(GuitarChord newChord) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        List<GuitarChord> chords;

        try (InputStream chordsStream = new FileInputStream(filePath)) {
            chords = objectMapper.readValue(chordsStream, new TypeReference<>() {});
        }

        boolean foundSameChord = false;
        for (GuitarChord chord : chords) {
            if (Objects.equals(chord.getRoot(), newChord.getRoot()) &&
                    Objects.equals(chord.getType(), newChord.getType()) &&
                    Objects.equals(chord.getModifier(), newChord.getModifier()) &&
                    Objects.equals(chord.getBass(), newChord.getBass())) {

                foundSameChord = true;
                boolean fingeringExists = chord.getFingering().stream()
                        .anyMatch(f -> f.equals(newChord.getFingering().getFirst()));

                if (!fingeringExists) {
                    chord.getFingering().addAll(newChord.getFingering());
                } else {
                    throw new IllegalStateException("Az akkord már létezik a megadott lefogással.");
                }
                break;
            }
        }

        if (!foundSameChord) {
            chords.add(newChord);
        }

        objectMapper.writeValue(new File(filePath), chords);

        return chords;
    }
}
