#include <ESP32PWM.h>
#include <ESP32Servo.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include <Keypad.h>
#include <LiquidCrystal_I2C.h>
#include <Wire.h>
#include <DHT.h>
#include <HardwareSerial.h>


/* ----- AJUSTES ----- */
// WiFi and MQTT settings
#define WIFI_SSID "Alpargatas Tenebrosas"
#define WIFI_PASSWORD "RayoMcQueenFeo"
const char* mqttServer = "192.168.63.38";
const int mqttPort = 1883;
const char* mqttUser = "user";
const char* mqttPassword = "pw";

// MQTT Topics
#define topicEstado "Paquetes/p0/estado"
#define topicID "Paquetes/p0/id"
#define topicTemp "Paquetes/p0/dht22/temperatura"
#define topicHumedad "Paquetes/p0/dht22/humedad"
#define topicGPS "Paquetes/p0/gps/ubicacion"
#define topicVentilador "Paquetes/p0/ventilador/estado"
#define topicPW "Paquetes/p0/PW"

//Keypad - Columns y Rows
#define KPC0PIN 13
#define KPC1PIN 14
#define KPC2PIN 15
#define KPR0PIN 16
#define KPR1PIN 17
#define KPR2PIN 18
#define KPR3PIN 19
//DHT
#define DHTTYPE  DHT22
#define DHTPIN    4
//GPS
#define RXPIN 5
#define TXPIN 12
//SERVO
#define SERVO_PIN 23
//VENTILADOR
#define VENTILADOR_PIN 27

/* ----- VARIABLES ----- */
WiFiClient espClient;
PubSubClient client(espClient);

// Información sobre el paquete
String id = ""; //ID del paquete
String estado = "CARGA"; // CARGA->CIERRE->ENVIO->APERTURA
bool ventilador = false;
String password = "";

//LCD 16x2
LiquidCrystal_I2C lcd(0x27, 16, 2);
//DHT22
DHT dht(DHTPIN, DHTTYPE, 22); 
//KeyPad
const byte ROWS = 4; 
const byte COLS = 3; 
byte rowPins[ROWS] = {KPR0PIN, KPR1PIN, KPR2PIN, KPR3PIN}; 
byte colPins[COLS] = {KPC0PIN, KPC1PIN, KPC2PIN}; 
char hexaKeys[ROWS][COLS] = {
  {'1', '2', '3'},
  {'4', '5', '6'},
  {'7', '8', '9'},
  {'*', '0', '#'}
};
Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS); 
//GPS
HardwareSerial GPS(2); 
//Servo
Servo servo;

/* ----- FUNCIONES DE CONFIGURACIÓN ----- */
/* ----- FUNCIONES DE CONFIGURACIÓN ----- */
void initWiFi() {
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("Conectado al WiFi.");
  Serial.print("Dirección IP: ");
  Serial.println(WiFi.localIP());
}

void ConnectMqtt() {
  
  while (!client.connected()) {
    if (client.connect("ESP32Client", mqttUser, mqttPassword)) {
      Serial.println("Conectado al broker MQTT.");
      client.subscribe(topicEstado);
      client.subscribe(topicPW);
      client.subscribe(topicID);
    } else {
      Serial.print("Error de conexión MQTT: ");
      Serial.print(client.state());
      Serial.println();
      delay(2000);
    }
  }
}

/* ----- CALLBACK PARA RECIBIR MENSAJES ----- */
void callback(char* topic, byte* payload, unsigned int length) {
  String message;
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }
  Serial.print("Mensaje recibido en topic ");
  Serial.print(topic);
  Serial.print(": ");
  Serial.println(message);

  // Actualizar estado en función del topic
  if (String(topic) == topicEstado) {
    Serial.println("Estado recibido");
    estado = message;
  } if (String(topic) == topicVentilador) {
    ventilador = (message == "1");
  } if (String(topic) == topicID) {
    Serial.println("ID recibido");
    id = message;
  } if (String(topic) == topicPW) {
    password = message;
  }
}

