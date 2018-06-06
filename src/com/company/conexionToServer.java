package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class conexionToServer {
    private  static  final int longuitudTrama = 1024;
    private Socket conexionServer;
    private OutputStream flujoSalida;
    private InputStream flujoEntrada;
    private  String ip;
    private  int puerto;
    public conexionToServer(String ip , int puerto){
        this.ip = ip;
        this.puerto = puerto;
        //inicalizaConexion();
    }
    public boolean inicalizaConexion(){
        try {
            conexionServer = new Socket(ip,puerto);
            conexionServer.setSoLinger(true,10);
            flujoSalida = conexionServer.getOutputStream();
            flujoEntrada = conexionServer.getInputStream();
            return true;
        } catch (UnknownHostException e){
            //System.out.println(e);
            System.out.println("Servidor no encontrado..." );
            return false;
        }
        catch (IOException e) {
            System.out.println("Error de conexion con la maquina "+ip+"\nRevisar el servicio...");
            //  e.printStackTrace();
            return false;
        }
    }

    public OutputStream getFlujoSalida() { return flujoSalida; }

    public InputStream getFlujoEntrada() { return flujoEntrada; }

    public Socket getConexionServer() { return conexionServer; }
    public byte[] leer(){
        byte[] byteArray = new byte[longuitudTrama];
        try {
            flujoEntrada.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public boolean enviarPaqute(Paquete p){
        try {
            flujoSalida.write(p.castByteArray());
            flujoSalida.flush();
            if(p.getPaqueteFinal() == 1){
                flujoSalida.close();
                conexionServer.close();
                System.out.println("Cerrando la conexion con "+ip);
            }
            return true;
        } catch (IOException e) {
            System.out.println("No se pudo enviar el paqute ");
            e.printStackTrace();
            return false;
        }
    }
}
