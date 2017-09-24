package com.netshell.libraries.utilities.converter;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Abhishek
 * on 3/24/2016.
 */
public class ConverterTest {
    @Test
    public void converterTest() {
        assertEquals(101, Converters.getConverter(Converters.StringToIntegerConverter.class).convert("101"));
        assertEquals("101", Converters.getConverter(Converters.StringToIntegerConverter.class).convertBack(101));

        assertEquals(false, Converters.getConverter(Converters.StringToBooleanConverter.class).convert("101"));
        assertEquals(true, Converters.getConverter(Converters.StringToBooleanConverter.class).convert("True"));
        assertEquals("false", Converters.getConverter(Converters.StringToBooleanConverter.class).convertBack(false));
        assertEquals("true", Converters.getConverter(Converters.StringToBooleanConverter.class).convertBack(true));

        assertEquals(true, Converters.getConverter(Converters.IntegerToBooleanConverter.class).convert(10));
        assertEquals(false, Converters.getConverter(Converters.IntegerToBooleanConverter.class).convert(0));
        assertEquals(1, Converters.getConverter(Converters.IntegerToBooleanConverter.class).convertBack(true));
        assertEquals(0, Converters.getConverter(Converters.IntegerToBooleanConverter.class).convertBack(false));
    }
}