package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import Logic.Log;


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
    
    public static PreparedStatement selectAllTransportistas(Connection con) {
        return getStatement(con, "SELECT * FROM Transportista");
    }

    public static PreparedStatement selectClientesByTipo(Connection con, String tipo) {
        return getStatement(con, "SELECT * FROM Cliente WHERE idCliente IN (SELECT Cliente_idCliente FROM " + tipo + ")");
    }
    
    public static PreparedStatement selectTipo(Connection con){
        return getStatement(con, "SELECT \n" +
                            "    CASE \n" +
                            "        WHEN EXISTS (\n" +
                            "            SELECT 1 FROM Receptor WHERE Cliente_idCliente = ?\n" +
                            "        ) THEN 'Receptor'\n" +
                            "        WHEN EXISTS (\n" +
                            "            SELECT 1 FROM Remitente WHERE Cliente_idCliente = ?\n" +
                            "        ) THEN 'Remitente'\n" +
                            "        ELSE 'Ninguno'\n" +
                            "    END AS Rol\n" +
                            "FROM Cliente\n" +
                            "WHERE idCliente = 1;");
    }

    public static PreparedStatement selectClienteById(Connection con) {
        return getStatement(con, "SELECT * FROM Cliente WHERE idCliente = ?");
    }
    
    public static PreparedStatement selectMaxIdCliente(Connection con) {
        return getStatement(con, "SELECT MAX(idCliente) AS max_id FROM Cliente;");
    }
    
    public static PreparedStatement selectMaxIdEnvio(Connection con) {
        return getStatement(con, "SELECT MAX(idEnvio) AS max_id FROM Envio;");
    }
    
    public static PreparedStatement selectMaxIdDato(Connection con) {
        return getStatement(con, "SELECT MAX(idDato) AS max_id FROM Dato");
        //return getStatement(con, "SELECT MAX(idDato) AS max_id FROM Dato WHERE Envio_idEnvio = ?;");
    }

    public static PreparedStatement insertCliente(Connection con) {
        return getStatement(con, "INSERT INTO Cliente (idCliente, Nombre, PW, Longitud, Latitud) VALUES (?, ?, ?, ?, ?)");
    }

    public static PreparedStatement insertTipo(Connection con, String tipo) {
        return getStatement(con, "INSERT INTO "+tipo+" (Cliente_idCliente) VALUES (?)");
    }
    
    public static PreparedStatement selectIdPorNombre(Connection con) {
        return getStatement(con, "SELECT idCliente FROM Cliente WHERE Nombre = ?");
    }
    
    public static PreparedStatement checkUsuarioPorNombreYContraseña(Connection con) {
        return getStatement(con, "SELECT COUNT(*) AS count FROM Cliente WHERE Nombre = ? AND PW = ?");
    }
    
    public static PreparedStatement checkClienteEnTabla(Connection con, String tabla) {
        return getStatement(con, "SELECT COUNT(*) AS count FROM " + tabla + " WHERE Cliente_idCliente = ?");
    }

    public static PreparedStatement selectPosiblesReceptores(Connection con) {
        return getStatement(con, "SELECT * FROM Receptor NATURAL JOIN Cliente");
    }

    public static PreparedStatement insertNuevoEnvio(Connection con) {
        return getStatement(con, "INSERT INTO Envio (idEnvio, Transportista_idTransportista, Paquete_idPaquete, Receptor_Cliente_idCliente, Remitente_Cliente_idCliente, Estado, Temperatura_min, Temperatura_max) VALUES (?, ?, ?, ?, ?, 'Envio', ?, ?)");
    }
    
    public static PreparedStatement selectEnviosCliente(Connection con) {
        return getStatement(con, "SELECT DISTINCT e.* FROM Envio e WHERE e.Remitente_Cliente_idCliente = ? OR e.Receptor_Cliente_idCliente = ?;");
    }
    
    public static PreparedStatement selectEnviosClienteActivo(Connection con) {
        return getStatement(con, "SELECT DISTINCT e.* FROM Envio e WHERE e.Remitente_Cliente_idCliente = ? OR e.Receptor_Cliente_idCliente = ? AND Estado = 'Envio';");
    }
    
    public static PreparedStatement selectEnviosClienteFinalizado(Connection con) {
        return getStatement(con, "SELECT DISTINCT e.* FROM Envio e WHERE e.Remitente_Cliente_idCliente = ? OR e.Receptor_Cliente_idCliente = ? AND Estado = 'Enviado';");
    }
    
    public static PreparedStatement selectEnviosClienteCancelado(Connection con) {
        return getStatement(con, "SELECT DISTINCT e.* FROM Envio e WHERE e.Remitente_Cliente_idCliente = ? OR e.Receptor_Cliente_idCliente = ? AND Estado = 'Cancelado';");
    }
    
    public static PreparedStatement selectTemperaturaMax(Connection con) {
        return getStatement(con, "SELECT Temperatura_max FROM Envio WHERE idEnvio = ?;");
    }
    
    public static PreparedStatement updateEstado(Connection con) {
        return getStatement(con, "UPDATE Envio SET Estado = ? WHERE idEnvio = ?;");
    }
    public static PreparedStatement updateVelocidadMedia(Connection con) {
        return getStatement(con, "UPDATE Envio SET Velocidad_media = ? WHERE idEnvio = ?;");
    }
    public static PreparedStatement updatePerdidaCadena(Connection con) {
        return getStatement(con, "UPDATE Envio SET Tiempo_perdida_cadena = ? WHERE idEnvio = ?;");
    }
    public static PreparedStatement updateTiempoEnvio(Connection con) {
        return getStatement(con, "UPDATE Envio SET Tiempo_envio = ? WHERE idEnvio = ?;");
    }


    public static PreparedStatement insertDato(Connection con) {
        return getStatement(con, "INSERT INTO Dato (idDato, Envio_idEnvio, Fecha) VALUES (?, ?, ?)");
    }

    public static PreparedStatement insertTemperaturaHumedad(Connection con) {
        return getStatement(con, "INSERT INTO TH (Dato_idDato, Temperatura, Humedad, Fecha) VALUES (?, ?, ?, ?)");
    }

    public static PreparedStatement insertUbicacion(Connection con) {
        return getStatement(con, "INSERT INTO Ubicacion (Dato_idDato, Longitud, Latitud, Velocidad, Velocidad_via, Fecha) VALUES (?, ?, ?, ?, ?, ?)");
    }

    public static PreparedStatement insertVentilador(Connection con) {
        return getStatement(con, "INSERT INTO Ventilador (Dato_idDato, Activo, Fecha) VALUES (?, ?, ?)");
    }

    public static PreparedStatement insertCambioEstado(Connection con) {
        return getStatement(con, "INSERT INTO Estado (Dato_idDato, Estado, Fecha) VALUES (?, ?, ?)");
    }
    
    public static PreparedStatement selectUbicaciones(Connection con) {
        return getStatement(con, "SELECT * FROM Ubicacion WHERE Dato_idDato IN (SELECT idDato FROM Dato WHERE Envio_idEnvio = ?)");
    }

    public static PreparedStatement selectVentiladores(Connection con) {
        return getStatement(con, "SELECT * FROM Ventilador WHERE Dato_idDato IN (SELECT idDato FROM Dato WHERE Envio_idEnvio = ?)");
    }

    public static PreparedStatement selectEstados(Connection con) {
        return getStatement(con, "SELECT * FROM Estado WHERE Dato_idDato IN (SELECT idDato FROM Dato WHERE Envio_idEnvio = ?)");
    }

    public static PreparedStatement selectTemperaturaHumedad(Connection con) {
        return getStatement(con, "SELECT * FROM TH WHERE Dato_idDato IN (SELECT idDato FROM Dato WHERE Envio_idEnvio = ?)");
    }
    
    public static PreparedStatement selectUltimaUbicacion(Connection con) {
    return getStatement(con, 
        "SELECT * FROM Ubicacion u JOIN Dato d ON u.Dato_idDato = d.idDato WHERE d.Envio_idEnvio = ? ORDER BY u.Fecha DESC LIMIT 1");
    }
    
    public static PreparedStatement selectMayorFecha(Connection con){
        return getStatement(con, "SELECT MAX(Fecha) AS MayorFecha FROM Dato WHERE Envio_idEnvio = ?");
    }
    
    public static PreparedStatement selectMenorFecha(Connection con){
        return getStatement(con, "SELECT MIN(Fecha) AS MenorFecha FROM Dato WHERE Envio_idEnvio = ?");
    }
    
    public static PreparedStatement selectMediaTiempoEnvio(Connection con){
        return getStatement(con, "SELECT AVG(Tiempo_envio) AS Media_Tiempo_Envio FROM Envio WHERE"+
                " Transportista_idTransportista = ? AND Receptor_Cliente_idCliente = ? AND Remitente_Cliente_idCliente = ?");
    }
    
    public static PreparedStatement selectMediaPerdida(Connection con){
        return getStatement(con, "SELECT AVG(Tiempo_perdida_cadena) AS Media_Perdida FROM Envio WHERE"+
                " Transportista_idTransportista = ?");
    }
    
    
}

