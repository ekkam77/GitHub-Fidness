public class Administrador extends Usuario {
    private static final long serialVersionUID = 1L;

    public Administrador(String nombre, String contrasena) {
        super(nombre, contrasena);
    }

    @Override
    public String getRol() { return "Administrador"; }
}
