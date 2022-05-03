package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PredictorCalculationsTest {

    @Test
    void calculatePredictedXGTest() {
        PredictorCalculations predictorCalculations = new PredictorCalculations();
        double expected = 1.2 * 0.8 * 1.3;
        assertEquals(expected, predictorCalculations.calculatePredictedXG(1.2, 0.8, 1.3));
    }
}