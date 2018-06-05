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
    //private static final String ruta = "/home/enrique/Documentos/Redes2/ARCHIVOSRECONSTRUIDOS";
    private static final String ruta = "/home/redes/respaldo";
    private ServerSocket servidor;
    private  boolean servidorActivo, acceparConexion;
    private boolean isWorker;
    private  int numeroServidor;
    private String[] espejo = new String[3];
    private conexionToServer conexionMirror;
    public Servidor(int tipoServidor, int numeroServidor){
        espejo[0] = "192.168.31.2";
        espejo[1] = "192.168.31.3";
        espejo[2] = "192.168.31.4";
        this.numeroServidor = numeroServidor;
        if(tipoServidor == 0){
            isWorker = true;
            conexionMirror = new conexionToServer(espejo[numeroServidor],puerto);
        }
        else  isWorker = false;


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
    public  void respaldarEnEspejo(Paquete p){
        if(conexionMirror.getConexionServer().isConnected()){
            conexionMirror.enviarPaqute(p);
            return;
        }
    }
    private void clasificaPaquete(Paquete paquete){
        switch (paquete.getTipoTrama()){
            case 0:
                System.out.println("Guardando los datos...");
                respaldarDatos(paquete);
                if(isWorker) respaldarEnEspejo(paquete);
                if(paquete.getPaqueteFinal() == 1) servidorActivo = false;
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
            if(conexionMirror.inicalizaConexion())System.out.println("Conectado con su espejo...");
            else System.out.println("No se conecto con su espejo !!");
            while (acceparConexion){
                Socket conexion = servidor.accept();
                conexion.setSoLinger(true,10);
                System.out.println("Aceptando conexion con "+servidor.getInetAddress().getHostAddress());
                InputStream  flujoEntrada = conexion.getInputStream();
                OutputStream flujoSalida = conexion.getOutputStream();
                servidorActivo = true;
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
        int tipoServidor, numerServidor;

        if((tipoServidor = Integer.parseInt(args[0])) > 1) {
            System.err.println("Error tipo de servidor desconocido ...");
            System.exit(0);
        }
        if((numerServidor = Integer.parseInt(args[1])) > 2) {
            System.err.println("Solo existen 3  servidores ...");
            System.exit(0);
        }
        Servidor servidor = new Servidor(tipoServidor, numerServidor);
       // if(servidor.conexionMirror.inicalizaConexion());System.out.println("Conectando con sys ");
        servidor.escucharPeticiones();

    }
}
