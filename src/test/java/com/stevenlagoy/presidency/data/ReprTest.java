package com.stevenlagoy.presidency.data;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReprTest {
    
    @Test
    public void testClassRepr() {   
        Class<String> clazz = String.class;
        String repr = Repr.classRepr(clazz);
        assertNotNull(repr);
    }

    @Test
    public void testArrayToReprList() {
        String[] arr = {"Hello", "world", "this", "is", " ", "an", "\"array\"", "."};
        String repr = Repr.arrayToReprList(arr);
        String expected = "0:\"Hello\";1:\"world\";2:\"this\";3:\"is\";4:\" \";5:\"an\";6:\"\"array\"\";7:\".\";";
        assertEquals(expected, repr);
        List<String> list = new ArrayList<>(Arrays.asList(arr));
        repr = Repr.arrayToReprList(list);
        assertEquals(expected, repr);
        arr = null;
        repr = Repr.arrayToReprList(arr);
        assertEquals("", repr);
        list = null;
        repr = Repr.arrayToReprList(list);
        assertEquals("", repr);
    }
}
