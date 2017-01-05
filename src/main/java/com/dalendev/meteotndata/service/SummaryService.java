
package com.dalendev.meteotndata.service;

import com.dalendev.meteotndata.dao.MeasurementDaoInterface;
import com.dalendev.meteotndata.dao.StationDaoInterface;
import com.dalendev.meteotndata.domain.Measurement;
import com.dalendev.meteotndata.domain.Station;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDate;

/**
 *
 * @author dalen
 */
public class SummaryService {

    private MeasurementDaoInterface measurementDao;
    private StationDaoInterface stationDao;

    public SummaryService(MeasurementDaoInterface measurementDao, StationDaoInterface stationDao) {
        this.measurementDao = measurementDao;
        this.stationDao = stationDao;
    }

    public Map<String, Float> summarizeByStationAndDay(Station station, LocalDate day) {
        List<Measurement> measurements = measurementDao.getMeasurements(
                station.getCode(),
                TimeService.getMillis(day),
                TimeService.getMillis(day.plusDays(1)));
        
        Map<String, Float> summaries = new HashMap<>();
        summaries.put("rain", new Float(0));
        summaries.put("average_temperature", new Float(-100));
        summaries.put("max_temperature", new Float(-100));
        summaries.put("min_temperature", new Float(100));
        summaries.put("average_rsg", new Float(-100));
        summaries.put("max_wind", new Float(-100));
        
        Float sumTemperature = new Float(0);
        Float sumRsg = new Float(0);
        
        for(Measurement measurement : measurements) {
            summaries.put("rain", summaries.get("rain") + measurement.getRain());
            
            if(measurement.getTemperature() > summaries.get("max_temperature")) {
                summaries.put("max_temperature", measurement.getTemperature());
            }
            
            if(measurement.getTemperature() < summaries.get("min_temperature")) {
                summaries.put("min_temperature", measurement.getTemperature());
            }
            
            if(measurement.getWindVelocity() > summaries.get("max_wind")) {
                summaries.put("max_wind", measurement.getWindVelocity());
            }
            
            sumTemperature += measurement.getTemperature();
            sumRsg += measurement.getRsg();
        }
        
        summaries.put("average_temperature", sumTemperature/measurements.size());
        summaries.put("average_rsg", sumRsg/measurements.size());
        
        return summaries;
    }
    
}
