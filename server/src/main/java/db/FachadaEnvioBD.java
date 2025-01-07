/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import Clases.Cliente;
import Clases.Dato;
import Clases.Envio;
import Clases.Estado;
import Logic.Log;
import Clases.TemperaturaHumedad;
import Logic.Transportista;
import Clases.Ubicacion;
import Clases.UbicacionEnvio;
import Clases.Ventilador;
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
                    rs.getString("Estado")           // finalizado
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
                    rs.getString("Estado")          // finalizado
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
                    rs.getString("Estado")         // finalizado
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
    
    public static ArrayList<Envio> getEnviosPorClienteCancelado(int idCliente) {
        ArrayList<Envio> envios = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectEnviosClienteCancelado(con);
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
                    rs.getString("Estado")         // finalizado
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
    
    public static boolean actualizarEstado(int idEnvio, String estado){
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean valido = false;
        
        try{
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.updateEstado(con);
            ps.setString(1, estado);
            ps.setInt(2, idEnvio);
            Log.log.info("Ejecutando: " + ps);
            int rowsAffected = ps.executeUpdate();
        
            // Verificar si se actualizaron filas
            if (rowsAffected > 0) {
                valido = true;
            }
                valido = true;
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }
        
        return valido;
    }
    
    public static boolean actualizarVelocidadMedia(int idEnvio, double velocidadMedia) {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean valido = false;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.updateVelocidadMedia(con);
            ps.setDouble(1, velocidadMedia);
            ps.setInt(2, idEnvio);
            Log.log.info("Ejecutando: " + ps);
            int rowsAffected = ps.executeUpdate();

            // Verificar si se actualizaron filas
            if (rowsAffected > 0) {
                valido = true;
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return valido;
    }
    
    public static boolean actualizarPerdidaCadena(int idEnvio, double perdidaCadena) {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean valido = false;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.updatePerdidaCadena(con);
            ps.setDouble(1, perdidaCadena);
            ps.setInt(2, idEnvio);
            Log.log.info("Ejecutando: " + ps);
            int rowsAffected = ps.executeUpdate();

            // Verificar si se actualizaron filas
            if (rowsAffected > 0) {
                valido = true;
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return valido;
    }

    
    public static boolean actualizarTiempoEnvio(int idEnvio, double tiempoEnvio) {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean valido = false;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.updateTiempoEnvio(con);
            ps.setDouble(1, tiempoEnvio);
            ps.setInt(2, idEnvio);
            Log.log.info("Ejecutando: " + ps);
            int rowsAffected = ps.executeUpdate();

            // Verificar si se actualizaron filas
            if (rowsAffected > 0) {
                valido = true;
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return valido;
    }



    public static ArrayList<UbicacionEnvio> getUbicaciones(int idEnvio) {
        ArrayList<UbicacionEnvio> ubicaciones = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectUbicaciones(con);
            ps.setInt(1, idEnvio);
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UbicacionEnvio ubicacion = new UbicacionEnvio(
                        rs.getInt("Dato_idDato"),
                        rs.getDouble("Longitud"),
                        rs.getDouble("Latitud"),
                        rs.getDouble("Velocidad"),
                        rs.getDouble("Velocidad_via"),
                        rs.getTimestamp("Fecha")
                );
                ubicaciones.add(ubicacion);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return ubicaciones;
    }

    public static ArrayList<Ventilador> getVentiladores(int idEnvio) {
        ArrayList<Ventilador> ventiladores = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectVentiladores(con);
            ps.setInt(1, idEnvio);
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ventilador ventilador = new Ventilador(
                        rs.getInt("Dato_idDato"),
                        rs.getBoolean("Activo"),
                        rs.getTimestamp("Fecha")
                );
                ventiladores.add(ventilador);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return ventiladores;
    }

    public static ArrayList<Estado> getEstados(int idEnvio) {
        ArrayList<Estado> estados = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectEstados(con);
            ps.setInt(1, idEnvio);
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Estado estado = new Estado(
                        rs.getInt("Dato_idDato"),
                        rs.getString("Estado"),
                        rs.getTimestamp("Fecha")
                );
                estados.add(estado);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return estados;
    }

    public static ArrayList<TemperaturaHumedad> getTemperaturaHumedad(int idEnvio) {
        ArrayList<TemperaturaHumedad> thData = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectTemperaturaHumedad(con);
            ps.setInt(1, idEnvio);
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TemperaturaHumedad th = new TemperaturaHumedad(
                        rs.getInt("Dato_idDato"),
                        rs.getDouble("Temperatura"),
                        rs.getDouble("Humedad"),
                        rs.getTimestamp("Fecha")
                );
                thData.add(th);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return thData;
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

    public static int crearNuevoEnvio(int idTransportista, int idPaquete, int idReceptor, int idRemitente, double temperatura_max, double temperatura_min) {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean success = false;
        int id = -1;

        try {
            con = connector.obtainConnection(false);
            PreparedStatement ps = ConnectionDB.insertNuevoEnvio(con);
            id = FachadaEnvioBD.ultimoIdEnvio()+1;
            ps.setInt(1, id);
            ps.setInt(2, idTransportista);
            ps.setInt(3, idPaquete);
            ps.setInt(4, idReceptor);
            ps.setInt(5, idRemitente);
            ps.setDouble(6, temperatura_min);
            ps.setDouble(7, temperatura_max);
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

        return id;
    }
    
    public static int getUltimoIdDato(int idEnvio){
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        int maxId = -1;

        try {
            con = connector.obtainConnection(false);
            PreparedStatement ps = ConnectionDB.selectMaxIdDato(con);
            //ps.setInt(1, idEnvio);
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
    
    public static double getTemperaturaMax(int idEnvio){
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        double temp = 8;
        
        try {
            con = connector.obtainConnection(false);
            PreparedStatement ps = ConnectionDB.selectTemperaturaMax(con);
            ps.setInt(1, idEnvio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                temp = rs.getInt("Temperatura_max"); // Obtiene el valor del campo `max_id`
                Log.log.info("T max: "+temp);
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

        return temp;
    }
    
    public static UbicacionEnvio getUltimaUbicacionPorEnvio(int idEnvio) {
        UbicacionEnvio ubicacion = null;
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectUltimaUbicacion(con);
            ps.setInt(1, idEnvio);
            Log.log.info("Ejecutando: " + ps);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ubicacion = new UbicacionEnvio(
                    rs.getInt("Dato_idDato"),
                    rs.getDouble("Longitud"),
                    rs.getDouble("Latitud"),
                    rs.getDouble("Velocidad"),
                    rs.getDouble("Velocidad_via"),
                    rs.getTimestamp("Fecha")
                );
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return ubicacion;
    }
    
    public static Timestamp getUltimaFecha(int idEnvio) {
        Timestamp t = null;
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectMayorFecha(con);
            ps.setInt(1, idEnvio);
            Log.log.info("Ejecutando: " + ps);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                t = rs.getTimestamp("MayorFecha");
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return t;
    }
    
    public static Timestamp getPrimeraFecha(int idEnvio) {
        Timestamp t = null;
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectMenorFecha(con);
            ps.setInt(1, idEnvio);
            Log.log.info("Ejecutando: " + ps);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                t = rs.getTimestamp("MenorFecha");
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return t;
    }
    
    public static double getMediaTiempoEnvio(int idTransportista, int idReceptor, int idRemitente) {
        double mediaTiempoEnvio = 0.0; // Valor inicial de la media
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectMediaTiempoEnvio(con); // Utilizar la consulta preparada
            ps.setInt(1, idTransportista); // Transportista como parámetro
            ps.setInt(2, idReceptor);      // Receptor como parámetro
            ps.setInt(3, idRemitente);     // Remitente como parámetro
            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Obtener el resultado de AVG(Tiempo_envio)
                mediaTiempoEnvio = rs.getDouble("Media_Tiempo_Envio");
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return mediaTiempoEnvio;
    }
    
    public static double getMediaPerdida(int idTransportista) {
        double mediaTiempoEnvio = 0.0; // Valor inicial de la media
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectMediaPerdida(con); // Utilizar la consulta preparada
            ps.setInt(1, idTransportista); // Transportista como parámetro

            Log.log.info("Ejecutando: " + ps);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Obtener el resultado de AVG(Tiempo_envio)
                mediaTiempoEnvio = rs.getDouble("Media_Perdida");
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return mediaTiempoEnvio;
    }

    
    

}
