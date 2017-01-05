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

import com.dalendev.meteotndata.dao.MeasurementDataStoreDao;
import com.dalendev.meteotndata.domain.Measurement;
import com.dalendev.meteotndata.service.MeasurementService;
import com.dalendev.meteotndata.service.TimeService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiClass;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import java.util.List;

/**
 *
 * @author danieleorler
 */
@Api(name = "meteotn", version = "v1")
@ApiClass(resource = "measurement")
public class MeasurementApi
{
    private final MeasurementService measurementService;

    public MeasurementApi() {
        this.measurementService = new MeasurementService(new MeasurementDataStoreDao(), new TimeService());
    }
    
    @ApiMethod
    (
        name = "measurement.getByStationAndPeriod",
        path = "measurement/{stationCode}",
        httpMethod = ApiMethod.HttpMethod.GET
    )
    public List<Measurement> getMeasurments(@Named("stationCode") String stationCode,
            @Nullable @Named("from") Long from,
            @Nullable @Named("to") Long to)
    {
        return measurementService.getMeasurements(stationCode, from, to);
    }
}

