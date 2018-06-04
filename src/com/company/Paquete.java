package com.company;

import java.nio.ByteBuffer;

public class Paquete  extends Trama{

    private final static  int sizeInt = Integer.BYTES;

    private final static int sizeCabecera = 5*sizeInt;
    private  int longitudPaquete;/// en bytes
    private  int paquteFinal;
    private byte[] datos;

    public  Paquete(int tipoTrama, int hashCode, int worker ){
        super(tipoTrama,hashCode,worker);
    }
    public  Paquete(int tipoTrama, int hashCode, int worker,int longitudPaquete ){
        super(tipoTrama,hashCode,worker);
        this.longitudPaquete = longitudPaquete;
    }

    public Paquete(byte[] byteArray ){
        setTipoTrama(ByteBuffer.wrap(byteArray,0,sizeInt).getInt());
        setHashCode(ByteBuffer.wrap(byteArray,sizeInt,2*sizeInt).getInt());
        setWorker(ByteBuffer.wrap(byteArray,2*sizeInt,3*sizeInt).getInt());
        setLongitudPaquete(ByteBuffer.wrap(byteArray,3*sizeInt,4*sizeInt).getInt());
        setPaquteFinal (ByteBuffer.wrap(byteArray,4*sizeInt,5*sizeInt).getInt());
        datos = new byte[super.getLongitudTrama() - sizeCabecera];
        System.arraycopy(byteArray,sizeCabecera,datos,0,getLongitudPaquete());
        setDatos(datos);
    }



    public byte[] castByteArray(){
        byte[] ArrayRaw = new byte[super.getLongitudTrama()];
        ByteBuffer.wrap(ArrayRaw,0,sizeInt).putInt(super.getTipoTrama());
        ByteBuffer.wrap(ArrayRaw,sizeInt,2*sizeInt).putInt(super.getHashCode());
        ByteBuffer.wrap(ArrayRaw,2*sizeInt,3*sizeInt).putInt(super.getWorker());
        ByteBuffer.wrap(ArrayRaw,3*sizeInt,4*sizeInt).putInt(getLongitudPaquete());
        ByteBuffer.wrap(ArrayRaw,4*sizeInt,5*sizeInt).putInt(getPaqueteFinal());
        System.arraycopy(datos,0,ArrayRaw,sizeCabecera, datos.length);
        return  ArrayRaw;
    }
    public int getLongitudMaximoPaquete(){ return (super.getLongitudTrama() - sizeCabecera); }
    public int getLongitudPaquete(){return longitudPaquete;}
    public void setLongitudPaquete(int l){longitudPaquete = l;}
    public byte[] getDatos() {
        byte [] auxDatos  =  new byte[getLongitudPaquete()];
        System.arraycopy(datos,0,auxDatos,0,getLongitudPaquete());
        return auxDatos;
    }
    public void setPaquteFinal(int f){ paquteFinal = f; }
    public int getPaqueteFinal(){return paquteFinal;}
    public void setDatos(byte[] datos) { this.datos = datos; }

    public static int getSizeCabecera() { return sizeCabecera; }

    ///////////////Metodos de la clase Trama
    @Override
    public String toString() {
        return super.toString()+" longitud de la info "+ getLongitudPaquete();
    }


    @Override
    public int getHashCode() {
        return super.getHashCode();
    }

    @Override
    public int getLongitudTrama() {
        return super.getLongitudTrama();
    }

    @Override
    public int getTipoTrama() {
        return super.getTipoTrama();
    }

    @Override
    public int getWorker() {
        return super.getWorker();
    }

    @Override
    public void setHashCode(int hashCode) {
        super.setHashCode(hashCode);
    }

    @Override
    public void setLongitudTrama(int longitudTrama) {
        super.setLongitudTrama(longitudTrama);
    }


    @Override
    public void setTipoTrama(int tipoTrama) {
        super.setTipoTrama(tipoTrama);
    }
}
