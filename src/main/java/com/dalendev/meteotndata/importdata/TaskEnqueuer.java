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
package com.dalendev.meteotndata.importdata;

import com.dalendev.meteotndata.domain.Station;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author danieleorler
 */
public class TaskEnqueuer
{
    /**
     * Enqueue a new task for each Station contained into the given list
     * @param stationList the list of stations whose data will be updated
     */
    public static void enqueueAllStations(List<Station> stationList)
    {
        for(Station station : stationList)
        {
            TaskEnqueuer.equeueUpdateStation(station);
        }
    }
    
    /**
     * Enqueue a new task for the given Station.
     * The station will be serialized and passed as parameter to the task
     * @param station the Station
     * @return Boolean
     */
    public static Boolean equeueUpdateStation(Station station)
    {
        Queue queue = QueueFactory.getDefaultQueue();
        try
        {
            byte[] serializedStation = SerializationUtils.serialize(station);
            queue.add(TaskOptions.Builder.withUrl("/tasks/updateStationData").payload(serializedStation));
            return true;
        }
        catch(SerializationException e)
        {
            Logger.getLogger(Importer.class.getName()).log(Level.WARNING, "Cannot Serialize a Station object", e);
            return false;
        }
    }
}
