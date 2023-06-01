package app;

/**
 * Objeto que representa los vuelos, para usarlo en una TableView.
 */
public class Vuelo {

    private final int id;
    private final String Ciudad_Salida;
    private final String Ciudad_Llegada;
    private final String Fecha;
    private final int Asientos;

    /**
     * Constructor de Vuelo.
     *
     * @param id             ID del vuelo
     * @param Ciudad_Salida  Ciudad de salida del vuelo
     * @param Ciudad_Llegada Ciudad de llegada del vuelo
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
     * Obtiene el ID del vuelo.
     *
     * @return El ID del vuelo
     */
    public int getId() {
        return this.id;
    }

    /**
     * Obtiene la ciudad de salida del vuelo.
     *
     * @return La ciudad de salida del vuelo
     */
    public String getCiudad_Salida() {
        return this.Ciudad_Salida;
    }

    /**
     * Obtiene la ciudad de llegada del vuelo.
     *
     * @return La ciudad de llegada del vuelo
     */
    public String getCiudad_Llegada() {
        return this.Ciudad_Llegada;
    }

    /**
     * Obtiene la fecha de salida del vuelo.
     *
     * @return La fecha de salida del vuelo
     */
    public String getFecha() {
        return this.Fecha;
    }

    /**
     * Obtiene la cantidad de asientos libres en el vuelo.
     *
     * @return La cantidad de asientos libres en el vuelo
     */
    public int getAsientos() {
        return this.Asientos;
    }
}
