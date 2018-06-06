package com.company;

public class InterfazCliente {
    private Archivo prueba = null;
    private String[] fragmento;
    private int numeroWorkes = 3;
    private String[]  workers = new String[numeroWorkes];
    private String[] espejo = new String[numeroWorkes];
    private String[] rutasFragmento = new String[3];
    public void getArchivo(String archivo){
        workers[0] = "192.168.30.2";
        workers[1] = "192.168.30.3";
        workers[2] = "192.168.30.4";
        /// direcciones de los espejos
        espejo[0] = "192.168.31.2";
        espejo[1] = "192.168.31.3";
        espejo[2] = "192.168.31.4";
        ConstructorArchivo[] fragmento = new ConstructorArchivo[numeroWorkes];
        for (int i = 0; i < 1 ; i++) {
            fragmento[i] = new ConstructorArchivo(archivo,espejo[i],i);
            fragmento[i].getFragmento(i);
            rutasFragmento[i] = fragmento[i].getRutaFragmento();
        }


    }
    public void unirArchivo(String nombreArchivo){
        
    }
    public  void segmentarArchivo( String rutaArchivo){
        workers[0] = "192.168.30.2";
        workers[1] = "192.168.30.3";
        workers[2] = "192.168.30.4";
        /// direcciones de los espejos
        espejo[0] = "192.168.31.2";
        espejo[1] = "192.168.31.3";
        espejo[2] = "192.168.31.4";
    Cliente cliente;
        try {
            prueba = new Archivo(rutaArchivo);
            Fragmentador f = new Fragmentador(prueba,numeroWorkes);
            f.segmentar();
            fragmento = f.getRutasFragmentos();
            prueba.close();
            for (int i = 0; i < numeroWorkes; i++) {
                cliente = new Cliente(fragmento[i],prueba.getNombre(),espejo[i],i);
                cliente.enviarArchivo();
            }

        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
