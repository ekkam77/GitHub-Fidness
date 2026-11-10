import java.io.IOException;
import java.util.ArrayList;

public class SistemaFidness {
    private DatosFidness datos;
    private ArchivoDatos archivo;
    private ArrayList<Ejercicio> ejercicios;

    public SistemaFidness(ArchivoDatos archivo) throws IOException, ClassNotFoundException {
        this.archivo = archivo;
        datos = archivo.cargar();
        ejercicios = CatalogoInicial.crear();
    }

    public Usuario iniciarSesion(String nombre, String contrasena) throws DatosInvalidosException {
        if (nombre.trim().isEmpty() || contrasena.isEmpty()) {
            throw new DatosInvalidosException("Ingrese su usuario y su contraseña.");
        }
        for (int i = 0; i < datos.getUsuarios().size(); i++) {
            Usuario usuario = datos.getUsuarios().get(i);
            if (usuario.getNombre().equalsIgnoreCase(nombre.trim())
                    && usuario.getContrasena().equals(contrasena)) {
                return usuario;
            }
        }
        throw new DatosInvalidosException("Usuario o contraseña incorrectos.");
    }

    public void registrarUsuario(String nombre, String contrasena, String confirmacion)
            throws DatosInvalidosException, IOException {
        nombre = nombre.trim();
        if (nombre.isEmpty() || contrasena.trim().isEmpty() || confirmacion.isEmpty()) {
            throw new DatosInvalidosException("Todos los campos son obligatorios.");
        }
        if (contrasena.length() < 6) {
            throw new DatosInvalidosException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (!contrasena.equals(confirmacion)) {
            throw new DatosInvalidosException("Las contraseñas no coinciden.");
        }
        for (int i = 0; i < datos.getUsuarios().size(); i++) {
            if (datos.getUsuarios().get(i).getNombre().equalsIgnoreCase(nombre)) {
                throw new DatosInvalidosException("El nombre de usuario ya existe.");
            }
        }
        Usuario nuevo = new Cliente(nombre, contrasena);
        datos.getUsuarios().add(nuevo);
        try {
            archivo.guardar(datos);
        } catch (IOException e) {
            datos.getUsuarios().remove(nuevo);
            throw e;
        }
    }

    public ArrayList<Ejercicio> buscarEjercicios(String tipo, String nombre) {
        ArrayList<Ejercicio> resultado = new ArrayList<Ejercicio>();
        for (int i = 0; i < ejercicios.size(); i++) {
            Ejercicio ejercicio = ejercicios.get(i);
            if ((tipo.equals("Todos") || ejercicio.getTipo().equals(tipo))
                    && ejercicio.getNombre().toLowerCase(java.util.Locale.ROOT).contains(
                            nombre.trim().toLowerCase(java.util.Locale.ROOT))) {
                resultado.add(ejercicio);
            }
        }
        return resultado;
    }

    public ArrayList<Rutina> obtenerRutinas(Usuario usuario) {
        ArrayList<Rutina> resultado = new ArrayList<Rutina>();
        for (int i = 0; i < datos.getRutinas().size(); i++) {
            Rutina rutina = datos.getRutinas().get(i);
            if (rutina.getUsuario().equals(usuario.getNombre())) {
                resultado.add(rutina);
            }
        }
        return resultado;
    }

    public void guardarRutina(Usuario usuario, Rutina rutina)
            throws DatosInvalidosException, IOException {
        if (!rutina.getUsuario().equals(usuario.getNombre())) {
            throw new DatosInvalidosException("Solo puede guardar sus propias rutinas.");
        }
        if (rutina.getItems().isEmpty()) {
            throw new DatosInvalidosException("Agregue al menos un ejercicio.");
        }
        ArrayList<Rutina> propias = obtenerRutinas(usuario);
        for (int i = 0; i < propias.size(); i++) {
            if (propias.get(i).getNombre().equalsIgnoreCase(rutina.getNombre())) {
                throw new DatosInvalidosException("Ya tiene una rutina con ese nombre.");
            }
        }
        datos.getRutinas().add(rutina);
        try {
            archivo.guardar(datos);
        } catch (IOException e) {
            datos.getRutinas().remove(rutina);
            throw e;
        }
    }

    public void eliminarRutina(Usuario usuario, Rutina rutina)
            throws DatosInvalidosException, IOException {
        int posicion = datos.getRutinas().indexOf(rutina);
        if (!rutina.getUsuario().equals(usuario.getNombre()) || posicion < 0) {
            throw new DatosInvalidosException("Solo puede eliminar sus propias rutinas guardadas.");
        }
        datos.getRutinas().remove(posicion);
        try {
            archivo.guardar(datos);
        } catch (IOException e) {
            datos.getRutinas().add(posicion, rutina);
            throw e;
        }
    }
}
