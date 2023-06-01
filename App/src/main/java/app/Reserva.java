package app;

/**
 * Objeto que representa las reservas, para usarlo en una TableView.
 */
public class Reserva {

    private int ID;
    private String nameSubname;
    private int Asiento;
    private String CiudadSalida;
    private String CiudadDestino;
    private String Avion;
    private String Fecha;

    /**
     * Constructor de Reserva.
     *
     * @param ID            ID de la reserva
     * @param nameSubname   Nombre y apellidos del propietario de la reserva
     * @param Asiento       Asiento de la reserva
     * @param CiudadSalida  Ciudad de salida de la reserva
     * @param CiudadDestino Ciudad de destino de la reserva
     * @param Avion         Avión del vuelo de la reserva
     * @param fecha         Fecha de salida del avión de la reserva
     * @see InicioUserController#loadReservas()
     */
    public Reserva(int ID, String nameSubname, int Asiento, String CiudadSalida, String CiudadDestino, String Avion, String fecha) {
        this.ID = ID;
        this.nameSubname = nameSubname;
        this.Asiento = Asiento;
        this.CiudadSalida = CiudadSalida;
        this.CiudadDestino = CiudadDestino;
        this.Avion = Avion;
        this.Fecha = fecha;
    }

    /**
     * Obtiene el nombre y apellidos del propietario de la reserva.
     *
     * @return El nombre y apellidos del propietario de la reserva
     */
    public String getNameSubname() {
        return this.nameSubname;
    }

    /**
     * Establece el nombre y apellidos del propietario de la reserva.
     *
     * @param nameSubname El nombre y apellidos del propietario de la reserva
     */
    public void setNameSubname(String nameSubname) {
        this.nameSubname = nameSubname;
    }

    /**
     * Obtiene el ID de la reserva.
     *
     * @return El ID de la reserva
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Establece el ID de la reserva.
     *
     * @param ID El ID de la reserva
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Obtiene el asiento de la reserva.
     *
     * @return El asiento de la reserva
     */
    public int getAsiento() {
        return this.Asiento;
    }

    /**
     * Establece el asiento de la reserva.
     *
     * @param asiento El asiento de la reserva
     */
    public void setAsiento(int asiento) {
        this.Asiento = asiento;
    }

    /**
     * Obtiene la ciudad de salida de la reserva.
     *
     * @return La ciudad de salida de la reserva
     */
    public String getCiudadSalida() {
        return this.CiudadSalida;
    }

    /**
     * Establece la ciudad de salida de la reserva.
     *
     * @param ciudadSalida La ciudad de salida de la reserva
     */
    public void setCiudadSalida(String ciudadSalida) {
        this.CiudadSalida = ciudadSalida;
    }

    /**
     * Obtiene la ciudad de destino de la reserva.
     *
     * @return La ciudad de destino de la reserva
     */
    public String getCiudadDestino() {
        return this.CiudadDestino;
    }

    /**
     * Establece la ciudad de destino de la reserva.
     *
     * @param ciudadDestino La ciudad de destino de la reserva
     */
    public void setCiudadDestino(String ciudadDestino) {
        this.CiudadDestino = ciudadDestino;
    }

    /**
     * Obtiene el avión del vuelo de la reserva.
     *
     * @return El avión del vuelo de la reserva
     */
    public String getAvion() {
        return this.Avion;
    }

    /**
     * Establece el avión del vuelo de la reserva.
     *
     * @param avion El avión del vuelo de la reserva
     */
    public void setAvion(String avion) {
        this.Avion = avion;
    }

    /**
     * Obtiene la fecha de salida del avión de la reserva.
     *
     * @return La fecha de salida del avión de la reserva
     */
    public String getFecha() {
        return this.Fecha;
    }

    /**
     * Establece la fecha de salida del avión de la reserva.
     *
     * @param fecha La fecha de salida del avión de la reserva
     */
    public void setFecha(String fecha) {
        this.Fecha = fecha;
    }
}
