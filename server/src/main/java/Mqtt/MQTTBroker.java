package mqtt;

public class MQTTBroker {

    private static int qos = 2;
    private static final String broker = "tcp://localhost:1883";
    private static final String clientId = "ubicua";
    private static final String username = "user";
    private static final String password = "pw";
    
    public MQTTBroker() {
    }

    public static int getQos() {
        return qos;
    }

    public static String getBroker() {
        return broker;
    }

    public static String getClientId() {
        return clientId;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
    
}
