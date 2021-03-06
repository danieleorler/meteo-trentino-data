/*
 * The MIT License
 *
 * Copyright 2016 danieleorler.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dalendev.meteotndata.service;

import com.dalendev.meteotndata.domain.Measurement;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author danieleorler
 */
public class MeasurementServiceTest {
    
    public MeasurementServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Test of getMeasurements method, of class MeasurementService.
     * Inconsistent date range
     */
    @Test
    public void testGetMeasurementsInconsistentDateRange() {
        System.out.println("getMeasurements");
        MeasurementService instance = new MeasurementService();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Date range inconsistent");
        instance.getMeasurements("T0420", 1454781600000L, 1454778000000L);
    }
    
    /**
     * Test of getMeasurements method, of class MeasurementService.
     * Date range too wide
     */
    @Test
    public void testGetMeasurementsDateRangeTooWide() {
        System.out.println("getMeasurements");
        MeasurementService instance = new MeasurementService();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Date range too wide (max 30 days)");
        instance.getMeasurements("T0420", 0L, 1454778000000L);
    }
    
    /**
     * Test of getMeasurements method, of class MeasurementService.
     * Station Code Invalid
     */
    @Test
    public void testGetMeasurementsStationCodeInvalid() {
        System.out.println("getMeasurements");
        MeasurementService instance = new MeasurementService();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Station code invalid");
        instance.getMeasurements("T020", 0L, 1454778000000L);
    }
    
    /**
     * Test of getMeasurements method, of class MeasurementService.
     * Station Code Invalid (null)
     */
    @Test
    public void testGetMeasurementsStationCodeInvalidNull() {
        System.out.println("getMeasurements");
        MeasurementService instance = new MeasurementService();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Station code invalid");
        instance.getMeasurements(null, 0L, 1454778000000L);
    }
    
}
