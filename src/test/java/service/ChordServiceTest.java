package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ChordRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ChordServiceTest {
    private ChordService chordService;

    @BeforeEach
    void setUp() {
        ChordRepository repository = new ChordRepository("test-chords.json");
        chordService = new ChordService(repository);
    }

    @Test
    void searchChord() throws IOException {
        //assertEquals("C", chordService.searchChord("C", "", "", "").getFirst().getRoot());
    }

    @Test
    void createChord() {
    }

    @Test
    void validateNoteInput() {
        assertDoesNotThrow(() -> chordService.validateNoteInput("C", "alaphang"));
        assertDoesNotThrow(() -> chordService.validateNoteInput("g#", "basszushang"));
        assertDoesNotThrow(() -> chordService.validateNoteInput("H", "alaphang"));
        assertThrows(InvalidInput.class, () -> chordService.validateNoteInput(null, "basszushang"));
        //assertThrows(InvalidInput.class, () -> chordService.validateNoteInput("", "basszushang"));
        //assertThrows(InvalidInput.class, () -> chordService.validateNoteInput("J", "basszushang"));
        //assertThrows(InvalidInput.class, () -> chordService.validateNoteInput("Kiskutya", "basszushang"));
    }

    @Test
    void isNoteExist() {
        //assertDoesNotThrow(() -> chordService.isNoteExist("C", "alaphang"));
        //assertDoesNotThrow(() -> chordService.isNoteExist("D", "basszushang"));
        //assertThrows(InvalidInput.class, () -> chordService.isNoteExist(null, "alaphang"));
        //assertThrows(InvalidInput.class, () -> chordService.isNoteExist("", "basszushang"));
    }

    @Test
    void fingeringValidate() throws InvalidInput {
        assertArrayEquals(new String[]{"1", "2", "3", "4", "5", "6"}, chordService.fingeringValidate("1 2 3 4 5 6"));
        assertArrayEquals(new String[]{"X", "2", "3", "4", "5", "6"}, chordService.fingeringValidate("x 2 3 4 5 6"));
        assertArrayEquals(new String[]{"X", "2", "3", "4", "5", "6"}, chordService.fingeringValidate("X 2 3 4 5 6"));
        //assertThrows(InvalidInput.class, () -> chordService.fingeringValidate("L 2 3 4 5 6"));
    }

    @Test
    void saveChord() {
    }

    @Test
    void loadAllChords() {
    }

    @Test
    void getMinFret() {
        assertEquals(2, chordService.getMinFret(new String[] {"2", "4", "5", "2", "3", "6"}));
        assertEquals(0, chordService.getMinFret(new String[] {"X", "0", "2", "2", "2", "0"}));
        assertEquals(2, chordService.getMinFret(new String[] {"2", "4", "4", "3", "2", "2"}));
        assertEquals(0, chordService.getMinFret(new String[] {"X", "x", "0", "2", "3", "2"}));
    }
}