package app;

public class Vuelo {

    private final int id;
    private final String Ciudad_Salida;
    private final String Ciudad_Llegada;
    private final String Fecha;
    private final int Asientos;

    public Vuelo(int id, String Ciudad_Salida, String Ciudad_Llegada, String Fecha, int Asientos){
        this.id = id;
        this.Ciudad_Salida = Ciudad_Salida;
        this.Ciudad_Llegada = Ciudad_Llegada;
        this.Fecha = Fecha;
        this.Asientos = Asientos;

    }


    public int getId() {
        return id;
    }

    public String getCiudad_Salida() {
        return Ciudad_Salida;
    }

    public String getCiudad_Llegada() {
        return Ciudad_Llegada;
    }

    public String getFecha() {
        return Fecha;
    }

    public int getAsientos() {
        return Asientos;
    }

}
