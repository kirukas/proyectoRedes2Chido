package com.company;

import java.io.IOException;

public class ConstructorArchivo {
    private Paquete peticionArchivo;
    private Archivo reconstruido;
    private conexionToServer conexionServidor;
    private String rutaFragmento = "/home/enrique/Documentos/Redes2/ARCHIVOSRECONSTRUIDOS";
    private  int puerto = 2121;
    private Paquete respuesta;
    public ConstructorArchivo(String nombreArchivo,String ip, int worker){
        peticionArchivo = new Paquete(1,nombreArchivo.hashCode(),worker,1);
        conexionServidor = new conexionToServer(ip,puerto);
        conexionServidor.inicalizaConexion();
    }
    public boolean PaquteFinal(Paquete p){
        if(p.getPaqueteFinal() == 1)return true;
        else return false;
    }
    public void reconstruirArchivo(int numFragmento){
        setPeticionArchivo();
        boolean esPaqueteFinal = false;
        if(conexionServidor.getConexionServer().isConnected()){
            System.out.println("Peticion  de Archivo:"+peticionArchivo.toString());
            if(conexionServidor.enviarPaqute(peticionArchivo)){
                System.out.println("Obteniendo fragmento "+numFragmento+" ...");
                try {
                    reconstruido = new Archivo(rutaFragmento+numFragmento,"rw");
                    while(!esPaqueteFinal){
                     try {
                            if((conexionServidor.getFlujoEntrada().available()) > 0){
                                respuesta = new Paquete(conexionServidor.leer());
                                reconstruido.escribirFinal(respuesta.getDatos());
                                if(PaquteFinal(respuesta)) esPaqueteFinal = true;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                     }
                    }
                 } catch (ArchivoNoExiste archivoNoExiste) {
                        archivoNoExiste.printStackTrace();
                 }
            }else System.out.println("Error al enviar la peticion del paquete ");
        }

    }
    private  void setPeticionArchivo(){
        byte[] ArrayNulo = new byte[1];
        peticionArchivo.setPaquteFinal(0);
        peticionArchivo.setDatos(ArrayNulo);
    }


}
