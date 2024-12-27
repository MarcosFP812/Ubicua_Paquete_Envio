# Ubicua_Paquete_Envio

[Enlace memoria en Word](https://docs.google.com/document/d/1EwQTDTlvPVAPi7CF_E9ypvhFXsR4SJYN-_lQEtFpixw/edit?usp=sharing)

## Organización

[Diagrama de la arquitectura del servidor](\images\Diagrama-ubicua.drawio.png)

En este diagrama de clases se explica aproximadamente la arquitectura del programa del server. Esto da las siguientes tareas por hacer:
### MQTT
Capa destinada a la publicación y suscripción de topic:
- [ ] Crear el Broker para que se conecte
- [ ] Unirlo con el arduino
Subscriber:
- [ ] Temperatura y humedad
- [ ] GPS
- [ ] Estado
Publisher
- [ ] Paquete
- [ ] Estado
- [ ] ID
- [ ] PW

### Android
Crear las aplicaciones Android que muestren datos y hagan peticiones al servidor
- [ ] Receptor
- [ ] Remitente
- [ ] Transportistas (Se puede hacer como pagina web)

### Servlets
Servlets que recojan las peticiones de android
- [ ] Login
- [ ] Register
- [ ] Paquetes (Devolver todos los paquetes en los que está un cliente)
- [ ] Historia (Devolver todos los datos de un envío)
- [ ] NuevoEnvio (iniciar un nuevo envío)
- [ ] Rastreo (Devolver en tiempo real las actualizaciones de un envío)
- [ ] PIN (Solicitar un pin para abrir la caja)
- [ ] Transportistas (Devolver información de los transportistas)

### FachadaBD
Peticiones a la base de datos sobre los clientes:
- [ ] Crear o eliminar cliente
- [ ] Devolver todos los clientes de un tipo
- [ ] Buscar cliente según el tipo
- [ ] Validar un cliente
Peticiones a la base de datos sobre los envíos:
- [ ] Pedir los transportistas
- [ ] Crear un nuevo envío
- [ ] Listar todos los envíos de un cliente
- [ ] Pedir la información entera de un envío
- [ ] Pedir información desde una fecha en concreto
- [ ] Introducir nuevos datos dentro de un envío

### Lógica

## Directorios

### android
Contiene el proyecto en netbeans de la aplicación android

### server
Contiene el proyecto en netbeans que ejecutará el servidor

### DB 
Contiene los scripts de carga de tablas y datos en la base de datos

### arduino
Código del esp de arduino

## RoadMap

- [X] BD: cargar mariadb en la maquina virtual
- [X] BD: Diagrama de tablas y crearlas en la base de datos
- [ ] BD: Poblar la BD (datos sintéticos)
- [X] Servidor: conexión con base de datos y fachada
- [ ] Servidor: Lógica interna, procesamiento de datos y rastreo del paquete
- [ ] Servidor: Servlets
- [ ] Android: Interfaz del receptor
- [ ] Android: Interfaz del remitente
- [ ] Android: Interfaz de control de transportistas
