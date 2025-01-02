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
    
    public static ArrayList<Envio> getEnviosPorCliente(int idCliente) {
        ArrayList<Envio> envios = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectEnviosCliente(con);
            ps.setInt(1, idCliente); // Cliente como remitente
            ps.setInt(2, idCliente); // Cliente como receptor
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Crear un objeto Envio basado en los datos de la fila
                Envio envio = new Envio(
                    rs.getInt("idEnvio"),                  // idEnvio
                    rs.getInt("Transportista_idTransportista"), // transportistaId
                    rs.getInt("Paquete_idPaquete"),       // paqueteId
                    rs.getInt("Receptor_Cliente_idCliente"), // receptorId
                    rs.getInt("Remitente_Cliente_idCliente"), // remitenteId
                    rs.getBoolean("Finalizado")           // finalizado
                );
                envios.add(envio); // Agregar el envío a la lista
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return envios;
    }
    
    public static ArrayList<Envio> getEnviosPorClienteActivo(int idCliente) {
        ArrayList<Envio> envios = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectEnviosClienteActivo(con);
            ps.setInt(1, idCliente); // Cliente como remitente
            ps.setInt(2, idCliente); // Cliente como receptor
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Crear un objeto Envio basado en los datos de la fila
                Envio envio = new Envio(
                    rs.getInt("idEnvio"),                  // idEnvio
                    rs.getInt("Transportista_idTransportista"), // transportistaId
                    rs.getInt("Paquete_idPaquete"),       // paqueteId
                    rs.getInt("Receptor_Cliente_idCliente"), // receptorId
                    rs.getInt("Remitente_Cliente_idCliente"), // remitenteId
                    rs.getBoolean("Finalizado")           // finalizado
                );
                envios.add(envio); // Agregar el envío a la lista
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return envios;
    }
    
    public static ArrayList<Envio> getEnviosPorClienteFinalizado(int idCliente) {
        ArrayList<Envio> envios = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectEnviosClienteFinalizado(con);
            ps.setInt(1, idCliente); // Cliente como remitente
            ps.setInt(2, idCliente); // Cliente como receptor
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Crear un objeto Envio basado en los datos de la fila
                Envio envio = new Envio(
                    rs.getInt("idEnvio"),                  // idEnvio
                    rs.getInt("Transportista_idTransportista"), // transportistaId
                    rs.getInt("Paquete_idPaquete"),       // paqueteId
                    rs.getInt("Receptor_Cliente_idCliente"), // receptorId
                    rs.getInt("Remitente_Cliente_idCliente"), // remitenteId
                    rs.getBoolean("Finalizado")           // finalizado
                );
                envios.add(envio); // Agregar el envío a la lista
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
    
    public static int getUltimoIdDato(int idEnvio){
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        int maxId = -1;

        try {
            con = connector.obtainConnection(false);
            PreparedStatement ps = ConnectionDB.selectMaxIdDato(con);
            ps.setInt(1, idEnvio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maxId = rs.getInt("max_id"); // Obtiene el valor del campo `max_id`
                Log.log.info("Ultimo id de dato de envio "+idEnvio+": "+maxId);
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
    
    private static Dato registrarDato(int idEnvio, Timestamp fecha) {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        Dato dato = null;
        boolean success;
        int id;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.insertDato(con);
            
            id = FachadaEnvioBD.getUltimoIdDato(idEnvio)+1;
            ps.setInt(1, id);
            ps.setInt(2, idEnvio);
            ps.setTimestamp(3, fecha);
            dato = new Dato(id, idEnvio, fecha);
            
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
        } finally {
            connector.closeConnection(con);
        }

        return dato;
    }
    
    public static boolean registrarTemperaturaHumedad(int idEnvio, Timestamp fecha, double temperatura, double humedad) {
        Dato dato = registrarDato(idEnvio, fecha);
        
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean success = false;
        
        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.insertTemperaturaHumedad(con);
            
            int id = dato.getIdDato();
            ps.setInt(1, id);
            ps.setDouble(2, temperatura);
            ps.setDouble(3, humedad);
            ps.setTimestamp(4, fecha);
            
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
        } finally {
            connector.closeConnection(con);
        }
        
        return success;
       
    }
    
    public static boolean registrarUbicacion(int idEnvio, Timestamp fecha, double longitud, double latitud, double velocidad, double velocidadVia) {
        Dato dato = registrarDato(idEnvio, fecha);
        
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean success = false;
        
        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.insertUbicacion(con);
            
            int id = dato.getIdDato();
            ps.setInt(1, id);
            ps.setDouble(2, longitud);
            ps.setDouble(3, latitud);
            if (velocidad < 0) ps.setNull(4, java.sql.Types.NULL);
            else ps.setDouble(4, velocidad);
            if (velocidadVia < 0) ps.setNull(5, java.sql.Types.NULL);
            else ps.setDouble(5, velocidadVia);
            ps.setTimestamp(6, fecha);
            
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
        } finally {
            connector.closeConnection(con);
        }
        
        return success;
    }
    

    public static boolean registrarVentilador(int idEnvio, Timestamp fecha, boolean activo) {
        Dato dato = registrarDato(idEnvio, fecha);
        
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean success = false;
        
        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.insertVentilador(con);
            
            int id = dato.getIdDato();
            ps.setInt(1, id);
            ps.setBoolean(2, activo);
            ps.setTimestamp(3, fecha);
            
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
        } finally {
            connector.closeConnection(con);
        }
        
        return success;
    }
    
    public static boolean registrarCambioEstado(int idEnvio, Timestamp fecha, String estado) {
        Dato dato = registrarDato(idEnvio, fecha);
        
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean success = false;
        
        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.insertCambioEstado(con);
            
            int id = dato.getIdDato();
            ps.setInt(1, id);
            ps.setString(2, estado);
            ps.setTimestamp(3, fecha);
            
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
        } finally {
            connector.closeConnection(con);
        }
        
        return success;
    }
    
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
