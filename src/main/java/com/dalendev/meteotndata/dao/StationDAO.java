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

import com.dalendev.meteotndata.domain.Station;
import com.googlecode.objectify.ObjectifyService;
import java.util.List;

/**
 *
 * @author danieleorler
 */
public class StationDAO
{
    /**
     * Retrieves all the Stations from the datastore
     * @return a list of Stations
     */
    public static List<Station> getStations()
    {
        return ObjectifyService.ofy().load().type(Station.class).list();
    }
    
    /**
     * Retrieves a Station by its code property
     * @param code
     * @return Station, null if no Station was found for the given code
     */
    public static Station getStationByCode(String code)
    {
        return ObjectifyService.ofy().load().type(Station.class).filter("code", code).first().now();
    }
    
    /**
     * Stores a list of Stations into the datastore
     * @param stations list of Stations to store
     */
    public static void storeStation(List<Station> stations)
    {
        for(Station station : stations)
        {
            StationDAO.storeStation(station);
        }
    }
    
    /**
     * Stores the given Station into the datastore
     * If the station has an Id then it will be updated
     * If the station doesn't have an Id this method will look up its code, if it cannot find it the stores the Station
     * @param station 
     */
    public static void storeStation(Station station)
    {
        if(station.getId() != null)
        {
            ObjectifyService.ofy().save().entity(station).now();
        }
        else
        {
            if(StationDAO.getStationByCode(station.getCode()) == null)
            {
                ObjectifyService.ofy().save().entity(station).now();
            }
        }
    }
    
}
