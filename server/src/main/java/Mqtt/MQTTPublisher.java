package mqtt;

import Logic.Log;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPublisher {

    /**
     *
     * @param broker
     * @param topic
     * @param content
     */
    public static void publish(String topic, String content) {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(MQTTBroker.getBroker(), MQTTBroker.getClientId(), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(MQTTBroker.getUsername());
            connOpts.setPassword(MQTTBroker.getPassword().toCharArray());
            connOpts.setCleanSession(true);
            Log.log.info("Connecting to broker: " + MQTTBroker.getBroker() + " con "+ MQTTBroker.getUsername()+" "+MQTTBroker.getPassword());
            sampleClient.connect(connOpts);
            Log.log.info("Connected");
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(MQTTBroker.getQos());
            sampleClient.publish(topic, message);
            Log.log.info("Message published");
            sampleClient.disconnect();
            Log.log.info("Disconnected");

        } catch (MqttException me) {
            Log.log.error("Error on publishing value: {}", me);
        } catch (Exception e) {
            Log.log.error("Error on publishing value: {}", e);
        }
    }
}
