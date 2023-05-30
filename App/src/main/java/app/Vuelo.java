package app;

/**
 * Objecto que respresenta los vuelos, para usarlo en una TableView
 */
public class Vuelo {

    private final int id;
    private final String Ciudad_Salida;
    private final String Ciudad_Llegada;
    private final String Fecha;
    private final int Asientos;

    /**
     * @param id             I del vuelo
     * @param Ciudad_Salida  Ciudad de Salida del vuelo
     * @param Ciudad_Llegada Ciudad de Llegada del vuelo
     * @param Fecha          Fecha de salida del vuelo
     * @param Asientos       Asientos libres del vuelo
     * @see InicioUserController#loadVuelos()
     */
    public Vuelo(int id, String Ciudad_Salida, String Ciudad_Llegada, String Fecha, int Asientos) {
        this.id = id;
        this.Ciudad_Salida = Ciudad_Salida;
        this.Ciudad_Llegada = Ciudad_Llegada;
        this.Fecha = Fecha;
        this.Asientos = Asientos;

    }


    /**
     * Obtener la ID
     *
     * @return ID del vuelo
     */
    public int getId() {
        return this.id;
    }

    /**
     * Obtener la ciudad de Salida
     *
     * @return Ciudad de Salida
     */
    public String getCiudad_Salida() {
        return this.Ciudad_Salida;
    }

    /**
     * Obtener la Ciudad de Llegada
     *
     * @return Ciudad de Llegada
     */
    public String getCiudad_Llegada() {
        return this.Ciudad_Llegada;
    }

    /**
     * Obtener la Fecha del vuelo
     *
     * @return Fecha
     */
    public String getFecha() {
        return this.Fecha;
    }

    /**
     * Obtener los asientos libres
     *
     * @return Asientos Libres
     */
    public int getAsientos() {
        return this.Asientos;
    }

}
