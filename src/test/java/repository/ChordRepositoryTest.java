package repository;

import model.GuitarChord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChordRepositoryTest {

    private final String testFilePath = "test-chords.json";



    @Test
    void loadChords() {
        ChordRepository repository = new ChordRepository(testFilePath);

    }

    @Test
    void updateChord() {
    }
}