package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
    void getMinFret_1() {
        assertEquals(2, underTest.getMinFret(new String[] {"2", "4", "5", "2", "3", "6"}));
    }

    @Test
    void getMinFret_2() {
        assertEquals(0, underTest.getMinFret(new String[] {"X", "0", "2", "2", "2", "0"}));
    }

    @Test
    void getMinFret_3() {
        assertEquals(2, underTest.getMinFret(new String[] {"2", "4", "4", "3", "2", "2"}));
    }

    @Test
    void getMinFret_4() {
        assertEquals(0, underTest.getMinFret(new String[] {"X", "x", "0", "2", "3", "2"}));
    }

    @Test
    void getMode() {
    }
}