/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

import db.FachadaClienteBD;
import db.FachadaEnvioBD;
import java.util.ArrayList;

/**
 *
 * @author socra
 */
public class Controlador {  
    
    /* Validar cliente
    * 1. Comprobar que dicho nombre no existe en la base de datos
    */
    public static boolean validarCliente(String nombre, String pw){
        boolean valido = FachadaClienteBD.validarUsuario(nombre, pw);
        
        return valido;
    }
    
    /* Registrar un cliente
    * 1. Comprobar que dicho nombre no existe en la base de datos
    * 2. Crear nuevo cliente
    * 3. insertarlo a la BD
    */
    public static boolean registrarCliente(String nombre, String pw, double longitud, double latitud, String tipo){
        boolean existe;
        
        int id = FachadaClienteBD.getIdByNombre(nombre);
        
        if (id != -1){
            existe = true;
        } else{
            existe = false;
            
            Cliente cliente = new Cliente(nombre, new Ubicacion(longitud, latitud), tipo);
            int idNuevo = FachadaClienteBD.insertCliente(cliente, pw);
        }
        
        return !existe;
    }
    
    /* Sacar todos los posibles receptores
    * 1. consultar todos los clientes de tipo receptor
    */
    public static ArrayList<Cliente> obtenerReceptores(){
        ArrayList<Cliente> clientes = FachadaClienteBD.getClientesByTipo("Receptor");
        
        return clientes;
    }
    
    /* sacar todos los transportistas
    * 1. consultar todos los transportistas
    */
    public static ArrayList<Transportista> obtenerTransportistas(){
        ArrayList<Transportista> t = FachadaEnvioBD.getTransportistas();
        
        return t;
    }
    
    /* Crear un nuevo envío
    * Dato el id del cliente, 
    */
    
    /* Sacar todos los envíos realizados por un cliente
    * 1. Dado un id llamar a la fachada para obtener los envíos
    */
    
    
    /* Ver el historial de un envío realizado
    *
    *
    */
    
    /* Sacar todos los envíos que siguen en envío de un cliente
    *
    *
    */
    
    /* Ver el historial de un envío desde una fecha
    *
    *
    */
    
    /* Registrar nueva temperatura y humedad
    * 
    *
    */
    
    /* Registrar localización
    * 
    *
    */
    
    /* Registrar ventilador
    * 
    *
    */
    
    /* Registrar cambio de estado
    * 
    *
    */
    
    
}
