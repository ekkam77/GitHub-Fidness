import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class PanelConstructorRutina extends JPanel implements ActionListener {
    private SistemaFidness sistema;
    private Usuario usuario;
    private VentanaPrincipal ventana;
    private JTextField campoNombre;
    private JTextField campoSeries;
    private JTextField campoRepeticiones;
    private JList<Ejercicio> listaDisponibles;
    private JList<ItemRutina> listaItems;
    private DefaultListModel<ItemRutina> modeloItems;
    private ArrayList<ItemRutina> items;
    private JButton botonAgregar;
    private JButton botonQuitar;
    private JButton botonSubir;
    private JButton botonBajar;
    private JButton botonGuardar;
    private JButton botonLimpiar;

    public PanelConstructorRutina(SistemaFidness sistema, Usuario usuario, VentanaPrincipal ventana) {
        this.sistema = sistema;
        this.usuario = usuario;
        this.ventana = ventana;
        items = new ArrayList<ItemRutina>();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel nombre = new JPanel(new BorderLayout(10, 10));
        nombre.add(new JLabel("Nombre de la rutina:"), BorderLayout.WEST);
        campoNombre = new JTextField();
        nombre.add(campoNombre, BorderLayout.CENTER);
        add(nombre, BorderLayout.NORTH);

        DefaultListModel<Ejercicio> modeloDisponibles = new DefaultListModel<Ejercicio>();
        ArrayList<Ejercicio> ejercicios = sistema.buscarEjercicios("Todos", "");
        for (int i = 0; i < ejercicios.size(); i++) {
            modeloDisponibles.addElement(ejercicios.get(i));
        }
        listaDisponibles = new JList<Ejercicio>(modeloDisponibles);
        listaDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modeloItems = new DefaultListModel<ItemRutina>();
        listaItems = new JList<ItemRutina>(modeloItems);
        listaItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel listas = new JPanel(new GridLayout(1, 2, 15, 10));
        JPanel disponibles = new JPanel(new BorderLayout(5, 5));
        disponibles.add(new JLabel("Ejercicios disponibles"), BorderLayout.NORTH);
        disponibles.add(new JScrollPane(listaDisponibles), BorderLayout.CENTER);
        JPanel miRutina = new JPanel(new BorderLayout(5, 5));
        miRutina.add(new JLabel("Mi rutina (series x repeticiones)"), BorderLayout.NORTH);
        miRutina.add(new JScrollPane(listaItems), BorderLayout.CENTER);
        listas.add(disponibles);
        listas.add(miRutina);
        add(listas, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new GridLayout(10, 1, 5, 5));
        acciones.add(new JLabel("Series (1-10):"));
        campoSeries = new JTextField("3");
        acciones.add(campoSeries);
        acciones.add(new JLabel("Repeticiones (1-50):"));
        campoRepeticiones = new JTextField("12");
        acciones.add(campoRepeticiones);
        botonAgregar = new JButton("Agregar >>");
        botonQuitar = new JButton("Quitar");
        botonSubir = new JButton("Subir");
        botonBajar = new JButton("Bajar");
        acciones.add(botonAgregar);
        acciones.add(botonQuitar);
        acciones.add(botonSubir);
        acciones.add(botonBajar);
        add(acciones, BorderLayout.EAST);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonGuardar = new JButton("Guardar rutina");
        botonLimpiar = new JButton("Limpiar");
        botones.add(botonGuardar);
        botones.add(botonLimpiar);
        add(botones, BorderLayout.SOUTH);
        botonAgregar.addActionListener(this);
        botonQuitar.addActionListener(this);
        botonSubir.addActionListener(this);
        botonBajar.addActionListener(this);
        botonGuardar.addActionListener(this);
        botonLimpiar.addActionListener(this);
    }

    public void seleccionarEjercicio(Ejercicio ejercicio) {
        listaDisponibles.setSelectedValue(ejercicio, true);
        campoSeries.setText("" + ejercicio.getSeries());
        campoRepeticiones.setText("" + ejercicio.getRepeticiones());
    }

    private void actualizarLista() {
        modeloItems.clear();
        for (int i = 0; i < items.size(); i++) {
            modeloItems.addElement(items.get(i));
        }
    }

    private void agregar() throws DatosInvalidosException {
        Ejercicio ejercicio = listaDisponibles.getSelectedValue();
        if (ejercicio == null) {
            throw new DatosInvalidosException("Seleccione un ejercicio disponible.");
        }
        int series = Integer.parseInt(campoSeries.getText().trim());
        int repeticiones = Integer.parseInt(campoRepeticiones.getText().trim());
        ItemRutina nuevo = new ItemRutina(ejercicio, series, repeticiones);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getEjercicio().getNombre().equalsIgnoreCase(ejercicio.getNombre())) {
                throw new DatosInvalidosException("El ejercicio ya está en la rutina.");
            }
        }
        items.add(nuevo);
        actualizarLista();
        listaItems.setSelectedIndex(items.size() - 1);
    }

    private void mover(int cambio) throws DatosInvalidosException {
        int posicion = listaItems.getSelectedIndex();
        if (posicion < 0) {
            throw new DatosInvalidosException("Seleccione un ejercicio de su rutina.");
        }
        int destino = posicion + cambio;
        if (destino >= 0 && destino < items.size()) {
            ItemRutina item = items.remove(posicion);
            items.add(destino, item);
            actualizarLista();
            listaItems.setSelectedIndex(destino);
        }
    }

    private void guardar() throws DatosInvalidosException, IOException {
        Rutina rutina = new Rutina(campoNombre.getText(), usuario.getNombre());
        for (int i = 0; i < items.size(); i++) {
            rutina.agregarItem(items.get(i));
        }
        sistema.guardarRutina(usuario, rutina);
        ventana.actualizarRutinas();
        JOptionPane.showMessageDialog(this, "Rutina guardada correctamente.");
        limpiar();
    }

    private void limpiar() {
        campoNombre.setText("");
        items.clear();
        actualizarLista();
        listaDisponibles.clearSelection();
        campoSeries.setText("3");
        campoRepeticiones.setText("12");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == botonAgregar) {
                agregar();
            } else if (e.getSource() == botonQuitar) {
                int posicion = listaItems.getSelectedIndex();
                if (posicion < 0) {
                    throw new DatosInvalidosException("Seleccione un ejercicio de su rutina.");
                }
                items.remove(posicion);
                actualizarLista();
            } else if (e.getSource() == botonSubir) {
                mover(-1);
            } else if (e.getSource() == botonBajar) {
                mover(1);
            } else if (e.getSource() == botonGuardar) {
                guardar();
            } else if (e.getSource() == botonLimpiar) {
                if (JOptionPane.showConfirmDialog(this, "¿Limpiar la rutina en preparación?",
                        "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    limpiar();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese números enteros para series y repeticiones.");
        } catch (DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la rutina.\n" + ex.getMessage());
        }
    }
}
