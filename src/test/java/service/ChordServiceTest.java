package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ChordRepository;

import static org.junit.jupiter.api.Assertions.*;

class ChordServiceTest {
    private ChordService underTest;

    @BeforeEach
    void setUp() {
        ChordRepository repository = new ChordRepository("test-chords.json");
        underTest = new ChordService(repository);
    }

    @Test
    void searchChord() {
    }

    @Test
    void createChord() {
    }

    @Test
    void saveChord() {
    }

    @Test
    void loadAllChords() {
    }

    @Test
    void getMinFret() {
        assertEquals(2, underTest.getMinFret(new String[] {"2", "4", "5", "2", "3", "6"}));
        assertEquals(0, underTest.getMinFret(new String[] {"X", "0", "2", "2", "2", "0"}));
        assertEquals(2, underTest.getMinFret(new String[] {"2", "4", "4", "3", "2", "2"}));
        assertEquals(0, underTest.getMinFret(new String[] {"X", "x", "0", "2", "3", "2"}));
    }
}