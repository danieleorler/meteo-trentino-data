
package com.dalendev.meteotndata.dao;

import com.dalendev.meteotndata.domain.Station;
import java.util.List;

/**
 *
 * @author dalen
 */
public interface StationDaoInterface {
    
    public List<Station> getStations();
    public Station getStationByCode(String code);
    public void storeStation(List<Station> stations);
    public void storeStation(Station station);
}
