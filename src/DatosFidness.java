import java.io.Serializable;
import java.util.ArrayList;

public class DatosFidness implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Rutina> rutinas;

    public DatosFidness() {
        usuarios = new ArrayList<Usuario>();
        rutinas = new ArrayList<Rutina>();
        usuarios.add(new Cliente("eduardo", "clave123"));
        usuarios.add(new Administrador("admin", "admin123"));
    }

    public ArrayList<Usuario> getUsuarios() { return usuarios; }
    public ArrayList<Rutina> getRutinas() { return rutinas; }
}
