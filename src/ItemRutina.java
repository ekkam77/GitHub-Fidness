import java.io.Serializable;

public class ItemRutina implements Serializable {
    private static final long serialVersionUID = 1L;
    private Ejercicio ejercicio;
    private int series;
    private int repeticiones;

    public ItemRutina(Ejercicio ejercicio, int series, int repeticiones)
            throws DatosInvalidosException {
        if (ejercicio == null) {
            throw new DatosInvalidosException("Seleccione un ejercicio.");
        }
        if (series < 1 || series > 10 || repeticiones < 1 || repeticiones > 50) {
            throw new DatosInvalidosException("Series: de 1 a 10. Repeticiones: de 1 a 50.");
        }
        this.ejercicio = ejercicio;
        this.series = series;
        this.repeticiones = repeticiones;
    }

    public Ejercicio getEjercicio() { return ejercicio; }
    public int getSeries() { return series; }
    public int getRepeticiones() { return repeticiones; }

    @Override
    public String toString() {
        return ejercicio.getNombre() + " - " + series + " x " + repeticiones;
    }
}
