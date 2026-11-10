import java.io.Serializable;

public class Ejercicio implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String tipo;
    private String descripcion;
    private String equipo;
    private String nivel;
    private String pasos;
    private int series;
    private int repeticiones;

    public Ejercicio(String nombre, String tipo, String descripcion, String equipo,
            String nivel, String pasos, int series, int repeticiones) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.equipo = equipo;
        this.nivel = nivel;
        this.pasos = pasos;
        this.series = series;
        this.repeticiones = repeticiones;
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public String getEquipo() { return equipo; }
    public String getNivel() { return nivel; }
    public String getPasos() { return pasos; }
    public int getSeries() { return series; }
    public int getRepeticiones() { return repeticiones; }

    public String obtenerDetalle() {
        return nombre + "\n\nTipo: " + tipo + "\nNivel: " + nivel
                + "\nEquipo: " + equipo + "\n\n" + descripcion
                + "\n\nSugerido: " + series + " series de " + repeticiones
                + " repeticiones\n\nCómo se ejecuta:\n" + pasos;
    }

    @Override
    public String toString() { return nombre + " (" + tipo + ")"; }
}
