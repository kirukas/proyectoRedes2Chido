package com.company;

public class ConstructorArchivo {
    private Paquete peticionArchivo;
    private Archivo reconstruido;
    private conexionToServer conexionServidor;
    private  int puerto = 2121;
    public ConstructorArchivo(String nombreArchivo,String ip, int worker){
        peticionArchivo = new Paquete(1,nombreArchivo.hashCode(),worker,1);
        conexionServidor = new conexionToServer(ip,puerto);
        conexionServidor.inicalizaConexion();
    }
    public void reconstruirArchivo(){
        setPeticionArchivo();
        if(conexionServidor.getConexionServer().isConnected()){
            System.out.println("Peticion  de Archivo:"+peticionArchivo.toString());
            if(conexionServidor.enviarPaqute(peticionArchivo)){
                //conexionServidor.recivir();
                System.out.println("Espera de paquetes");
            }else System.out.println("Error al enviar la peticion del paquete ");
        }

    }
    private  void setPeticionArchivo(){
        byte[] ArrayNulo = new byte[1];
        peticionArchivo.setPaquteFinal(1);
        peticionArchivo.setDatos(ArrayNulo);
    }


}
