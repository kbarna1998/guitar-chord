package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChordManagerControllerTest {

    private ChordManagerController underTest;

    @BeforeEach
    void setUp() {underTest = new ChordManagerController();}

    @Test
    void firstLetterUpperCase() {
        assertEquals("A", underTest.firstLetterUpperCase("a"));
        assertEquals("B", underTest.firstLetterUpperCase("B"));
        assertEquals("C#", underTest.firstLetterUpperCase("c#"));
        assertEquals("Db", underTest.firstLetterUpperCase("db"));
        assertEquals("Kiskutya", underTest.firstLetterUpperCase("kIsKuTyA"));
    }

    @Test
    void getMode() {
    }
}