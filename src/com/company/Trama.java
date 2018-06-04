package com.company;

public class Trama {
    private int longitudTrama = 1024;
    private int tipoTrama;
    private int hashCode;
    private  int worker;
    public Trama(){}
    public  Trama(int tipoTrama, int hashCode, int worker){
        this.worker = worker;
        this.hashCode = hashCode;
        this.tipoTrama = tipoTrama;
    }
    @Override
    public String toString() {
        return "longitud del paquete "+getLongitudTrama()+ "  tipo de trama "+getTipoTrama()+
                "  Hashcode "+getHashCode() +"  numero de worker "+ getWorker();
    }
    public int getWorker(){return worker;}
    public void setWorker(int worker){this.worker = worker;}
    public void setTipoTrama(int tipoTrama) { this.tipoTrama = tipoTrama; }
    public void setHashCode(int hashCode) { this.hashCode = hashCode; }
    public int getHashCode() { return hashCode; }
    public int getLongitudTrama() { return longitudTrama; }
    public int getTipoTrama() { return tipoTrama; }
    public void setLongitudTrama(int longitudTrama) { this.longitudTrama = longitudTrama; }

}
