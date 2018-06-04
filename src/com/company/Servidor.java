package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Servidor {
    private  static  final int  puerto = 2121;
    private  static final int longitudTrama = 1024;
    private static final String ruta = "/home/enrique/Documentos/Redes2/ARCHIVOSRECONSTRUIDOS";
    private ServerSocket servidor;
    private  boolean servidorActivo, acceparConexion;
    private Paquete peticion;
    private boolean isWorker;
    public Servidor(int tipoServidor){
        if(tipoServidor == 0) isWorker = true;
        else  isWorker = false;
    }
    public void escucharPeticiones(){
        byte[] ArrayByte = new byte[longitudTrama];
        if(isWorker) System.out.println("Worker activo ...");
        else System.out.println("Mirror activo ...");
        try {
            servidor = new ServerSocket(puerto);
            acceparConexion = true;
            while (acceparConexion){
                Socket conexion = servidor.accept();
                System.out.println("Aceptando conexion con "+servidor.getInetAddress());
                InputStream  flujoEntrada = conexion.getInputStream();
                servidorActivo = true;
                while (servidorActivo){
                    if(flujoEntrada.available() > 0){
                        System.out.println("Se recivio una peticion ..");
                        flujoEntrada.read(ArrayByte);
                        Paquete paquete = new Paquete(ArrayByte);
                        System.out.println("info de la trama "+paquete.toString());
                        servidorActivo = false;
                    }

                }
                flujoEntrada.close();
                System.out.println("Cerrando conexion con "+servidor.getInetAddress());
                conexion.close();

            }
        }  catch (UnknownHostException e){
            //System.out.println(e);
            System.out.println("Servidor no encontrado..." );

        }
        catch (IOException e) {
            System.out.println("Error de conexion con la maquina "+servidor.getInetAddress()+"\nRevisar el servicio...");
            //  e.printStackTrace();

        }
    }
    public static void main(String[] args){
        int tipoServidor;
        if((tipoServidor = Integer.parseInt(args[0])) > 1) {
            System.err.println("Error tipo de servidor desconocido ...");
            System.exit(0);
        }
        Servidor servidor = new Servidor(tipoServidor);
        servidor.escucharPeticiones();
    }
}
