package com.company;
/*Esta clase  fragmenta el archivo en n archivos*/
public class Fragmentador {
    private Archivo file;
    private int numeroFragmentos;
    private String[] rutasFragmentos;
    private String rutaCopias = "/home/enrique/Documentos/";
    public Fragmentador( Archivo archivo , int numeroFragmentos){
        file = archivo;
        this.numeroFragmentos = numeroFragmentos;
        rutasFragmentos = new String[this.numeroFragmentos];
    }
    public void segmentar(){
        System.out.println("informacion del archivo a segmentar:\n"+file.toString());
        int  Acopiar ,rango;
        long sizeBytesArchivo = file.getSize();
        if(sizeBytesArchivo < numeroFragmentos){
            System.err.println("El numero de fragmentos es mayor al numero de bytes del archivo");
            System.exit(0);
        }
        Acopiar  = rango = (int)(sizeBytesArchivo/numeroFragmentos);
        int de = 0;
        for (int i = 0; i < numeroFragmentos ; i++) {
                if((Acopiar + rango) > sizeBytesArchivo){
                    Acopiar = (int)sizeBytesArchivo;
                }
            try {
                String ruta = file.getRuta()+"fragmento"+i;

                Archivo copia = new Archivo(ruta,"rw");
                System.out.println("El archivo "+ i+" guarda de :  "+de+"  hasta :"+Acopiar);
                copia.escribir(file.getDatos(de,Acopiar));
                copia.close();
                rutasFragmentos[i] = ruta;
            } catch (ArchivoNoExiste archivoNoExiste) {
                archivoNoExiste.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            de+=rango;
                Acopiar+=rango;
        }
    }
    public String[] getRutasFragmentos() { return rutasFragmentos; }
}
