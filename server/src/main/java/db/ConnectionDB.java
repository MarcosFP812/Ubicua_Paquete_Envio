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

/**
 * Se encarga de establecer la conexión a la base de datos y preparar las consultas
 * @author socra
 */
public class ConnectionDB {

    public Connection obtainConnection(boolean autoCommit) throws NullPointerException {
        Connection con = null;
        int attempts = 5;
        Log.log.info("Estableciendo conexión");
        for (int i = 0; i < attempts; i++) {
            try {
                Context ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/ubica");
                con = ds.getConnection();
                con.setAutoCommit(autoCommit);
                Log.log.info("Conexión establecida");
                break;
            } catch (NamingException | SQLException ex) {
                Log.log.info("Fallido "+i);
                if (i == attempts - 1) {
                    Log.log.info(ex);
                    throw new NullPointerException("Failed to connect to the database after multiple attempts.");
                }
            }
        }
        return con;
    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static PreparedStatement getStatement(Connection con, String sql) {
        try {
            return con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Consultas específicas para la base de datos
    public static PreparedStatement selectAllClientes(Connection con) {
        return getStatement(con, "SELECT * FROM Cliente");
    }

    public static PreparedStatement selectClientesByTipo(Connection con, String tipo) {
        return getStatement(con, "SELECT * FROM Cliente WHERE idCliente IN (SELECT Cliente_idCliente FROM " + tipo + ")");
    }

    public static PreparedStatement selectClienteById(Connection con) {
        return getStatement(con, "SELECT * FROM Cliente WHERE idCliente = ?");
    }
    
    public static PreparedStatement selectMaxIdCliente(Connection con) {
        return getStatement(con, "SELECT MAX(idCliente) AS max_id FROM Cliente;");
    }

    public static PreparedStatement insertCliente(Connection con) {
        return getStatement(con, "INSERT INTO Cliente (idCliente, Nombre, PW, Longitud, Latitud) VALUES (?, ?, ?, ?, ?)");
    }

    public static PreparedStatement insertTipo(Connection con, String tipo) {
        return getStatement(con, "INSERT INTO " + tipo + " (Cliente_idCliente) VALUES (?)");
    }
}
