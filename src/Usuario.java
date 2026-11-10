import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String contrasena;

    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public String getNombre() { return nombre; }
    public String getContrasena() { return contrasena; }
    public abstract String getRol();

    @Override
    public String toString() {
        return nombre + " (" + getRol() + ")";
    }
}
