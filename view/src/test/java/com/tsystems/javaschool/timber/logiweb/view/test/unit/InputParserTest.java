package com.tsystems.javaschool.timber.logiweb.view.test.unit;

import com.tsystems.javaschool.timber.logiweb.view.exceptions.IntegerOutOfRangeException;
import com.tsystems.javaschool.timber.logiweb.view.util.InputParser;
import org.junit.Test;

import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;

/**
 * Created by tims on 2/29/2016.
 */
public class InputParserTest {

    @Test
    public void parseNumber_ValidInput() throws IntegerOutOfRangeException {
        assertEquals(20, InputParser.parseNumber("20", 1, 4000));
    }

    @Test
    public void parseNumber_ValidNegativeInput() throws IntegerOutOfRangeException {
        assertEquals(-5, InputParser.parseNumber("-5", -10, 10));
    }

    @Test(expected = IntegerOutOfRangeException.class)
    public void parseNumber_OutOfRange() throws IntegerOutOfRangeException {
        assertEquals(20, InputParser.parseNumber("5000", 1, 4000));
    }

    @Test
    public void parsePlateNumber_ValidInput() throws PatternSyntaxException {
        assertEquals("XX24520", InputParser.parsePlateNumber("XX24520"));
        assertEquals("ai00120", InputParser.parsePlateNumber("ai00120"));
    }

    @Test(expected = PatternSyntaxException.class)
    public void parsePlateNumber_InvalidInput() throws PatternSyntaxException {
        assertEquals(false, InputParser.parsePlateNumber("XX2452022"));
        assertEquals(false, InputParser.parsePlateNumber("aia00120"));
        assertEquals(false, InputParser.parsePlateNumber("ssss"));
        assertEquals(false, InputParser.parsePlateNumber("2347887435234325"));
    }

    @Test
    public void parseOnlyLetters_ValidInput() {
        assertEquals("John", InputParser.parseLettersOnlyString("John"));
        assertEquals("john", InputParser.parseLettersOnlyString("john"));
        assertEquals("absdsrewqreqwerqewfsdgasd", InputParser.parseLettersOnlyString("absdsrewqreqwerqewfsdgasd"));
    }

    @Test(expected = PatternSyntaxException.class)
    public void parseOnlyLetters_InvalidInput() throws PatternSyntaxException {
        assertEquals("John", InputParser.parseLettersOnlyString("John5"));
        assertEquals("john", InputParser.parseLettersOnlyString("-john:;"));
        assertEquals("absdsrewqreqwerqewfsdgasd", InputParser.parseLettersOnlyString("J0hn"));
    }
}