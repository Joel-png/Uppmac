package com.example;

import org.junit.jupiter.api.Test;

import com.uppmacparser.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test.
     */
    @Test
    void testApp() {
        assertEquals(1, 1);
    }

    @Test
    void testFunctionCounter() {
        Declaration dec = new Declaration("bool twoNhopsRoutingMatrix(){}\n\rbool twoNhopsRoutingMatrix(){}");

        int functionCounter = 0;
        functionCounter = dec.countFunctions();
        assertEquals(2, functionCounter);
    }
}
