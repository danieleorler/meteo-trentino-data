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

import com.dalendev.meteotndata.dao.StationDaoInterface;
import com.dalendev.meteotndata.dao.StationDataStoreDao;
import com.dalendev.meteotndata.domain.Station;
import com.dalendev.meteotndata.generated.WeatherStation;
import com.dalendev.meteotndata.generated.WeatherStationList;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

/**
 *
 * @author danieleorler
 */
public class ImportStationServlet extends HttpServlet
{

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
        try
        {
            JAXBContext jc = JAXBContext.newInstance(WeatherStationList.class);
            Unmarshaller u = jc.createUnmarshaller();
            URL url = new URL("http://dati.meteotrentino.it/service.asmx/listaStazioni");
            StreamSource src = new StreamSource(url.openStream());
            JAXBElement je = u.unmarshal(src,WeatherStationList.class);

            WeatherStationList o = (WeatherStationList) je.getValue();
            
            StationDaoInterface stationDao = new StationDataStoreDao();
        
            for(WeatherStation ws : o.getStationList())
            {
                stationDao.storeStation(new Station(ws));
            }
            response.setStatus(200);
        }
        catch (JAXBException ex) 
        {
            Logger.getLogger(ImportStationServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(500);
        }
        catch (MalformedURLException ex)
        {
            Logger.getLogger(ImportStationServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(500);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ImportStationServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(500);
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
