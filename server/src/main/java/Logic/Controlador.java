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
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import mqtt.MQTTPublisher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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
        MQTTPublisher.publish("Paquetes/p"+String.valueOf(idPaquete)+"/"+String.valueOf(id), String.valueOf(id));
        MQTTPublisher.publish("Paquetes/p"+String.valueOf(idPaquete)+"/"+String.valueOf(id)+"/estado","CARGA");
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
                
        return e;
    }
    
    /* Registrar nueva temperatura y humedad
    * 
    */
    public static boolean registrarTH(int idPaquete, int idEnvio, double t, double h, Timestamp fecha){
       boolean valido = FachadaEnvioBD.registrarTemperaturaHumedad(idEnvio, fecha, t, h);
        //Lógica de la temperatura
        MQTTPublisher.publish("Paquetes/p"+String.valueOf(idPaquete)+"/"+String.valueOf(idEnvio)+"/temperatura", String.valueOf(t));
        MQTTPublisher.publish("Paquetes/p"+String.valueOf(idPaquete)+"/"+String.valueOf(idEnvio)+"/temperatura", String.valueOf(h));
        return valido;
    }
    
    /* Registrar localización
    * 
    */
    public static boolean registrarUbicacion(int idEnvio, double longitud, double latitud, double velocidad, Timestamp fecha){
        
        UbicacionEnvio ubAnterior = FachadaEnvioBD.getUltimaUbicacionPorEnvio(idEnvio);

        if (ubAnterior != null) {
            // En caso de que la velocidad sea -1, calcular la velocidad actual comparando con la ubicación anterior
            if (velocidad == -1) {
                // Distancia entre la ubicación anterior y la nueva (en kilómetros)
                double distancia = Logic.calcularDistancia(ubAnterior.getLongitud(), ubAnterior.getLatitud(), longitud, latitud);
                Log.log.info("Distancia: " + distancia);

                // Tiempo entre la ubicación anterior y la nueva (en horas)
                double tiempoHoras = (fecha.getTime() - ubAnterior.getFecha().getTime()) / 3600000.0;
                Log.log.info("Tiempo: " + tiempoHoras);

                // Calcular velocidad (en km/h)
                if (tiempoHoras > 0) {
                    velocidad = distancia / tiempoHoras;
                } else {
                    velocidad = 0; // En caso de tiempo 0 o negativo, asignar velocidad 0
                }
            }

            // Comprobar que la ubicación no ha pegado un salto ilógico (velocidad exagerada)
            if (velocidad > 250) { // Por ejemplo, velocidad mayor a 300 km/h es sospechosa
                return false; // Rechazar el registro por salto ilógico
            }
        }

        // Comprobar la velocidad en la vía según las coordenadas usando algún API
        // Aquí deberíamos integrar un servicio externo que permita consultar la velocidad permitida en la vía
        // Ejemplo de simulación:
        Log.log.info("Intentado sacar la velocidad de la via...");
        double velocidadVia = Logic.obtenerVelocidadVia(longitud, latitud); // Método que interactúa con un API externo
        Log.log.info("velocidad de la via: "+velocidadVia);
        // Validar si la velocidad registrada excede en un margen razonable la velocidad de la vía
        if (velocidadVia > 0 && velocidad > velocidadVia * 2) { // Margen del 20% por ejemplo
            return false; // Rechazar si la velocidad supera este margen
        }

        // Registrar la nueva ubicación en la base de datos
        boolean valido = FachadaEnvioBD.registrarUbicacion(idEnvio, fecha, longitud, latitud, velocidad, velocidadVia);

        return valido;
    }
    
    /* Registrar ventilador
    * 
    */
    public static boolean registrarVentilador(int idPaquete, int idEnvio, boolean activo, Timestamp fecha){
       boolean valido = FachadaEnvioBD.registrarVentilador(idEnvio, fecha, activo);
       
        //Publish del estado del ventilador
        MQTTPublisher.publish("Paquetes/p"+String.valueOf(idPaquete)+"/"+String.valueOf(idEnvio)+"/ventilador", String.valueOf(activo));
        
        return valido;
    }
    
    /* Registrar cambio de estado
    * 
    */
    public static boolean registrarEstado(int idPaquete, int idEnvio, String estado, Timestamp fecha){
       boolean valido = FachadaEnvioBD.registrarCambioEstado(idEnvio, fecha, estado);
       
        //Publish estado 
        MQTTPublisher.publish("Paquetes/p"+String.valueOf(idPaquete)+"/"+String.valueOf(idEnvio)+"/estado", estado);
        
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
    
    public static String obtenerTipo(int idCliente){
        return FachadaClienteBD.getTipo(idCliente);
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
        //MQTTPublisher.publish("Paquetes/p"+String.valueOf(idPaquete)+"/"+String.valueOf(idEnvio)+"/pw", pin);
        int idPaquete = 0;
        
        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);
        
        Controlador.registrarEstado(idPaquete, idEnvio, "APERTURA", timestamp);
        
        return pin;
    }
    
    public static boolean cancelarEnvio(int idEnvio){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);
        
        return Controlador.cambiarEstadoEnvio(idEnvio, "Cancelado");
    }
    
    
    
    public static void finalizarEnvio(int idEnvio, int idPaquete){
        
        
        //Calculos
        double tiempoEnvio = Logic.calcularTiempoEnvio(idEnvio);
        double tiempoCadenaFrio = Logic.contarMinutosPorEncimaDelUmbral(idEnvio);
        
        FachadaEnvioBD.actualizarTiempoEnvio(idEnvio, tiempoEnvio);
        FachadaEnvioBD.actualizarPerdidaCadena(idEnvio, tiempoCadenaFrio);
        
        Controlador.cambiarEstadoEnvio(idEnvio, "Enviado");
        
    }

    public static void poblarEnvios(String ruta) {
        boolean explotado = false;
        try {
            // Leer el archivo envio.json
            String content = new String(Files.readAllBytes(Paths.get(ruta+"envio.json")));
            JSONObject envio = new JSONObject(content);

            // Iterar sobre los envíos y registrar cada uno en la base de datos
            for (int i = 0; i < 30; i++) {
                //JSONObject envio = enviosArray.getJSONObject(i);
                //if (explotado) break;
                int idTransportista = envio.getInt("Transportista_idTransportista");
                int idPaquete = envio.getInt("Paquete_idPaquete");
                int idReceptor = envio.getInt("Receptor_Cliente_idCliente");
                int idRemitente = envio.getInt("Remitente_Cliente_idCliente");
                double temperaturaMax = envio.getDouble("Temperatura_max");
                double temperaturaMin = envio.getDouble("Temperatura_min");

                // Crear el envío en la base de datos
                int idEnvio = Controlador.crearEnvio(idTransportista, idPaquete, idReceptor, idRemitente, temperaturaMax, temperaturaMin);
                if (explotado) break;
                // Registrar datos asociados al envío
                registrarDatosAsociados(idEnvio, idPaquete, i, ruta);
            }

        } catch (Exception e) {
            explotado = true;
            Log.log.info(e);
        }
    }

    public static void registrarDatosAsociados(int idEnvio, int idPaquete, int indice, String ruta) {
        boolean explotado = false;
        try {
            // Leer y procesar estados
            FileReader estadosReader = new FileReader(ruta+"estados.json");
            JSONObject estadosJson = new JSONObject(new JSONTokener(estadosReader));
            JSONArray estadosArray = estadosJson.getJSONArray(String.valueOf(indice));
            for (int j = 0; j < estadosArray.length(); j++) {
                if (explotado) break;
                JSONObject estado = estadosArray.getJSONObject(j);
                Timestamp fecha = Timestamp.valueOf(estado.getString("Fecha"));
                String estadoStr = estado.getString("Estado");
                Controlador.registrarEstado(idPaquete, idEnvio, estadoStr, fecha);
            }

            // Leer y procesar temperaturas y humedad
            FileReader temperaturasReader = new FileReader(ruta+"temp.json");
            JSONObject temperaturasJson = new JSONObject(new JSONTokener(temperaturasReader));
            JSONArray temperaturasArray = temperaturasJson.getJSONArray(String.valueOf(indice));
            for (int j = 0; j < temperaturasArray.length(); j++) {
                if (explotado) break;
                JSONObject temp = temperaturasArray.getJSONObject(j);
                Timestamp fecha = Timestamp.valueOf(temp.getString("Fecha"));
                double temperatura = temp.getDouble("Temperatura");
                double humedad = temp.getDouble("Humedad");
                Controlador.registrarTH(idPaquete, idEnvio, temperatura, humedad, fecha);
            }

            // Leer y procesar ventiladores
            FileReader ventiladorReader = new FileReader(ruta+"ventilador.json");
            JSONObject ventiladoresJson = new JSONObject(new JSONTokener(ventiladorReader));
            JSONArray ventiladoresArray = ventiladoresJson.getJSONArray(String.valueOf(indice));
            for (int j = 0; j < ventiladoresArray.length(); j++) {
                if (explotado) break;
                JSONObject ventilador = ventiladoresArray.getJSONObject(j);
                Timestamp fecha = Timestamp.valueOf(ventilador.getString("Fecha"));
                boolean activo = ventilador.getBoolean("Activo");
                Controlador.registrarVentilador(idPaquete, idEnvio, activo, fecha);
            }

            // Leer y procesar ubicaciones
            FileReader ubicacionesReader = new FileReader(ruta+"ubi.json");
            JSONObject ubicacionesJson = new JSONObject(new JSONTokener(ubicacionesReader));
            JSONArray ubicacionesArray = ubicacionesJson.getJSONArray(String.valueOf(indice));
            for (int j = 0; j < ubicacionesArray.length(); j++) {
                if (explotado) break;
                JSONObject ubicacion = ubicacionesArray.getJSONObject(j);
                double longitud = ubicacion.getDouble("Longitud");
                double latitud = ubicacion.getDouble("Latitud");
                double velocidad = ubicacion.getDouble("Velocidad");
                Timestamp fecha = Timestamp.valueOf(ubicacion.getString("Fecha"));
                Controlador.registrarUbicacion(idEnvio, longitud, latitud, velocidad, fecha);
            }

        } catch (Exception e) {
            explotado = true;
            Log.log.info(e);
        }
    }
    
    
    public static double obtenerTiempoMedioTransportista(int idTransportista, int idReceptor, int idRemitente){
        double tiempo_medio = FachadaEnvioBD.getMediaTiempoEnvio(idTransportista, idReceptor, idRemitente);
        return tiempo_medio;
    }
    
     public static double obtenerPerdidaMedia(int idTransportista){
        double tiempo_medio = FachadaEnvioBD.getMediaPerdida(idTransportista);
        return tiempo_medio;
    }
    
    
    
}
