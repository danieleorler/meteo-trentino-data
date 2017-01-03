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
package com.dalendev.meteotndata.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import java.util.Objects;

/**
 *
 * @author danieleorler
 */
@Entity
public class Measurement
{
    @Id
    private Long id;
    @Index
    private String stationCode;
    @Index
    private Long timestamp;
    private float temperature = -100;
    private float rain = -100;
    private float windVelocity = -100;
    private int windDirection = -100;
    private int rsg = -100;

    public Measurement() {
    }

    public Measurement(Long id, String stationCode, Long timestamp) {
        this.id = id;
        this.stationCode = stationCode;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getRain() {
        return rain;
    }

    public void setRain(float rain) {
        this.rain = rain;
    }

    public float getWindVelocity() {
        return windVelocity;
    }

    public void setWindVelocity(float windVelocity) {
        this.windVelocity = windVelocity;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public int getRsg() {
        return rsg;
    }

    public void setRsg(int rsg) {
        this.rsg = rsg;
    }

    @Override
    public String toString() {
        return "Measurement{" + "id=" + id + ", stationCode=" + stationCode + ", timestamp=" + timestamp + ", temperature=" + temperature + ", rain=" + rain + ", windVelocity=" + windVelocity + ", windDirection=" + windDirection + ", rsg=" + rsg + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.stationCode);
        hash = 59 * hash + Objects.hashCode(this.timestamp);
        hash = 59 * hash + Float.floatToIntBits(this.temperature);
        hash = 59 * hash + Float.floatToIntBits(this.rain);
        hash = 59 * hash + Float.floatToIntBits(this.windVelocity);
        hash = 59 * hash + this.windDirection;
        hash = 59 * hash + this.rsg;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Measurement other = (Measurement) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.stationCode, other.stationCode)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        if (Float.floatToIntBits(this.temperature) != Float.floatToIntBits(other.temperature)) {
            return false;
        }
        if (this.rain != other.rain) {
            return false;
        }
        if (Float.floatToIntBits(this.windVelocity) != Float.floatToIntBits(other.windVelocity)) {
            return false;
        }
        if (this.windDirection != other.windDirection) {
            return false;
        }
        if (this.rsg != other.rsg) {
            return false;
        }
        return true;
    }
}
