/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

import Clases.Cliente;
import Clases.TemperaturaHumedad;
import Clases.Estado;
import Clases.Ubicacion;
import Clases.Ventilador;
import Clases.Envio;
import Clases.UbicacionEnvio;
import db.FachadaClienteBD;
import db.FachadaEnvioBD;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author socra
 */
public class Controlador {  
    
    /* Validar cliente
    * 1. Comprobar que dicho nombre no existe en la base de datos
    */
    public static int validarCliente(String nombre, String pw){
        int id = FachadaClienteBD.validarUsuario(nombre, pw);
        
        return id;
    }
    
    /* Registrar un cliente
    * 1. Comprobar que dicho nombre no existe en la base de datos
    * 2. Crear nuevo cliente
    * 3. insertarlo a la BD
    */
    public static int registrarCliente(String nombre, String pw, double longitud, double latitud, String tipo){
        boolean existe;
        int idNuevo = -1;
        int id = FachadaClienteBD.getIdByNombre(nombre);
        
        if (id == -1){
            Cliente cliente = new Cliente(nombre, new Ubicacion(longitud, latitud), tipo);
            idNuevo = FachadaClienteBD.insertCliente(cliente, pw);
        }
        
        return idNuevo;
    }
    
    /* Sacar todos los posibles receptores
    * 1. consultar todos los clientes de tipo receptor
    */
    public static ArrayList<Cliente> obtenerReceptores(){
        ArrayList<Cliente> clientes = FachadaClienteBD.getClientesByTipo("Receptor");
        
        return clientes;
    }
    
    /* sacar todos los transportistas
    * 1. consultar todos los transportistas
    */
    public static ArrayList<Transportista> obtenerTransportistas(){
        ArrayList<Transportista> t = FachadaEnvioBD.getTransportistas();
        
        return t;
    }
    
    /* Crear un nuevo envío
    * 1. crear envío
    */
    public static int crearEnvio(int idTransportista, int idPaquete, int idReceptor, int idRemitente, double temperatura_max, double temperatura_min){
        int id = FachadaEnvioBD.crearNuevoEnvio(idTransportista, idPaquete, idReceptor, idRemitente, temperatura_max, temperatura_min);
        
        return id;
    }
    
    /* Sacar todos los envíos realizados por un cliente
    * 1. Dado un id llamar a la fachada para obtener los envíos
    */
    public static ArrayList<Envio> obtenerEnviosCliente(int idCliente){
        ArrayList<Envio> e = FachadaEnvioBD.getEnviosPorCliente(idCliente);
        
        return e;
    }
    public static ArrayList<Envio> obtenerEnviosClienteActivo(int idCliente){
        ArrayList<Envio> e = FachadaEnvioBD.getEnviosPorClienteActivo(idCliente);
        
        return e;
    }
    public static ArrayList<Envio> obtenerEnviosClienteFinalizado(int idCliente){
        ArrayList<Envio> e = FachadaEnvioBD.getEnviosPorClienteFinalizado(idCliente);
        
        return e;
    }
    public static ArrayList<Envio> obtenerEnviosClienteCancelado(int idCliente){
        ArrayList<Envio> e = FachadaEnvioBD.getEnviosPorClienteCancelado(idCliente);
        
        return e;
    }
    
    /* Cambiar estado de un envío
    */
    public static boolean cambiarEstadoEnvio(int idEnvio, String estado){
        boolean e = FachadaEnvioBD.actualizarEstado(idEnvio, estado);
        
        //Publish estado
        
        return e;
    }
    
    /* Registrar nueva temperatura y humedad
    * 
    */
    public static boolean registrarTH(int idEnvio, double t, double h, Timestamp fecha){
       boolean valido = FachadaEnvioBD.registrarTemperaturaHumedad(idEnvio, fecha, t, h);
        //Lógica de la temperatura
        
        return valido;
    }
    
    /* Registrar localización
    * 
    */
    public static boolean registrarUbicacion(int idEnvio, double longitud, double latitud, double velocidad, Timestamp fecha){
        
        //Comprobar que la ubicación el válida con la anterior
        
        //Calcular la velocidad si lo necesita
        
        //Comprobar la velocidad en la vía
        double velocidadVia = -1;
        
        boolean valido = FachadaEnvioBD.registrarUbicacion(idEnvio, fecha, longitud, latitud, velocidad, velocidadVia);
        
        return valido;
        
    }
    
    /* Registrar ventilador
    * 
    */
    public static boolean registrarVentilador(int idEnvio, boolean activo, Timestamp fecha){
       boolean valido = FachadaEnvioBD.registrarVentilador(idEnvio, fecha, activo);
       
        //Publish del estado del ventilador
        
        return valido;
    }
    
    /* Registrar cambio de estado
    * 
    */
    public static boolean registrarEstado(int idEnvio, String estado, Timestamp fecha){
       boolean valido = FachadaEnvioBD.registrarCambioEstado(idEnvio, fecha, estado);
       
        //Publish estado 
        
        return valido;
    }
    
    /* Obtener historial de ubicaciones
    */
    public static ArrayList<UbicacionEnvio> obtenerUbicaciones(int idEnvio){
        return FachadaEnvioBD.getUbicaciones(idEnvio);
    }

    /* Obtener historial de ventiladores
    */
    public static ArrayList<Ventilador> obtenerVentiladores(int idEnvio){
        return FachadaEnvioBD.getVentiladores(idEnvio);
    }

    /* Obtener historial de estados
    */
    public static ArrayList<Estado> obtenerEstados(int idEnvio){
        return FachadaEnvioBD.getEstados(idEnvio);
    }

    /* Obtener historial de temperatura y humedad
    */
    public static ArrayList<TemperaturaHumedad> obtenerTemperaturasHumedades(int idEnvio){
        return FachadaEnvioBD.getTemperaturaHumedad(idEnvio);
    }
    
    public static String generarJson(ArrayList<?> lista) {
        // Iniciamos el JSON con un array []
        StringBuilder json = new StringBuilder("[");
        
        // Iteramos sobre la lista de objetos
        for (int i = 0; i < lista.size(); i++) {
            // Obtenemos el toString del objeto que debe ser en formato JSON
            String objetoJson = lista.get(i).toString();
            
            // Añadimos el objeto al JSON resultante
            json.append(objetoJson);
            
            // Si no es el último objeto, agregamos una coma
            if (i < lista.size() - 1) {
                json.append(", ");
            }
        }
        
        // Cerramos el JSON con un corchete ]
        json.append("]");
        
        return json.toString();
    }

    
    /* Crear pin
    */
    public static String generarPin(int idEnvio){
        Random random = new Random();
        StringBuilder pinBuilder = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int digit = random.nextInt(10); // Generar un dígito aleatorio (0-9)
            pinBuilder.append(digit);
        }

        String pin = pinBuilder.toString();
        
        //Publish del pin
        
        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);
        
        Controlador.registrarEstado(idEnvio, "APERTURA", timestamp);
        
        return pin;
    }
    
    
}
