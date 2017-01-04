/*
 * The MIT License
 *
 * Copyright 2015 danieleorler.
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
import com.dalendev.meteotndata.domain.Station;
import com.dalendev.meteotndata.generated.MeasurementList;
import com.dalendev.meteotndata.generated.Precipitation;
import com.dalendev.meteotndata.generated.Radiation;
import com.dalendev.meteotndata.generated.Temperature;
import com.dalendev.meteotndata.generated.Wind;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author danieleorler
 */
public class MeasurmentSamplerService
{
    private final Map<Long,Measurement> measurementMap;
    
    public MeasurmentSamplerService()
    {
        this.measurementMap = new HashMap<>();
    }
    
    private void createMeasurementIfNotExists(String stationCode, long timestamp)
    {
        if(!this.measurementMap.containsKey(timestamp))
        {
            Measurement m = new Measurement();
            m.setTimestamp(timestamp);
            m.setStationCode(stationCode);
            this.measurementMap.put(timestamp, m);
        }
    }
    
    /**
     * Merges the measurements, as read from the source XML, into a HashMap
     * where the key is a Long representing the timestamp and the value is a 
     * Measurement object
     * @param station the Station instance whose measurements are merged
     * @param measurementList the measurements to be merged
     */
    public void mergeMeasurment(Station station, MeasurementList measurementList)
    {
        for(Temperature t : measurementList.getTemperatureList().getContent())
        {
            Long timestamp = getTimeInMilliSeconds(t.getDate());
            if(timestamp > station.getLastUpdate())
            {
                this.createMeasurementIfNotExists(station.getCode(), timestamp);
                this.measurementMap.get(timestamp).setTemperature(t.getTemperature());
            }
        }

        for(Precipitation p : measurementList.getPrecipitationList().getContent())
        {
            Long timestamp = p.getDate().toGregorianCalendar().getTimeInMillis();
            if(timestamp > station.getLastUpdate())
            {
                this.createMeasurementIfNotExists(station.getCode(), timestamp);
                this.measurementMap.get(timestamp).setRain(p.getRain());
            }
        }

        for(Wind w : measurementList.getWindList().getContent())
        {
            Long timestamp = getTimeInMilliSeconds(w.getDate());
            if(timestamp > station.getLastUpdate())
            {
                this.createMeasurementIfNotExists(station.getCode(), timestamp);
                this.measurementMap.get(timestamp).setWindDirection(w.getDirection());
                this.measurementMap.get(timestamp).setWindVelocity(w.getVelocity());
            }
        }

        for(Radiation r : measurementList.getRadiationList().getContent())
        {
            Long timestamp = getTimeInMilliSeconds(r.getDate());
            if(timestamp > station.getLastUpdate())
            {
                this.createMeasurementIfNotExists(station.getCode(), timestamp);
                this.measurementMap.get(timestamp).setRsg(r.getRsg());
            }
        }
    }
    
    public List<Measurement> getSampledMeasurementList()
    {
        SortedSet<Long> keys = new TreeSet<>(this.measurementMap.keySet());
        List<Measurement> measurements = new ArrayList<>();
        
        int counter = 0;
        float cumulativeRain = 0;
        Measurement previous = null;
        long maxAllowedHoleBetween2Measurement = 59 * 60 * 1000;
        for(long key : keys)
        {
            Measurement current = this.measurementMap.get(key);
            if(previous != null && current.getTimestamp()-previous.getTimestamp() > maxAllowedHoleBetween2Measurement)
            {
                float rain = cumulativeRain > 0 ? getRain(current) + cumulativeRain : current.getRain();
                current.setRain(rain);
                measurements.add(current);
                counter = 0;
                cumulativeRain = 0;
            }
            else
            {
                if(counter % 4 == 0)
                {
                    float rain = cumulativeRain > 0 ? getRain(current) + cumulativeRain : current.getRain();
                    current.setRain(rain);
                    measurements.add(current);
                    cumulativeRain = 0;
                }
                else
                {
                    cumulativeRain += getRain(current);
                }
            }
            previous = current;
            counter++;
        }
        
        return measurements;
    }
    
    private float getRain(Measurement m)
    {
        return m.getRain() >= 0 ? m.getRain() : 0;
    }
    
    private Long getTimeInMilliSeconds(final XMLGregorianCalendar date) {
        GregorianCalendar gregorianCalendar = date.toGregorianCalendar();
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
        return gregorianCalendar.getTimeInMillis();
    }
    
}
