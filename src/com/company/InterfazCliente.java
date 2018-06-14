package com.company;

public class InterfazCliente {
    private Archivo file = null;
    private String[] fragmento;
    private int numeroWorkes = 3;
    private String[]  workers = new String[numeroWorkes];
    private String[] espejo = new String[numeroWorkes];
    private String[] rutaFragmento = new String[3];

    private void obtenerFragmento(String archivo){
        workers[0] = "192.168.30.2";
        workers[1] = "192.168.30.3";
        workers[2] = "192.168.30.4";
        /// direcciones de los espejos
        espejo[0] = "192.168.31.2";
        espejo[1] = "192.168.31.3";
        espejo[2] = "192.168.31.4";
        ConstructorArchivo[] fragmento = new ConstructorArchivo[numeroWorkes];
        for (int i = 0; i < workers.length ; i++) {
            fragmento[i] = new ConstructorArchivo(archivo,espejo[i],i);
            fragmento[i].getFragmento(i);
            rutaFragmento[i] = fragmento[i].getRutaFragmento();
        }


    }
    public void getArchivo(String nombre){
        obtenerFragmento(nombre);
        unirFragmentos(nombre);
    }
    private void unirFragmentos(String nombre){

        try {
            file = new Archivo(nombre,"rw");
            for (int i = 0; i <rutaFragmento.length ; i++) {
                Archivo fragmento = new Archivo(rutaFragmento[i],"rw");
                file.escribirFinal(fragmento.getDatos(0,(int)fragmento.getSize()));
            }
        } catch (ArchivoNoExiste archivoNoExiste) {
            archivoNoExiste.printStackTrace();
        }
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
            file = new Archivo(rutaArchivo);
            Fragmentador f = new Fragmentador(file,numeroWorkes);
            f.segmentar();
            fragmento = f.getRutasFragmentos();
            file.close();
            for (int i = 0; i < numeroWorkes; i++) {
                cliente = new Cliente(fragmento[i], file.getNombre(),espejo[i],i);
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
