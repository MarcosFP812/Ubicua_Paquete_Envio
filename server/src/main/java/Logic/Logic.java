package Logic;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    // Método simulado para obtener la velocidad permitida en la vía
    public static double obtenerVelocidadVia(double longitud, double latitud) {
        // Este método debería interactuar con un servicio de mapas o API de tráfico
        // Por ahora se devuelve un valor fijo para propósitos de demostración
        return 80; // Ejemplo: velocidad permitida de 80 km/h
    }

}
