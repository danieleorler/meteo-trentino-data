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
package com.dalendev.meteotndata.service;

import com.dalendev.meteotndata.domain.Measurement;
import com.dalendev.meteotndata.domain.Station;
import com.dalendev.meteotndata.generated.MeasurementList;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author danieleorler
 */
public class MeasurmentSamplerServiceTest {
    
    public MeasurmentSamplerServiceTest()
    {

    }
    
    private MeasurementList getMeasurementList(String fileName) throws JAXBException, MalformedURLException, IOException
    {
        JAXBContext jc = JAXBContext.newInstance(MeasurementList.class);
        Unmarshaller u = jc.createUnmarshaller();
        URL url =  Thread.currentThread().getContextClassLoader().getResource("testdata/"+fileName+".xml");
        StreamSource src = new StreamSource(url.openStream());
        JAXBElement je = u.unmarshal(src,MeasurementList.class);
        return (MeasurementList) je.getValue();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getSampledMeasurementList method, of class MeasurmentSamplerService.
     */
    @Test
    public void testGetSampledMeasurementList() throws JAXBException, IOException
    {
        System.out.println("getSampledMeasurementList");
        MeasurmentSamplerService instance = new MeasurmentSamplerService();
        Station s = new Station();
        s.setCode("T0420");
        s.setLastUpdate(0);
        instance.mergeMeasurment(s, getMeasurementList("measurement_input"));
        List<Measurement> expResult = new ArrayList<>();
        expResult.add(new Measurement(null, "T0420", new Long("1450134000000")));
        expResult.get(0).setTemperature((float) -2.4);
        expResult.get(0).setRain(12);
        expResult.get(0).setWindDirection(270);
        expResult.get(0).setWindVelocity((float) 3.5);
        expResult.get(0).setRsg(0);
        expResult.add(new Measurement(null, "T0420", new Long("1450138500000")));
        expResult.get(1).setTemperature((float) -2.6);
        expResult.get(1).setRain((float) 3);
        expResult.add(new Measurement(null, "T0420", new Long("1450160100000")));
        expResult.get(2).setWindDirection(0);
        expResult.get(2).setWindVelocity(0);
        List<Measurement> result = instance.getSampledMeasurementList();
        System.out.println(result);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getSampledMeasurementList method, of class MeasurmentSamplerService.
     */
    @Test
    public void testGetSampledMeasurementListRain() throws JAXBException, IOException
    {
        System.out.println("getSampledMeasurementListRain");
        MeasurmentSamplerService instance = new MeasurmentSamplerService();
        Station s = new Station();
        s.setCode("T0420");
        s.setLastUpdate(0);
        instance.mergeMeasurment(s, getMeasurementList("measurement_input_rain"));
        List<Measurement> expResult = new ArrayList<>();
        expResult.add(new Measurement(null, "T0420", new Long("1455675300000")));
        expResult.get(0).setTemperature((float) 3.1);
        expResult.get(0).setRain((float) 0.4);
        expResult.get(0).setWindDirection(-100);
        expResult.get(0).setWindVelocity((float) -100);
        expResult.get(0).setRsg(-100);
        expResult.add(new Measurement(null, "T0420", new Long("1455678900000")));
        expResult.get(1).setTemperature((float) 3.5);
        expResult.get(1).setRain((float) 1.4);
        expResult.get(1).setWindDirection(-100);
        expResult.get(1).setWindVelocity((float) -100);
        expResult.get(1).setRsg(-100);
        List<Measurement> result = instance.getSampledMeasurementList();
        assertEquals(expResult, result);
    }
}
