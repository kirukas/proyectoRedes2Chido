package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Servidor {
    private  static  final int  puerto = 2121;
    private  static final int longitudTrama = 1024;
    private static final String ruta = "/home/enrique/Documentos/Redes2/ARCHIVOSRECONSTRUIDOS";
    private ServerSocket servidor;
    private  boolean servidorActivo, acceparConexion;
    private boolean isWorker;
    private String[] espejo = new String[3];

    public Servidor(int tipoServidor){
        if(tipoServidor == 0) isWorker = true;
        else  isWorker = false;
        espejo[0] = "192.168.31.2";
        espejo[1] = "192.168.31.3";
        espejo[2] = "192.168.31.4";
    }
    private void respaldarDatos(Paquete p){
        try {
            Archivo file = new Archivo(ruta+"/"+p.getHashCode(),"rw");
            file.escribirFinal(p.getDatos());
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        }
    }
    private boolean buscarArchivo(int hashCode){
        try {
            Archivo file = new Archivo(ruta+"/"+hashCode);
            return true;
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
            return false;
        }
    }
    private void respaldarEspejos(Paquete p){
        try {
            Socket conexionServidor = new Socket(espejo[p.getWorker()], puerto);
           OutputStream  salida = conexionServidor.getOutputStream();
           salida.write(p.castByteArray());
           salida.flush();
           if(p.getPaqueteFinal() == 1){
               salida.close();
               conexionServidor.close();
           }
        } catch (UnknownHostException e){
            //System.out.println(e);
            System.out.println("Servidor no encontrado..." );

        }
        catch (IOException e) {
            System.out.println("Error de conexion con la maquina "+servidor.getInetAddress()+"\nRevisar el servicio...");
            //  e.printStackTrace();
        }
    }
    private void clasificaPaquete(Paquete paquete){
        switch (paquete.getTipoTrama()){
            case 0:
                System.out.println("Respaldando los datos...");
                respaldarDatos(paquete);
                if(paquete.getPaqueteFinal() == 1)servidorActivo = false;
                if(isWorker) respaldarEspejos(paquete);
                break;
            case  1:
                if(buscarArchivo(paquete.getHashCode())); System.out.println("El archivo existe ...");
                break;

            default:System.out.println("tipo de Paquete desconocido");
        }
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
                conexion.setSoLinger(true,10);
                System.out.println("Aceptando conexion con "+servidor.getInetAddress().getHostAddress());
                InputStream  flujoEntrada = conexion.getInputStream();
                OutputStream flujoSalida = conexion.getOutputStream();
                servidorActivo = true;
                int aux;
                while (servidorActivo){
                    if( flujoEntrada.available() > 0){
                        System.out.println("Se recivio una peticion ..");
                        flujoEntrada.read(ArrayByte);
                        Paquete paquete = new Paquete(ArrayByte);
                        System.out.println("info de la trama "+paquete.toString());
                        clasificaPaquete(paquete);
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