void handleMQTT(){
  if (!client.connected())
    {
        ConnectMqtt();
    }
    client.loop();
}

/* ----- SETUP ----- */
void setup() {
  Serial.begin(115200);  // Inicia la comunicación serial a 115200 baudios
  lcd.begin();  // Inicializa el LCD
  lcd.backlight();  // Enciende la luz de fondo del LCD
  
  // WiFi y MQTT
  lcd.setCursor(0,0);
  lcd.print("Init WIFI");  // Muestra mensaje de inicialización de Wi-Fi
  initWiFi();  // Inicializa la conexión Wi-Fi
  lcd.setCursor(0,1);
  lcd.print("Init MQTT");  // Muestra mensaje de inicialización de MQTT
  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);
  ConnectMqtt();
  lcd.clear();  // Limpia la pantalla LCD

  // Servomotor
  servo.attach(SERVO_PIN);  // Conecta el servomotor al pin especificado
  servo.write(0);  // Asegura que la caja esté cerrada al inicio
  
  // DHT22
  dht.begin();  // Inicializa el sensor DHT22

  // GPS
  GPS.begin(9600, SERIAL_8N1, TXPIN, RXPIN);  // Inicializa el GPS a 9600 baudios

  // VENTILADOR
  pinMode(VENTILADOR_PIN, OUTPUT);  // Configura el pin del ventilador como salida
  digitalWrite(VENTILADOR_PIN, LOW);  // Apaga el ventilador al inicio
}

/* ----- LOOP ----- */
void loop() {
  if (!client.connected()) {  // Si no está conectado a MQTT, intenta reconectar
    ConnectMqtt();
  }
  client.loop();  // Mantiene la conexión MQTT activa

  enviarEstado();  // Envía el estado actual del paquete

  cargarPaquete();  // Espera a que el paquete sea cargado
  while(id == ""){
    handleMQTT();
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("Esperando ID");
    delay(2000);
  }  // Espera a que el ID del paquete se defina
  cerrarPaquete();  // Cierra el paquete (cerrando la caja)

  enviandoPaquete();  // Empieza el proceso de envío del paquete

  pedirPW(password);  // Pide la contraseña para abrir el paquete
  abrirPaquete();  // Abre el paquete si la contraseña es correcta
}

/* ----- FUNCIONES DE MQTT ----- */
void enviarTH(float h, float t) {
  client.publish(topicTemp, String(t).c_str());  // Publica la temperatura en el tema correspondiente
  client.publish(topicHumedad, String(h).c_str());  // Publica la humedad en el tema correspondiente
}

void enviarGPS(String ubicacion) {
  Serial.println("Enviando Ubicación");
  Serial.println(ubicacion.c_str());

  client.publish(topicGPS, ubicacion.c_str());  // Publica la ubicación GPS en el tema correspondiente
}

void enviarEstado() {
  client.publish(topicEstado, estado.c_str());  // Publica el estado del paquete
}

/* ----- FUNCIONES DE ESTADO ----- */
void cargarPaquete() {
  while (estado == "CARGA") {  // Mientras el paquete esté en estado "CARGA"
    lcd.clear();  // Limpia la pantalla LCD
    lcd.setCursor(0, 0);
    lcd.print("Esperando Carga");  // Muestra mensaje de espera
    delay(2000);  // Espera 2 segundos
    handleMQTT();
  }
}

void cerrarPaquete() {
  lcd.clear();  // Limpia la pantalla LCD
  lcd.setCursor(0, 0);
  lcd.print("Cerrando Paquete...");  // Muestra mensaje de cierre
  servo.write(90);  // Mueve el servomotor a la posición de cierre
  delay(1000);  // Espera 2 segundos

  lcd.setCursor(0, 1);
  lcd.print("Paquete Cerrado");  // Muestra mensaje de que el paquete está cerrado
  delay(2000);  // Espera 1 segundo

  estado = "ENVIO";  // Cambia el estado del paquete a "CERRADO"
  enviarEstado();  // Envía el nuevo estado (CERRADO) al servidor MQTT
}

