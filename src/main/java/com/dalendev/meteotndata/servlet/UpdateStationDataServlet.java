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
package com.dalendev.meteotndata.servlet;

import com.dalendev.meteotndata.dao.MeasurementDaoInterface;
import com.dalendev.meteotndata.dao.MeasurementDataStoreDao;
import com.dalendev.meteotndata.dao.StationDaoInterface;
import com.dalendev.meteotndata.dao.StationDataStoreDao;
import com.dalendev.meteotndata.domain.Measurement;
import com.dalendev.meteotndata.domain.Station;
import com.dalendev.meteotndata.generated.MeasurementList;
import com.dalendev.meteotndata.service.MeasurmentSamplerService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author danieleorler
 */
public class UpdateStationDataServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        InputStream inputStream = request.getInputStream();
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        
        int length;
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer)) >= 0)
        {
            byteArrayStream.write(buffer, 0, length);
        }
        
        if (byteArrayStream.size() > 0)
        {
            Station station = SerializationUtils.deserialize(byteArrayStream.toByteArray());
            Logger.getLogger(UpdateStationDataServlet.class.getName()).log(Level.INFO, station.getCode());
            
            JAXBContext jc;
            try
            {
                jc = JAXBContext.newInstance(MeasurementList.class);
                Unmarshaller u = jc.createUnmarshaller();
                URL url = new URL("http://dati.meteotrentino.it/service.asmx/ultimiDatiStazione?codice="+station.getCode());
                StreamSource src = new StreamSource(url.openStream());
                JAXBElement je = u.unmarshal(src,MeasurementList.class);
                MeasurementList measurementList = (MeasurementList) je.getValue();
                
                MeasurmentSamplerService mss = new MeasurmentSamplerService();
                mss.mergeMeasurment(station, measurementList);
                List<Measurement> sampledList = mss.getSampledMeasurementList();
                MeasurementDaoInterface measurementDao = new MeasurementDataStoreDao();
                measurementDao.storeStation(sampledList);
                StationDaoInterface stationDao = new StationDataStoreDao();
                
                if(sampledList.size() > 0)
                {
                    Measurement lastMeasurement = sampledList.get(sampledList.size()-1);
                    station.setLastUpdate(lastMeasurement.getTimestamp());
                    stationDao.storeStation(station);
                }
                
                Logger.getLogger(UpdateStationDataServlet.class.getName()).log(Level.INFO, "Station {0} has {1} new measurements", new Object[]{station.getCode(), sampledList.size()});
            }
            catch (JAXBException ex)
            {
                Logger.getLogger(UpdateStationDataServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            response.setStatus(200);
        }
        else
        {
            Logger.getLogger(UpdateStationDataServlet.class.getName()).log(Level.INFO, "Cannot retrieve Station's serialization");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
