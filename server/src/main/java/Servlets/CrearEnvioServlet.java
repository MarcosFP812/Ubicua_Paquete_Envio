/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;


import Logic.Controlador;
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
@WebServlet("/CrearEnvio")
public class CrearEnvioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CrearEnvioServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int idTransportista = Integer.parseInt(request.getParameter("idTransportista"));
            int idPaquete = Integer.parseInt(request.getParameter("idPaquete"));
            int idReceptor = Integer.parseInt(request.getParameter("idReceptor"));
            int idRemitente = Integer.parseInt(request.getParameter("idRemitente"));
            boolean creado = Controlador.crearEnvio(idTransportista, idPaquete, idReceptor, idRemitente);
            out.print("{" + "\"creado\": " + creado + "}");
        } catch (Exception e) {
            out.println("-1");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
