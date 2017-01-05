
package com.dalendev.meteotndata.domain;

import com.googlecode.objectify.annotation.Id;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dalen
 */
public class DailySummary {
    @Id
    private Long id;
    private Long timestamp;
    private String stationCode;
    private Map<String, Float> values;

    public DailySummary() {
        values = new HashMap<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Map<String, Float> getValues() {
        return values;
    }

    public void setValues(Map<String, Float> values) {
        this.values = values;
    }
    
}
