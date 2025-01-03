/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;


import Clases.Cliente;
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
@WebServlet("/ObtenerReceptores")
public class ObtenerReceptoresServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ObtenerReceptoresServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            ArrayList<Cliente> receptores = Controlador.obtenerReceptores();
            out.print(receptores);
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
