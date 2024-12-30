/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic;

import db.FachadaClienteBD;

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
    
    /* Sacar todos los envíos realizados por un cliente
    *
    *
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
    
    /* Sacar todos los posibles receptores
    *
    *
    */
    
    /* Crear un nuevo envío
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