void enviandoPaquete() {
 
  while (estado == "ENVIO") {  // Mientras el estado sea "ENVIO", se realiza el envío
    float h = dht.readHumidity();  // Lee la humedad desde el sensor DHT22
    float t = dht.readTemperature();  // Lee la temperatura desde el sensor DHT22
    String ubicacion = "";  // Variable para almacenar la ubicación GPS

    while (GPS.available() > 0) {  // Si hay datos disponibles del GPS
      char c = GPS.read();  // Lee un carácter del GPS
      ubicacion += c;  // Añade el carácter a la ubicación
    }

    lcd.clear();  // Limpia la pantalla LCD
    lcd.setCursor(0, 0);
    lcd.print("ID:");  // Muestra el ID del paquete
    lcd.print(id);
    lcd.setCursor(0, 1);
    lcd.print("H: ");
    lcd.print(h);  // Muestra la humedad leída
    lcd.print(" T: ");
    lcd.print(t);  // Muestra la temperatura leída

    enviarTH(h, t);  // Envía la temperatura y humedad al servidor MQTT
    if (ubicacion != ""){
      enviarGPS(ubicacion);  // Envía la ubicación GPS al servidor MQTT
    }
  
    // Control del ventilador
    if (ventilador) {  // Si el ventilador está activado
      digitalWrite(VENTILADOR_PIN, HIGH);  // Enciende el ventilador
    } else {  // Si el ventilador está desactivado
      digitalWrite(VENTILADOR_PIN, LOW);  // Apaga el ventilador
    }  
    delay(500);  // Espera 1 segundo antes de enviar la siguiente lectura
    handleMQTT();
  }
}

void pedirPW(const String& password) {
  while(password == ""){
    handleMQTT();
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("Esperando PW");
  }  // Espera hasta que se reciba una contraseña válida

  String input = "";  // Variable para almacenar la contraseña ingresada
  while (input != password) {  // Mientras la contraseña ingresada no coincida con la almacenada
    lcd.clear();  // Limpia la pantalla LCD
    lcd.setCursor(0, 0);
    lcd.print("Password: ");  // Muestra el mensaje "Password:"
    lcd.setCursor(0, 1);
    while (input.length() < password.length()) {  // Lee cada tecla ingresada en el teclado
      char key = customKeypad.getKey();  // Obtiene la tecla presionada
      if (key) {
        lcd.print(key);  // Muestra la tecla presionada en el LCD
        input += key;  // Añade la tecla a la contraseña ingresada
      }
    }
    lcd.clear();  // Limpia la pantalla LCD
    lcd.setCursor(0, 0);
    if (input == password) {  // Si la contraseña es correcta
      lcd.print("PW ACEPTADA");  // Muestra el mensaje "PW ACEPTADA"
    } else {  // Si la contraseña es incorrecta
      lcd.print("PW RECHAZADA");  // Muestra el mensaje "PW RECHAZADA"
      input = "";  // Reinicia la contraseña ingresada para intentar de nuevo
    }
    delay(2000);  // Espera 2 segundos antes de permitir otro intento
  }
}

void abrirPaquete() {
  lcd.clear();  // Limpia la pantalla LCD
  lcd.setCursor(0, 0);
  lcd.print("Abriendo paquete");  // Muestra el mensaje "Abriendo paquete"
  servo.write(0);  // Abre el servomotor (abriendo la caja)
  delay(1000);  // Espera 1 segundo

  lcd.setCursor(0, 1);
  lcd.print("Paquete Abierto");  // Muestra el mensaje "Paquete Abierto"
  delay(2000);  // Espera 2 segundos

  estado = "CARGA";  // Cambia el estado del paquete a "CARGA" (listo para cargar otro paquete)
  enviarEstado();  // Envía el nuevo estado al servidor MQTT
}