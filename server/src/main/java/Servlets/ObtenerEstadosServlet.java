/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

/**
 *
 * @author socra
 */
import Clases.Estado;
import Logic.Controlador;
import Logic.Log;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/ObtenerEstados")
public class ObtenerEstadosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ObtenerEstadosServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int idEnvio = Integer.parseInt(request.getParameter("idEnvio"));
            ArrayList<Estado> estados = Controlador.obtenerEstados(idEnvio);
            out.print(Controlador.generarJson(estados)); // Asume método para generar JSON
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

