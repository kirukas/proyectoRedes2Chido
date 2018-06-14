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
    private static final String ruta = "/home/enrique/redes/respaldo";
   // private static final String ruta = "/home/redes/respaldo";
    private ServerSocket servidor;
    private  boolean servidorActivo, acceparConexion;
    private boolean isWorker;
    private  int numeroServidor;
    private String[] espejo = new String[3];
    private conexionToServer conexionMirror;
    private OutputStream flujoSalida;
    private Paquete datos;
    private Archivo fragmento;
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
    private  void setPaquete(int de, int hasta){
        datos.setLongitudPaquete(hasta-de);
        try {
            datos.setDatos(fragmento.getDatos(de, hasta));

        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        }
    }
    private void enviarPaquete(){
        System.out.println("Datos trama a enviar "+datos.toString());
        try {
            flujoSalida.write(datos.castByteArray());
            flujoSalida.flush();
            if(datos.getPaqueteFinal() == 1){
                flujoSalida.close();
                servidorActivo = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void enviarArchivo(int hashCode){
        System.out.println("Buscando archivo...");
        int de,hasta,longitudMaximaPaquete,longitudArchivo;
        de = 0;
        try {
            fragmento = new Archivo(ruta+"/"+hashCode);
            System.out.println("Enviando el archivo ...");
            datos = new Paquete(0,hashCode,numeroServidor);
            longitudArchivo = (int)fragmento.getSize();
            longitudMaximaPaquete = datos.getLongitudMaximoPaquete();// longitud de la trama - cabecera de la trama
                hasta = longitudMaximaPaquete;
                if(hasta > longitudArchivo) {
                    System.out.println("Solo se enviara un paquete !!");
                    setPaquete(de, hasta);
                    datos.setPaquteFinal(1);
                    enviarPaquete();
                }else{
                    boolean segmentarEnPaquetes = true;
                    while (segmentarEnPaquetes){
                        System.out.println("de "+de+" hasta "+hasta);
                        setPaquete(de,hasta);
                        datos.setPaquteFinal(0);
                        enviarPaquete();
                        de = hasta;
                        if((hasta + longitudMaximaPaquete) > longitudArchivo){
                            hasta = longitudArchivo;
                            segmentarEnPaquetes = false;
                        }else hasta += longitudMaximaPaquete;
                     }
                     System.out.println("de "+de+" hasta "+hasta);
                    setPaquete(de,hasta);
                    datos.setPaquteFinal(1);
                    enviarPaquete();
                 }
                 System.out.println("Archivo enviado con exito ...");
        } catch (ArchivoNoExiste archivoNoExiste) {
            System.err.println("El archivo no existe!!");
             archivoNoExiste.printStackTrace();
            return;
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
                //if(isWorker) respaldarEnEspejo(paquete);/// respalda en su espejo
                if(paquete.getPaqueteFinal() == 1) servidorActivo = false;
                break;
            case  1:
                System.out.println("Peticion de un archivo ...");
                enviarArchivo(paquete.getHashCode());
                break;

            default:System.out.println("tipo de Paquete desconocido...");
        }
    }
    public void escucharPeticiones(){
        byte[] ArrayByte = new byte[longitudTrama];
        if(isWorker) System.out.println("Worker activo ...");
        else System.out.println("Mirror activo ...");
        try {
            servidor = new ServerSocket(puerto);
            acceparConexion = true;
          /*  if(isWorker){
                if(conexionMirror.inicalizaConexion())System.out.println("Conectado con su espejo...");
                else System.out.println("No se conecto con su espejo !!");
            }*/
            while (acceparConexion){
                Socket conexion = servidor.accept();
                conexion.setSoLinger(true,10);
                System.out.println("Aceptando conexion con "+servidor.getInetAddress().getHostAddress());
                InputStream  flujoEntrada = conexion.getInputStream();
                flujoSalida = conexion.getOutputStream();
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
                conexion.close();
                System.out.println("Cerrando la conexion...");
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
