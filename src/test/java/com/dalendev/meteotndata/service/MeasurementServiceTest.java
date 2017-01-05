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

import com.dalendev.meteotndata.dao.MeasurementDaoInterface;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 *
 * @author danieleorler
 */
@RunWith(MockitoJUnitRunner.class)
public class MeasurementServiceTest {
    
    @Mock
    TimeService timeService;
    @Mock
    MeasurementDaoInterface measurementDao;
    
    private MeasurementService unitUnderTest;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        unitUnderTest = new MeasurementService(measurementDao, timeService);
    }
    
    /**
     * Test of getMeasurements method, of class MeasurementService.
     * Inconsistent date range
     */
    @Test
    public void testGetMeasurementsInconsistentDateRange() {
        System.out.println("getMeasurements");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Date range inconsistent");
        unitUnderTest.getMeasurements("T0420", 1454781600000L, 1454778000000L);
    }
    
    /**
     * Test of getMeasurements method, of class MeasurementService.
     * Date range too wide
     */
    @Test
    public void testGetMeasurementsDateRangeTooWide() {
        System.out.println("getMeasurements");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Date range too wide (max 30 days)");
        unitUnderTest.getMeasurements("T0420", 0L, 1454778000000L);
    }
    
    /**
     * Test of getMeasurements method, of class MeasurementService.
     * Station Code Invalid
     */
    @Test
    public void testGetMeasurementsStationCodeInvalid() {
        System.out.println("getMeasurements");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Station code invalid");
        unitUnderTest.getMeasurements("T020", 0L, 1454778000000L);
    }
    
    /**
     * Test of getMeasurements method, of class MeasurementService.
     * Station Code Invalid (null)
     */
    @Test
    public void testGetMeasurementsStationCodeInvalidNull() {
        System.out.println("getMeasurements");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Station code invalid");
        unitUnderTest.getMeasurements(null, 0L, 1454778000000L);
    }
    
    @Test
    public void testGetMeasurementsDateRangeTooWideNoTo() {
        System.out.println("getMeasurements");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Date range too wide (max 30 days)");
        when(timeService.now()).thenReturn(LocalDate.parse("2017-01-05").toDate());
        unitUnderTest.getMeasurements("T0420", LocalDate.parse("2016-01-05").plusDays(45).toDate().getTime(), null);
    }
    
    @Test
    public void testGetMeasurementsNoTimeInterval() {
        System.out.println("getMeasurements");
        when(timeService.now()).thenReturn(LocalDate.parse("2017-01-05").toDate());
        unitUnderTest.getMeasurements("T0420", null, null);
        verify(measurementDao, times(1)).getMeasurements(anyString(), any(Long.class), any(Long.class));
    }
    
}
