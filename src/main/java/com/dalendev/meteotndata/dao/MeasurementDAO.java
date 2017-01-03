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
package com.dalendev.meteotndata.dao;

import com.dalendev.meteotndata.domain.Measurement;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import java.util.List;

/**
 *
 * @author danieleorler
 */
public class MeasurementDAO
{

    public static void storeMeasurement(Measurement measurement)
    {
        ObjectifyService.ofy().save().entity(measurement).now();
    }
    
    /**
     * Stores a list of measurements into the datastore
     * @param measurements list of Measurement to store
     */
    public static void storeStation(List<Measurement> measurements)
    {
        for(Measurement measurement : measurements)
        {
            MeasurementDAO.storeMeasurement(measurement);
        }
    }
    
    public static List<Measurement> getMeasurements(String station, Long from, Long to)
    {
        // Query onbjects are immutable
        Query<Measurement> q = ObjectifyService.ofy().load().type(Measurement.class);
        q = q.filter("stationCode",station);
        q = q.filter("timestamp >=",from);
        q = q.filter("timestamp <=",to);
        q = q.order("timestamp");
        return q.list();
    }
    
}
