package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import Logic.Controlador;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import db.Topics;
import Logic.Log;
import java.util.HashMap;
import java.util.Map;

public class MQTTSuscriber implements MqttCallback {
    
    private final Map<Integer, Double> temperaturaCache = new HashMap<>();
    private final Map<Integer, Double> humedadCache = new HashMap<>();

    public void suscribeTopic(String topic) {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(MQTTBroker.getBroker(), MQTTBroker.getClientId(), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(MQTTBroker.getUsername());
            connOpts.setPassword(MQTTBroker.getPassword().toCharArray());
            connOpts.setCleanSession(true);
            
            Log.logmqtt.info("Mqtt Conectando al broker: " + MQTTBroker.getBroker());
            sampleClient.connect(connOpts);
            Log.logmqtt.info("Mqtt: Conectado");
            
            sampleClient.setCallback(this);
            sampleClient.subscribe(topic);
            Log.logmqtt.info("Suscrito al topic {}", topic);

        } catch (MqttException me) {
            Log.logmqtt.error("Error al intentar suscribirse topic: {}", me);
        } catch (Exception e) {
            Log.logmqtt.error("Error al intentar suscribirse topic: {}", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.logmqtt.warn("Conexion perdida: ", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.logmqtt.info("Mensaje recibido en topic {}: {}", topic, message.toString());
        //Topics newTopic = new Topics();
        //newTopic.setValue(message.toString());
        try{
            String[] topicParts = topic.split("/");
            if (topicParts.length < 4) return;
            
            int idPaquete = Integer.parseInt(topicParts[1]);
            int idEnvio = Integer.parseInt(topicParts[2]);
            String subTopic = topicParts[3];
            Timestamp now = new Timestamp(System.currentTimeMillis());

            switch (subTopic) {
                case "estado":
                    Controlador.registrarEstado(idPaquete, idEnvio, message.toString(), now);
                    break;
                case "temperatura":
                    double temperatura = Double.parseDouble(message.toString());
                    temperaturaCache.put(idEnvio, temperatura);

                    // Verifica si ya hay un valor de humedad asociado
                    if (humedadCache.containsKey(idEnvio)) {
                        double humedad = humedadCache.remove(idEnvio);
                        Controlador.registrarTH(idPaquete, idEnvio, temperatura, humedad, now);
                        temperaturaCache.remove(idEnvio);
                    }
                    break;
                case "humedad":
                    double humedad = Double.parseDouble(message.toString());
                    humedadCache.put(idEnvio, humedad);

                    // Verifica si ya hay un valor de temperatura asociado
                    if (temperaturaCache.containsKey(idEnvio)) {
                        temperatura = temperaturaCache.remove(idEnvio);
                        Controlador.registrarTH(idPaquete, idEnvio, temperatura, humedad, now);
                        humedadCache.remove(idEnvio);
                    }
                    break;
                default:
                    Log.logmqtt.warn("Subtopico no reconocido: " + subTopic);
                    break;
            }
        }catch (Exception e){
            Log.logmqtt.error("Error al procesar el mensaje", e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.logmqtt.info("Mensaje enviado correctamente: {} ", token.isComplete());
    }
}
