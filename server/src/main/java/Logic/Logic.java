package Logic;

import Clases.TemperaturaHumedad;
import db.FachadaEnvioBD;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONObject;

public class Logic 
{
    public static void bucle(){
        Log.log.info("Comienza el bucle");
    }

    // Método auxiliar para calcular distancia entre dos puntos geográficos
    public static double calcularDistancia(double lon1, double lat1, double lon2, double lat2) {
        final int R = 6371; // Radio de la Tierra en kilómetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distancia en kilómetros
    }

    public static double obtenerVelocidadVia(double longitud, double latitud) {
        Log.log.info("Iniciando consulta de límite de velocidad para las coordenadas: lon={}, lat={}.", longitud, latitud);

        String query = String.format(
            """
            [out:json];
            way
            (around:1000,%f,%f)["highway"];
            out tags;
            """, latitud, longitud
        );
        
        Log.log.info(query);

        String url = "https://overpass-api.de/api/interpreter";

        try {
            Log.log.info("Construyendo conexión HTTP al servicio Overpass API.");
            URL apiUrl = new URL(url + "?data=" + java.net.URLEncoder.encode(query, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            Log.log.info("Código de respuesta HTTP: {}", responseCode);

            if (responseCode == 200) {
                Log.log.info("Conexión exitosa. Procesando respuesta del servidor.");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray elements = jsonResponse.optJSONArray("elements");

                if (elements != null) {
                    for (int i = 0; i < elements.length(); i++) {
                        JSONObject element = elements.getJSONObject(i);
                        JSONObject tags = element.optJSONObject("tags");
                        if (tags != null && tags.has("maxspeed")) {
                            String maxspeed = tags.getString("maxspeed");
                            Log.log.info("Velocidad máxima encontrada: {} km/h", maxspeed); 
                            return Double.parseDouble(maxspeed);
                        }
                    }
                }
                Log.log.info("No se encontró velocidad máxima en la vía. Devolviendo valor predeterminado.");
                return -1; // Valor predeterminado si no se encuentra maxspeed
            } else {
                Log.log.info("Error en la consulta al servidor. Código de respuesta: {}", responseCode);
                throw new Exception("Error al consultar el servicio Overpass API.");
            }
        } catch (Exception e) {
            Log.log.info("Error al consultar límite de velocidad: {}", e.getMessage(), e);

            Log.log.info("No se encontró velocidad previa. Devolviendo valor predeterminado.");
            return -1; // Valor predeterminado en caso de error
        }
    }
    
    public static double calcularTiempoEnvio(int idEnvio){
        Timestamp primera = FachadaEnvioBD.getPrimeraFecha(idEnvio);
        Timestamp ultima = FachadaEnvioBD.getUltimaFecha(idEnvio);
        
        // Calcular la diferencia en milisegundos
        double diferenciaEnMilisegundos = ultima.getTime() - primera.getTime();

        // Convertir la diferencia a horas, minutos y segundos
        double minutosTotales = diferenciaEnMilisegundos / 60000;
        
        return minutosTotales;
        
    }
    
    public static int contarMinutosPorEncimaDelUmbral(int idEnvio) {
        // Obtener los datos de temperatura y humedad para el idEnvio
        ArrayList<TemperaturaHumedad> datos = FachadaEnvioBD.getTemperaturaHumedad(idEnvio);

        // Obtener el umbral de temperatura
        double umbral = FachadaEnvioBD.getTemperaturaMax(idEnvio);

        // Ordenar los datos por fecha (asegurarnos de que están ordenados cronológicamente)
        Collections.sort(datos, Comparator.comparing(TemperaturaHumedad::getFecha));

        // Variable para contar los minutos
        int minutosTotales = 0;

        // Recorrer el ArrayList y calcular los minutos
        for (int i = 0; i < datos.size() - 1; i++) {
            TemperaturaHumedad actual = datos.get(i);
            TemperaturaHumedad siguiente = datos.get(i + 1);

            // Verificar si la temperatura está por encima del umbral
            if (actual.getTemperatura() > umbral) {
                // Calcular la diferencia de tiempo en minutos entre los dos objetos
                long diferenciaMillis = siguiente.getFecha().getTime() - actual.getFecha().getTime();
                int diferenciaMinutos = (int) (diferenciaMillis / (1000 * 60));

                // Sumar los minutos al total
                minutosTotales += diferenciaMinutos;
            }
        }

        return minutosTotales;
    }
}

   
