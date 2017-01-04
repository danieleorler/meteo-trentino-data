/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dalendev.meteotndata.dao;

import com.dalendev.meteotndata.domain.Measurement;
import java.util.List;

/**
 *
 * @author dalen
 */
public interface MeasurementDaoInterface {
    public void storeMeasurement(Measurement measurement);
    public void storeStation(List<Measurement> measurements);
    public List<Measurement> getMeasurements(String station, Long from, Long to);
}
