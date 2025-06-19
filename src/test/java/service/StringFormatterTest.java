package service;

import controller.ChordManagerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringFormatterTest {
    private StringFormatter underTest;

    @BeforeEach
    void setUp() {underTest = new StringFormatter();}

    @Test
    void firstLetterUpperCase() {
        assertEquals("A", underTest.firstLetterUpperCase("a"));
        assertEquals("B", underTest.firstLetterUpperCase("B"));
        assertEquals("C#", underTest.firstLetterUpperCase("c#"));
        assertEquals("Db", underTest.firstLetterUpperCase("db"));
        assertEquals("Kiskutya", underTest.firstLetterUpperCase("kIsKuTyA"));
    }
}