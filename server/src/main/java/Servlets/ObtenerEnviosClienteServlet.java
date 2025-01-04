/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;


import Logic.Controlador;
import Clases.Envio;
import Logic.Log;
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

@WebServlet("/ObtenerEnviosCliente")
public class ObtenerEnviosClienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ObtenerEnviosClienteServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int idCliente = Integer.parseInt(request.getParameter("idCliente"));
            Log.log.info("Id pasado por postman: "+idCliente);
            ArrayList<Envio> enviosEnvio = Controlador.obtenerEnviosClienteActivo(idCliente);
            Log.log.info(Controlador.generarJson(enviosEnvio)+enviosEnvio.size());
            ArrayList<Envio> enviosEnviados = Controlador.obtenerEnviosClienteFinalizado(idCliente);
            Log.log.info(Controlador.generarJson(enviosEnviados)+enviosEnviados.size());
            ArrayList<Envio> enviosCancelados = Controlador.obtenerEnviosClienteCancelado(idCliente);
            Log.log.info(Controlador.generarJson(enviosCancelados)+enviosCancelados.size());
            
            String r = "{"+"\"Envio\": \n"+Controlador.generarJson(enviosEnvio)+
                       ","+"\"Enviado\": \n"+Controlador.generarJson(enviosEnviados)+
                       ","+"\"Cancelado\": \n"+Controlador.generarJson(enviosCancelados)+"}";
                          
            out.print(r);
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

