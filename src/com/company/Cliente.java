package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    private String  rutaArchivo;
    private String IP;
    private int puerto = 2121;
    private int longitudArchivo;
    private Archivo fragmento;
    private int de, hasta;
    private Paquete datos;
    private int longitudMaximaPaquete;
    private Socket conexionServidor;
    private InputStream entrada;
    private OutputStream salida;
    private conexionToServer toServer;
    // constructor para archivos fragmentados
    public Cliente(String ruta,String archivoOriginal , String ip, int worker){
        try {
            fragmento = new Archivo(ruta);
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        }
        IP = ip;
        longitudArchivo = (int)fragmento.getSize();
        datos = new Paquete(0,archivoOriginal.hashCode(),worker);
        longitudMaximaPaquete = datos.getLongitudMaximoPaquete();
        toServer = new conexionToServer(ip,puerto);
        toServer.inicalizaConexion();
        de = 0;
    }

    private  void setPaquete(int de, int hasta){
        datos.setLongitudPaquete(hasta-de);
        try {
            datos.setDatos(fragmento.getDatos(de, hasta));

        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        }
    }

    public void enviarArchivo(){
        if(toServer.getConexionServer().isConnected()){
            hasta = longitudMaximaPaquete;
            if(hasta > longitudArchivo) {
                System.out.println("Solo se enviara un paquete !!");
                setPaquete(de, hasta);
                datos.setPaquteFinal(1);
                toServer.enviarPaqute(datos);
            }else{
                boolean segmentarEnPaquetes = true;
                while (segmentarEnPaquetes){
                    System.out.println("de "+de+" hasta "+hasta);
                    setPaquete(de,hasta);
                    datos.setPaquteFinal(0);
                    toServer.enviarPaqute(datos);
                    de = hasta;
                    if((hasta + longitudMaximaPaquete) > longitudArchivo){
                        hasta = longitudArchivo;
                        segmentarEnPaquetes = false;
                    }else hasta += longitudMaximaPaquete;
                }
                System.out.println("de "+de+" hasta "+hasta);
                setPaquete(de,hasta);
                datos.setPaquteFinal(1);
                toServer.enviarPaqute(datos);
            }

        }else System.out.println("Error de conexion ...");

    }
   /* public void enviarArchivo(){
        System.out.println("Enviando el Archivo ...");
        try {
            conexionServidor = new Socket(IP,puerto);
            salida = conexionServidor.getOutputStream();
            hasta = longitudMaximaPaquete;
            if(hasta > longitudArchivo){
                System.out.println("Solo se enviara un paquete !!");
                setPaquete(de,hasta);
                datos.setPaquteFinal(1);
                salida.write(datos.castByteArray());
                salida.flush();
            }else{
                boolean segmentarEnPaquetes = true;
                while (segmentarEnPaquetes){
                    System.out.println("de "+de+" hasta "+hasta);
                    setPaquete(de,hasta);
                    datos.setPaquteFinal(0);
                    salida.write(datos.castByteArray());
                    //salida.flush();
                    de = hasta;
                    if((hasta + longitudMaximaPaquete) > longitudArchivo){
                        hasta = longitudArchivo;
                        segmentarEnPaquetes = false;
                    }else hasta += longitudMaximaPaquete;
                }
                System.out.println("de "+de+" hasta "+hasta);
                setPaquete(de,hasta);
                datos.setPaquteFinal(1);
                salida.write(datos.castByteArray());

                //salida.flush();
            }
            salida.flush();
            salida.close();
            conexionServidor.close();

        } catch (UnknownHostException e){
            //System.out.println(e);
            System.out.println("Servidor no encontrado..." );

        }
        catch (IOException e) {
            System.out.println("Error de conexion con la maquina "+IP +"\nRevisar el servicio...");
            //  e.printStackTrace();

        }
    }
     /* private void estableceConexion(){
        try {
            conexionServidor = new Socket(IP,puerto);
            //entrada = conexionServidor.getInputStream();
            salida = conexionServidor.getOutputStream();

        } catch (UnknownHostException e){
            //System.out.println(e);
            System.out.println("Servidor no encontrado..." );

        }
        catch (IOException e) {
            System.out.println("Error de conexion con la maquina "+IP +"\nRevisar estado de la maquina");
            //  e.printStackTrace();

        }

    }*/

  /*  public void enviaArchivo(){
        System.out.println("numero maximimo paquete "+longitudMaximaPaquete);
        System.out.println("longitud del archivo "+ longitudArchivo);
        hasta = longitudMaximaPaquete;
        boolean segmentarEnPaquetes = false;
        if(hasta > longitudArchivo){
            System.out.println("Solo se enviara un paquete !!");
            setPaquete(de,hasta);
            enviar(datos,segmentarEnPaquetes);
        }else{
            segmentarEnPaquetes = true;
            while (segmentarEnPaquetes){
                System.out.println("de "+de+" hasta "+hasta);
                setPaquete(de,hasta);
                enviar(datos,segmentarEnPaquetes);
                de = hasta;
                if((hasta + longitudMaximaPaquete) > longitudArchivo){
                    hasta = longitudArchivo;
                    segmentarEnPaquetes = false;
                }else hasta += longitudMaximaPaquete;
            }
            System.out.println("de "+de+" hasta "+hasta);
            setPaquete(de,hasta);
            enviar(datos,segmentarEnPaquetes);
        }

    }*/


}