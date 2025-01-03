/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;


import Logic.Controlador;
import Logic.Log;
import Logic.Transportista;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.sql.Timestamp;
/**
 *
 * @author socra
 */

@WebServlet("/ObtenerTransportistas")
public class ObtenerTransportistasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ObtenerTransportistasServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            ArrayList<Transportista> transportistas = Controlador.obtenerTransportistas();
            out.print(Controlador.generarJson(transportistas));
        } catch (Exception e) {
            out.println("-1");
            Log.log.info(e);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

