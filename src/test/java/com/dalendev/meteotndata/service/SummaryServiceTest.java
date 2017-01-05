/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dalendev.meteotndata.service;

import com.dalendev.meteotndata.dao.MeasurementDaoInterface;
import com.dalendev.meteotndata.dao.StationDaoInterface;
import com.dalendev.meteotndata.domain.Measurement;
import com.dalendev.meteotndata.domain.Station;
import java.util.ArrayList;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

/**
 *
 * @author dalen
 */
@RunWith(MockitoJUnitRunner.class)
public class SummaryServiceTest {
    
    @Mock
    StationDaoInterface stationDao;
    @Mock
    MeasurementDaoInterface measurementDao;
    
    private SummaryService unitUnderTest;
    
    @Before
    public void setUp() {
        unitUnderTest = new SummaryService(measurementDao, stationDao);
    }
    
    @Test
    public void testSummarizeByStationAndDay() {
        
        when(measurementDao.getMeasurements(anyString(), anyLong(), anyLong()))
                .thenReturn(getMeasurements());
        
        Station station = new Station();
        station.setCode("T0420");
        
        Map<String, Float> actual = unitUnderTest.summarizeByStationAndDay(station, LocalDate.parse("2017-01-05"));
        
        assertEquals(new Float(5), actual.get("rain"));
        assertEquals(new Float(-1.5), actual.get("max_temperature"));
        assertEquals(new Float(-4), actual.get("min_temperature"));
        assertEquals(new Float(-3), actual.get("average_temperature"));
        assertEquals(new Float(2), actual.get("average_rsg"));
        assertEquals(new Float(6.3), actual.get("max_wind"));
    }
    
    private List<Measurement> getMeasurements() {
        List<Measurement> list = new ArrayList<>();
        list.add(new Measurement(new Long(1),
                "T0420",
                TimeService.getMillis(LocalDate.parse("2017-01-05"))));
        list.add(new Measurement(new Long(2),
                "T0420",
                TimeService.getMillis(LocalDate.parse("2017-01-05").toDateTimeAtStartOfDay().plusHours(1).toLocalDate())));
        list.add(new Measurement(new Long(3),
                "T0420",
                TimeService.getMillis(LocalDate.parse("2017-01-05").toDateTimeAtStartOfDay().plusHours(2).toLocalDate())));
        
        list.get(0).setRain(new Float(1.3));
        list.get(0).setTemperature(new Float(-1.5));
        list.get(0).setRsg(3);
        list.get(0).setWindVelocity(new Float(5.3));
        list.get(1).setRain(new Float(1.3));
        list.get(1).setTemperature(new Float(-4));
        list.get(1).setRsg(2);
        list.get(1).setWindVelocity(new Float(4.3));
        list.get(2).setRain(new Float(2.4));
        list.get(2).setTemperature(new Float(-3.5));
        list.get(2).setRsg(1);
        list.get(2).setWindVelocity(new Float(6.3));
        
        return list;
    }
    
}
