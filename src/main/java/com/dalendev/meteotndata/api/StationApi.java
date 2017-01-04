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
package com.dalendev.meteotndata.api;

import com.dalendev.meteotndata.dao.StationDataStoreDao;
import com.dalendev.meteotndata.domain.Station;
import com.dalendev.meteotndata.service.StationService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import java.util.List;

/**
 *
 * @author danieleorler
 */
@Api(name = "meteotn", version = "v1")
@ApiClass(resource = "station")
public class StationApi
{
    private StationService stationService;

    public StationApi() {
        stationService = new StationService(new StationDataStoreDao());
    }
    
    @ApiMethod
    (
        name = "station.list",
        path = "station",
        httpMethod = HttpMethod.GET
    )
    public List<Station> getStations()
    {
        return stationService.getStations();
    }
    
    
    @ApiMethod
    (
        name = "station.getByCode",
        path = "station/{stationCode}",
        httpMethod = HttpMethod.GET
    )
    public Station getStationByCode(@Named("stationCode") String code)
    {
        return stationService.getStationByCode(code);
    }
    
}
