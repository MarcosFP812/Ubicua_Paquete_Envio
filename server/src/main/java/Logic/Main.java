/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Logic;

import db.FachadaBD;

/**
 *
 * @author socra
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FachadaBD fachada = new FachadaBD();
        
        // Crear un cliente para pruebas
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Pérez");
        String PW = "123456";
        cliente.getUbicacion().setLongitud(-99.1332);
        cliente.getUbicacion().setLatitud(19.4326);
        cliente.setTipo("RECEPTOR"); // Puede ser "RECEPTOR" o "REMITENTE"

        System.out.println("Insertando cliente...");
        boolean insertSuccess = FachadaBD.insertCliente(cliente, PW);
        if (insertSuccess) {
            System.out.println("Cliente insertado exitosamente.");
        } else {
            System.out.println("Error al insertar el cliente.");
        }
    }
    
}
