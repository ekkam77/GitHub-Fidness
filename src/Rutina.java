import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Rutina implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private String usuario;
    private String fecha;
    private ArrayList<ItemRutina> items;

    public Rutina(String nombre, String usuario) throws DatosInvalidosException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre de la rutina es obligatorio.");
        }
        this.nombre = nombre.trim();
        this.usuario = usuario;
        this.fecha = LocalDate.now().toString();
        this.items = new ArrayList<ItemRutina>();
    }

    public String getNombre() { return nombre; }
    public String getUsuario() { return usuario; }
    public String getFecha() { return fecha; }
    public ArrayList<ItemRutina> getItems() {
        return new ArrayList<ItemRutina>(items);
    }

    public void agregarItem(ItemRutina item) throws DatosInvalidosException {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getEjercicio().getNombre().equalsIgnoreCase(
                    item.getEjercicio().getNombre())) {
                throw new DatosInvalidosException("El ejercicio ya está en la rutina.");
            }
        }
        items.add(item);
    }

    public void quitarItem(int posicion) {
        items.remove(posicion);
    }

    public void moverItem(int posicion, int destino) {
        if (posicion >= 0 && posicion < items.size() && destino >= 0 && destino < items.size()) {
            ItemRutina item = items.remove(posicion);
            items.add(destino, item);
        }
    }

    public String obtenerTexto() {
        String texto = "RUTINA: " + nombre + "\nUsuario: " + usuario
                + "    Fecha: " + fecha + "\n\n";
        for (int i = 0; i < items.size(); i++) {
            ItemRutina item = items.get(i);
            texto = texto + (i + 1) + ". " + item.getEjercicio().getNombre()
                    + " (" + item.getEjercicio().getTipo() + ") - "
                    + item.getSeries() + " series x " + item.getRepeticiones()
                    + " repeticiones\n" + item.getEjercicio().getPasos() + "\n\n";
        }
        return texto + "Total de ejercicios: " + items.size();
    }

    @Override
    public String toString() { return nombre + " - " + fecha; }
}
