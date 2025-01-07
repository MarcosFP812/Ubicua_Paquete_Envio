package Logic;

import Clases.Cliente;
import Clases.TemperaturaHumedad;
import Clases.Estado;
import Clases.Ventilador;
import Clases.UbicacionEnvio;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import mqtt.MQTTBroker;
import mqtt.MQTTPublisher;
import mqtt.MQTTSuscriber;
import db.FachadaClienteBD;
import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * Inicializar hilos
 */
import java.util.ArrayList;
@WebListener
public class Projectinitializer implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @Override
    /**
     * ES: Metodo empleado para detectar la inicializacion del servidor	<br>
     * EN: Method used to detect server initialization
     *
     * @param sce <br>
     * ES: Evento de contexto creado durante el arranque del servidor	<br>
     * EN: Context event created during server launch
     */
    public void contextInitialized(ServletContextEvent sce) {
        Log.log.info("INICIANDO SERVER");
        
        Log.log.info("Pruebas acceso base de datos...");
        
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp fecha = Timestamp.valueOf(localDateTime);
        
        Controlador.registrarUbicacion(1, -3.270053,  40.562697, -1, fecha);
        /* 
        //Test registrar Cliente
        Controlador.registrarCliente("ubicua", "ubicua", 0, 0, "Receptor");
        
        //Test validar Cliente
        Log.log.info("Validando cliente: "+Controlador.validarCliente("ubicua", "ubicua"));
        
        //Test obtener receptores
        ArrayList<Cliente> receptores = Controlador.obtenerReceptores();
        for (int i = 0; i < receptores.size(); i++){
            Log.log.info(receptores.get(i));
        }
        
        //Test obtener transportistas
        ArrayList<Transportista> t = Controlador.obtenerTransportistas();
        for (int i = 0; i < t.size(); i++){
            Log.log.info(t.get(i));
        }
        
        //Test crear envío
        Controlador.crearEnvio(1, 1, 1, 2, 8, 2);
          
        //Test obtener envios de idCliente 3
        Log.log.info("Envíos de 3: ");
        ArrayList<Envio> e1 = Controlador.obtenerEnviosCliente(3);
        for (int i = 0; i < e1.size(); i++){
            Log.log.info(e1.get(i));
        }
        
        //Test obtener envios de idCliente 3 activos
        Log.log.info("Envíos activos de 3: ");
        ArrayList<Envio> e2 = Controlador.obtenerEnviosClienteActivo(3);
        for (int i = 0; i < e2.size(); i++){
            Log.log.info(e2.get(i));
        }
        
        //Test obtener envios de idCliente 3 finalizados
        Log.log.info("Envíos finalizados de 3: ");
        ArrayList<Envio> e3 = Controlador.obtenerEnviosClienteFinalizado(3);
        for (int i = 0; i < e3.size(); i++){
            Log.log.info(e3.get(i));
        }
        
        
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp fecha = Timestamp.valueOf(localDateTime);

        Log.log.info("Registrando temperatura");
        Controlador.registrarTH(0, 1, 25.5, 60.0, fecha);
        
        Log.log.info("Registrando ubicacion");
        Controlador.registrarUbicacion(7, -3.347869, 40.513348, -1, fecha);
        
        Log.log.info("Registrando ventilador");
        Controlador.registrarVentilador(1, true, fecha);

        Log.log.info("Registrando estado");
        Controlador.registrarEstado(1, "ABIERTO", fecha);

        Log.log.info("Obteniendo historial de ubicaciones");
        ArrayList<UbicacionEnvio> ubicaciones = Controlador.obtenerUbicaciones(1);
        for (UbicacionEnvio ubicacion : ubicaciones) {
            Log.log.info("Ubicacion: " + ubicacion);
        }

        Log.log.info("Obteniendo historial de ventiladores");
        ArrayList<Ventilador> ventiladores = Controlador.obtenerVentiladores(1);
        for (Ventilador ventilador : ventiladores) {
            Log.log.info("Ventilador: " + ventilador);
        }

        Log.log.info("Obteniendo historial de estados");
        ArrayList<Estado> estados = Controlador.obtenerEstados(1);
        for (Estado estado : estados) {
            Log.log.info("Estado: " + estado);
        }

        Log.log.info("Obteniendo historial de temperatura y humedad");
        ArrayList<TemperaturaHumedad> ths = Controlador.obtenerTemperaturasHumedades(1);
        for (TemperaturaHumedad th : ths) {
            Log.log.info("Temperatura y Humedad: " + th);
        }
        */
        
        String path = "/opt/tomcat/jsons/t1-guada/";
        
        Controlador.poblarEnvios(path);
        
        
        /*
        Log.log.info("\nObteniendo todos los clientes...");
        ArrayList<Cliente> allClientes = FachadaClienteBD.getAllClientes();
        for (Cliente cl : allClientes) {
            Log.log.info(cl);
        }
        
        
        // Crear un cliente para pruebas
        Cliente cliente = new Cliente();
        cliente.setNombre("ubicua");
        String PW = "ubicua";
        Ubicacion ub = new Ubicacion(-99.1332, 19.4326);
        cliente.setUbicacion(ub);
        cliente.setTipo("REMITENTE"); // Puede ser "RECEPTOR" o "REMITENTE"      
        Log.log.info("Cliente creado: "+cliente);
        
        Log.log.info("Insertando cliente...");
        
        int insertSuccess = FachadaClienteBD.insertCliente(cliente, PW);
        if (insertSuccess != -1) {
            Log.log.info("Cliente insertado exitosamente.");
        } else {
            Log.log.info("Error al insertar el cliente.");
        }

        Log.log.info("\nObteniendo clientes de tipo 'RECEPTOR'...");
        ArrayList<Cliente> receptores = FachadaClienteBD.getClientesByTipo("Receptor");
        for (Cliente receptor : receptores) {
            Log.log.info(receptor);
        }
        
        Log.log.info("\nObteniendo clientes de tipo 'REMITENTE'...");
        ArrayList<Cliente> remitentes = FachadaClienteBD.getClientesByTipo("Receptor");
        for (Cliente receptor : receptores) {
            Log.log.info(receptor);
        }
        
        Log.log.info("\nObteniendo cliente por ID...");
        if (!allClientes.isEmpty()) {
            int idToSearch = allClientes.get(1).getId(); // Usa el ID del primer cliente en la lista
            Cliente clienteById = FachadaClienteBD.getClienteById(idToSearch);
            if (clienteById != null) {
                Log.log.info("Cliente encontrado: " + clienteById);
            } else {
                Log.log.info("No se encontró el cliente con ID " + idToSearch);
            }
        }*/
        
        Log.log.info("");

        // Prueba subscribers
        
        Log.log.info("-->Suscribe Topics<--");
        MQTTSuscriber suscriber = new MQTTSuscriber();
        
        int idTrans = 1;
        int Receptor = 0;
        int Remitente = 0;
        int idPaquete = 0;
        int idEnvio = Controlador.crearEnvio(idTrans, idPaquete, Receptor, Remitente, Receptor, Receptor);
        
        MQTTPublisher.publish("Paquetes/p0/"+String.valueOf(idEnvio)+"/dht22/temperatura-humedad", "20-34");
        MQTTPublisher.publish("Paquetes/p0/"+String.valueOf(idEnvio)+"/gps/longitud-latitud-velocidad", "20-34-10");
        
        suscriber.suscribeTopic("Paquetes/p0/"+String.valueOf(idEnvio)+"/gps/+");
        
        /*Log.log.info("-->Suscribe Topics<--");
        MQTTBroker broker = new MQTTBroker();
        MQTTSuscriber suscriber = new MQTTSuscriber();
        suscriber.suscribeTopic(broker, "test");
        MQTTPublisher.publish(broker, "test", "Hello from Tomcat :)");*/
    }
}
