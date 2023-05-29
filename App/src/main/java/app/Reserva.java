package app;

import java.util.Date;

public class Reserva {

    private int ID;
    private String nameSubname;
    private int Asiento;
    private String CiudadSalida;
    private String CiudadDestino;
    private String Avion;
    private String Fecha;

    public Reserva(int ID, String nameSubname, int Asiento, String CiudadSalida, String CiudadDestino, String Avion, String fecha){
        this.ID = ID;
        this.nameSubname = nameSubname;
        this.Asiento = Asiento;
        this.CiudadSalida = CiudadSalida;
        this.CiudadDestino = CiudadDestino;
        this.Avion = Avion;
        this.Fecha = fecha;
    }

    public String getNameSubname() {
        return this.nameSubname;
    }

    public void setNameSubname(String nameSubname) {
        this.nameSubname = nameSubname;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAsiento() {
        return this.Asiento;
    }

    public void setAsiento(int asiento) {
        this.Asiento = asiento;
    }

    public String getCiudadSalida() {
        return this.CiudadSalida;
    }

    public void setCiudadSalida(String ciudadSalida) {
        this.CiudadSalida = ciudadSalida;
    }

    public String getCiudadDestino() {
        return this.CiudadDestino;
    }

    public void setCiudadDestino(String ciudadDestino) {
        this.CiudadDestino = ciudadDestino;
    }

    public String getAvion() {
        return this.Avion;
    }

    public void setAvion(String avion) {
        this.Avion = avion;
    }

    public String getFecha() {
        return this.Fecha;
    }

    public void setFecha(String fecha) {
        this.Fecha = fecha;
    }
}
