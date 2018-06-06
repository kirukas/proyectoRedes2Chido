package com.company;

public class Main {

    public static void main(String[] args) {
        String rutaArchivo = "/home/enrique/Documentos/texto.txt";
        Archivo prueba = null;
        String[] fragmento;
        int numeroWorkes = 3;
        String ip = "127.0.0.1";
        String[]  workers = new String[numeroWorkes];
        workers[0] = "127.0.0.1";//"192.168.30.2";
        workers[1] = "192.168.30.3";
        workers[2] = "192.168.30.4";
        String[] espejo = new String[numeroWorkes];
        espejo[0] = "192.168.31.2";
        espejo[1] = "192.168.31.3";
        espejo[2] = "192.168.31.4";
        Cliente cliente;
        /*
        try {
            prueba = new Archivo(rutaArchivo);
            Fragmentador f = new Fragmentador(prueba,numeroWorkes);
            f.segmentar();
            fragmento = f.getRutasFragmentos();
            prueba.close();
            for (int i = 0; i < 1; i++) {
                cliente = new Cliente(fragmento[i],prueba.getNombre(),workers[i],i);
                cliente.enviarArchivo();
            }

        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        ConstructorArchivo  file = new ConstructorArchivo("texto.txt",workers[0],0);
        file.reconstruirArchivo(0);

    }
}
