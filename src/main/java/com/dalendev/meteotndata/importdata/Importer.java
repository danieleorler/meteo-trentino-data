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

import com.dalendev.meteotndata.domain.Measurement;
import com.dalendev.meteotndata.generated.MeasurementList;
import com.dalendev.meteotndata.generated.Precipitation;
import com.dalendev.meteotndata.generated.Radiation;
import com.dalendev.meteotndata.generated.Temperature;
import com.dalendev.meteotndata.generated.WeatherStation;
import com.dalendev.meteotndata.generated.WeatherStationList;
import com.dalendev.meteotndata.generated.Wind;
import com.dalendev.meteotndata.service.TimeService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author danieleorler
 */
public class Importer
{

    public static void main(String[] args) throws JAXBException, MalformedURLException, IOException
    {
//        readAll();
        Map<Long,Measurement> merged = mergeMeasurment("T0129");
        
        for(Measurement m : merged.values())
        {
            System.out.println(new Date(m.getTimestamp())+"-"+m);
            ObjectifyService.ofy().save().entity(m).now();
        }
        System.out.println(merged.size());
    }
    
    public static Boolean storeStations()
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(WeatherStationList.class);
            Unmarshaller u = jc.createUnmarshaller();
            URL url = new URL("http://dati.meteotrentino.it/service.asmx/listaStazioni");
            StreamSource src = new StreamSource(url.openStream());
            JAXBElement je = u.unmarshal(src,WeatherStationList.class);

            WeatherStationList o = (WeatherStationList) je.getValue();
        
            for(WeatherStation ws : o.getStationList())
            {
                Query<WeatherStation> q = ObjectifyService.ofy().load().type(WeatherStation.class);
                q.filter("code", ws.getCode());
                if(q.count() == 0)
                {
                    ObjectifyService.ofy().save().entity(ws).now();
                }
            }
            return true;
        }
        catch (JAXBException ex) 
        {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        catch (MalformedURLException ex)
        {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        catch (IOException ex)
        {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
    
    public static Map<Long,Measurement> mergeMeasurment(String code) throws JAXBException, MalformedURLException, IOException
    {
        JAXBContext jc2 = JAXBContext.newInstance(MeasurementList.class);
        Unmarshaller u2 = jc2.createUnmarshaller();
        URL url2 = new URL("http://dati.meteotrentino.it/service.asmx/ultimiDatiStazione?codice="+code);
        StreamSource src2 = new StreamSource(url2.openStream());
        JAXBElement je2 = u2.unmarshal(src2,MeasurementList.class);
        MeasurementList o2 = (MeasurementList) je2.getValue();

        Map<Long,Measurement> merged = new HashMap<>();

        for(Temperature t : o2.getTemperatureList().getContent())
        {
            Long timestamp = TimeService.getMillis(t.getDate());
            if(!merged.containsKey(timestamp))
            {
                Measurement m = new Measurement();
                m.setTimestamp(timestamp);
                m.setStationCode(code);
                merged.put(timestamp, m);
            }
            merged.get(timestamp).setTemperature(t.getTemperature());
        }

        for(Precipitation p : o2.getPrecipitationList().getContent())
        {
            Long timestamp = TimeService.getMillis(p.getDate());
            if(!merged.containsKey(timestamp))
            {
                Measurement m = new Measurement();
                m.setTimestamp(timestamp);
                m.setStationCode(code);
                merged.put(timestamp, m);
            }
            merged.get(timestamp).setRain(p.getRain());
        }

        for(Wind w : o2.getWindList().getContent())
        {
            Long timestamp = TimeService.getMillis(w.getDate());
            if(!merged.containsKey(timestamp))
            {
                Measurement m = new Measurement();
                m.setTimestamp(timestamp);
                m.setStationCode(code);
                merged.put(timestamp, m);
            }
            merged.get(timestamp).setWindDirection(w.getDirection());
            merged.get(timestamp).setWindVelocity(w.getVelocity());
        }

        for(Radiation r : o2.getRadiationList().getContent())
        {
            Long timestamp = TimeService.getMillis(r.getDate());
            if(!merged.containsKey(timestamp))
            {
                Measurement m = new Measurement();
                m.setTimestamp(timestamp);
                m.setStationCode(code);
                merged.put(timestamp, m);
            }
            merged.get(timestamp).setRsg(r.getRsg());
        }
        return merged;
    }
    
}
