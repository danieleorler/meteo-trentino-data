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

import com.dalendev.meteotndata.dao.MeasurementDAO;
import com.dalendev.meteotndata.domain.Measurement;
import java.util.Date;
import java.util.List;

/**
 *
 * @author danieleorler
 */
public class MeasurementService
{
    final static long _30DAYS_IN_S = 30 * 24 * 60 * 60;
    final static long _7DAYS_IN_S = 7 * 24 * 60 * 60;
    
    public List<Measurement> getMeasurements(String station, Long from, Long to)
    {
        if(to == null)
        {
            to = new Date().getTime();
        }
        
        if(from == null)
        {
            from = to - _7DAYS_IN_S*1000;
        }
        
        if(station == null || station.length() != 5)
        {
            throw new IllegalArgumentException("Station code invalid");
        }
        if(from > to)
        {
            throw new IllegalArgumentException("Date range inconsistent");
        }
        if((to-from)/1000 > _30DAYS_IN_S)
        {
            throw new IllegalArgumentException("Date range too wide (max 30 days)");
        }
        return MeasurementDAO.getMeasurements(station, from, to);
    }
}
