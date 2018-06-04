package com.company;

public class Main {

    public static void main(String[] args) {
        String rutaArchivo = "/home/enrique/Documentos/texto.txt";
        Archivo prueba = null;
        String[] fragmento;
        int numeroWorkes = 3;
        String ip = "127.0.0.1";
        Cliente cliente;
        try {
            prueba = new Archivo(rutaArchivo);
            Fragmentador f = new Fragmentador(prueba,numeroWorkes);
            f.segmentar();
            fragmento = f.getRutasFragmentos();
            cliente = new Cliente(fragmento[0],prueba.getNombre(),ip);
            cliente.enviarArchivo();
            prueba.close();


        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
