/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import Logic.Cliente;
import Logic.Dato;
import Logic.Envio;
import Logic.Log;
import Logic.Transportista;
import java.sql.*;
import java.util.ArrayList;

public class FachadaEnvioBD {

    public FachadaEnvioBD() {
        Log.log.info("Creando FachadaEnvioDB...");
    }

    public static ArrayList<Transportista> getTransportistas(){
        ArrayList<Transportista> trans = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        
        try{
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectAllTransportistas(con);
            
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Transportista t = new Transportista();
                
                t.setId(rs.getInt("idTransportista"));
                t.setNombre(rs.getString("Nombre"));
                
                trans.add(t);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }
        
        return trans;
    }
    
    public static ArrayList<Integer> getEnviosPorCliente(int idCliente) {
        ArrayList<Integer> envios = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectEnviosPorCliente(con);
            ps.setInt(1, idCliente);
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                envios.add(rs.getInt("idEnvio"));
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return envios;
    }

    public static ArrayList<Dato> getHistorialEnvio(int idEnvio) {
        ArrayList<Dato> historial = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectHistorialEnvio(con);
            ps.setInt(1, idEnvio);
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Dato dato = new Dato(
                    rs.getInt("idDato"),
                    rs.getInt("Envio_idEnvio"),
                    rs.getTimestamp("Fecha")
                );
                historial.add(dato);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return historial;
    }

    public static ArrayList<Integer> getEnviosActivosPorCliente(int idCliente) {
        ArrayList<Integer> envios = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectEnviosActivosPorCliente(con);
            ps.setInt(1, idCliente);
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                envios.add(rs.getInt("idEnvio"));
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return envios;
    }

    public static ArrayList<Dato> getHistorialEnvioDesdeFecha(int idEnvio, Timestamp fecha) {
        ArrayList<Dato> historial = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectHistorialEnvioDesdeFecha(con);
            ps.setInt(1, idEnvio);
            ps.setTimestamp(2, fecha);
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Dato dato = new Dato(
                    rs.getInt("idDato"),
                    rs.getInt("Envio_idEnvio"),
                    rs.getTimestamp("Fecha")
                );
                historial.add(dato);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return historial;
    }

    public static ArrayList<Cliente> getPosiblesReceptores() {
        ArrayList<Cliente> receptores = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectPosiblesReceptores(con);
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("Cliente_idCliente"));
                cliente.setNombre(rs.getString("Nombre"));
                receptores.add(cliente);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return receptores;
    }

    public static boolean crearNuevoEnvio(int idTransportista, int idPaquete, int idReceptor, int idRemitente) {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean success = false;

        try {
            con = connector.obtainConnection(false);
            PreparedStatement ps = ConnectionDB.insertNuevoEnvio(con);
            ps.setInt(1, FachadaEnvioBD.ultimoIdEnvio()+1);
            ps.setInt(2, idTransportista);
            ps.setInt(3, idPaquete);
            ps.setInt(4, idReceptor);
            ps.setInt(5, idRemitente);
            Log.log.info("Ejecutando: " + ps);

            int affectedRows = ps.executeUpdate();
            success = affectedRows > 0;

            if (success) {
                con.commit();
            } else {
                con.rollback();
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException rollbackEx) {
                Log.log.info(rollbackEx);
            }
        } finally {
            connector.closeConnection(con);
        }

        return success;
    }
    /*
    public static int getUltimoIdDato(){}

    // Métodos adicionales para registrar datos como temperatura, ubicación, ventilador y estado
    public static boolean registrarTemperaturaHumedad(Dato dato, double temperatura, double humedad) {
        return registrarDatoGenerico(ConnectionDB.insertTemperaturaHumedad(dato.getIdDato(), temperatura, humedad));
    }

    public static boolean registrarUbicacion(Dato dato, double longitud, double latitud, double velocidad, double velocidadVia) {
        return registrarDatoGenerico(ConnectionDB.insertUbicacion(dato.getIdDato(), longitud, latitud, velocidad, velocidadVia));
    }

    public static boolean registrarVentilador(Dato dato, boolean activo) {
        return registrarDatoGenerico(ConnectionDB.insertVentilador(dato.getIdDato(), activo));
    }

    public static boolean registrarCambioEstado(Dato dato, String estado) {
        return registrarDatoGenerico(ConnectionDB.insertCambioEstado(dato.getIdDato(), estado));
    }

    private static boolean registrarDatoGenerico(PreparedStatement ps) {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean success = false;

        try {
            con = connector.obtainConnection(true);
            Log.log.info("Ejecutando: " + ps);
            int affectedRows = ps.executeUpdate();
            success = affectedRows > 0;
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return success;
    }
*/
    
    public static int ultimoIdEnvio() {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        int maxId = -1;

        try {
            con = connector.obtainConnection(false);
            PreparedStatement ps = ConnectionDB.selectMaxIdEnvio(con);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maxId = rs.getInt("max_id"); // Obtiene el valor del campo `max_id`
                Log.log.info("Ultimo id de envio: "+maxId);
            } else {
                Log.log.info("No se encontraron resultados.");
            }

            // Cierra el ResultSet y PreparedStatement
            rs.close();
            ps.close();
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return maxId;
    }
}
