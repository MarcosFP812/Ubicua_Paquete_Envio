/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import Logic.Log;
import Logic.Cliente;
import Logic.Ubicacion;

/**
 * Usa connectionbd para realizar las consultas a la bd
 * @author socra
 */
public class FachadaBD {
    
    private static int ultimoIdCliente;    
    
    public FachadaBD() {
        
        Log.log.info("Creando Fachada...");
        
        ultimoIdCliente = this.ultimoIdCliente();
        if (ultimoIdCliente == -1){
            Log.log.info("NO SE HA PODIDO ENCONTRAR UN ID DE UN CLIENTE PUEDE QUE HAYA ERROR A LA HORA DE LA INSERCIÓN");
        }
        
        
    }


    public static ArrayList<Cliente> getAllClientes() {
        
        ArrayList<Cliente> clientes = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectAllClientes(con);
            Log.log.info("Ejecutando: "+ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                Ubicacion ubicacion = new Ubicacion(rs.getDouble("Longitud"), rs.getDouble("Latitud"));
                cliente.setId(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setUbicacion(ubicacion);

                clientes.add(cliente);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return clientes;
    }

    public static ArrayList<Cliente> getClientesByTipo(String tipo) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectClientesByTipo(con, tipo);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Ejecutando: "+ps);

            while (rs.next()) {
                Cliente cliente = new Cliente();
                Ubicacion ubicacion = new Ubicacion(rs.getDouble("Longitud"), rs.getDouble("Latitud"));
                cliente.setId(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setUbicacion(ubicacion);

                clientes.add(cliente);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return clientes;
    }

    public static Cliente getClienteById(int id) {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        Cliente cliente = null;

        try {
            con = connector.obtainConnection(true);
            PreparedStatement ps = ConnectionDB.selectClienteById(con);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Log.log.info("Ejecutando: "+ps);

            if (rs.next()) {
                Ubicacion ubicacion = new Ubicacion(rs.getDouble("Longitud"), rs.getDouble("Latitud"));
                cliente.setId(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setUbicacion(ubicacion);
            }
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return cliente;
    }

    public static boolean insertCliente(Cliente cliente, String pw) {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        boolean success = false;
        int id = 0;

        try {
            con = connector.obtainConnection(false);
            PreparedStatement ps = ConnectionDB.insertCliente(con);
            ultimoIdCliente += 1;
            ps.setInt(1, ultimoIdCliente);
            ps.setString(2, cliente.getNombre());
            ps.setString(3, pw);
            ps.setDouble(4, cliente.getUbicacion().getLongitud());
            ps.setDouble(5, cliente.getUbicacion().getLatitud());

            Log.log.info("Ejecutando: "+ps);
            ps.executeUpdate();
            

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int clienteId = keys.getInt(1);
                PreparedStatement tipoPs = ConnectionDB.insertTipo(con, cliente.getTipo());
                tipoPs.setInt(1, clienteId);
                tipoPs.executeUpdate();
                Log.log.info("Ejecutando: "+ps);
            }

            con.commit();
            success = true;
        } catch (SQLException | NullPointerException e) {
            Log.log.info(e);
        } finally {
            connector.closeConnection(con);
        }

        return success;
    }
    
    public static int ultimoIdCliente() {
        ConnectionDB connector = new ConnectionDB();
        Connection con = null;
        int maxId = -1;

        try {
            con = connector.obtainConnection(false);
            PreparedStatement ps = ConnectionDB.selectMaxIdCliente(con);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maxId = rs.getInt("max_id"); // Obtiene el valor del campo `max_id`
                Log.log.info("Ultimo id de cliente: "+maxId);
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
    
    
    /**
     * Get the value of ultimoIdCliente
     *
     * @return the value of ultimoIdCliente
     */
    public int getUltimoIdCliente() {
        return ultimoIdCliente;
    }

    /**
     * Set the value of ultimoIdCliente
     *
     * @param ultimoIdCliente new value of ultimoIdCliente
     */
    public void setUltimoIdCliente(int ultimoIdCliente) {
        this.ultimoIdCliente = ultimoIdCliente;
    }
    
}
