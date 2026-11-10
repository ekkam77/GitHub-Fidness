public class Cliente extends Usuario {
    private static final long serialVersionUID = 1L;

    public Cliente(String nombre, String contrasena) {
        super(nombre, contrasena);
    }

    @Override
    public String getRol() { return "Cliente"; }
}
