package Logic;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import mqtt.MQTTBroker;
import mqtt.MQTTPublisher;
import mqtt.MQTTSuscriber;
import db.FachadaClienteBD;
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
        
        /*Log.log.info("-->Suscribe Topics<--");
        MQTTBroker broker = new MQTTBroker();
        MQTTSuscriber suscriber = new MQTTSuscriber();
        suscriber.suscribeTopic(broker, "test");
        MQTTPublisher.publish(broker, "test", "Hello from Tomcat :)");*/
    }
}
