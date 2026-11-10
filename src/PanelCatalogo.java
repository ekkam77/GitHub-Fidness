import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class PanelCatalogo extends JPanel implements ActionListener {
    private SistemaFidness sistema;
    private VentanaPrincipal ventana;
    private JComboBox<String> filtro;
    private JTextField campoBuscar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private ArrayList<Ejercicio> visibles;
    private JButton botonBuscar;
    private JButton botonActualizar;
    private JButton botonDetalle;
    private JButton botonAgregar;

    public PanelCatalogo(SistemaFidness sistema, VentanaPrincipal ventana) {
        this.sistema = sistema;
        this.ventana = ventana;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro = new JComboBox<String>(new String[]{"Todos", "Pierna", "Espalda", "Brazo", "Pecho", "Hombro", "Abdomen"});
        campoBuscar = new JTextField(18);
        botonBuscar = new JButton("Buscar");
        botonActualizar = new JButton("Actualizar");
        filtros.add(new JLabel("Tipo:"));
        filtros.add(filtro);
        filtros.add(new JLabel("Buscar:"));
        filtros.add(campoBuscar);
        filtros.add(botonBuscar);
        filtros.add(botonActualizar);
        add(filtros, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"Nombre", "Tipo", "Nivel"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botonDetalle = new JButton("Ver detalle");
        botonAgregar = new JButton("Agregar a mi rutina");
        botones.add(botonDetalle);
        botones.add(botonAgregar);
        add(botones, BorderLayout.SOUTH);
        botonBuscar.addActionListener(this);
        botonActualizar.addActionListener(this);
        botonDetalle.addActionListener(this);
        botonAgregar.addActionListener(this);
        filtro.addActionListener(this);
        campoBuscar.addActionListener(this);
        cargar(false);
    }

    private void cargar(boolean avisar) {
        visibles = sistema.buscarEjercicios((String) filtro.getSelectedItem(), campoBuscar.getText());
        modelo.setRowCount(0);
        for (int i = 0; i < visibles.size(); i++) {
            Ejercicio ejercicio = visibles.get(i);
            modelo.addRow(new Object[]{ejercicio.getNombre(), ejercicio.getTipo(), ejercicio.getNivel()});
        }
        if (avisar && visibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay resultados para esa búsqueda.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonActualizar) {
            campoBuscar.setText("");
            filtro.setSelectedIndex(0);
            cargar(false);
        } else if (e.getSource() == botonBuscar || e.getSource() == filtro
                || e.getSource() == campoBuscar) {
            cargar(true);
        } else {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un ejercicio de la tabla.");
                return;
            }
            Ejercicio ejercicio = visibles.get(fila);
            if (e.getSource() == botonDetalle) {
                JTextArea area = new JTextArea(ejercicio.obtenerDetalle(), 18, 48);
                area.setEditable(false);
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setCaretPosition(0);
                JOptionPane.showMessageDialog(this, new JScrollPane(area),
                        "Detalle del ejercicio", JOptionPane.INFORMATION_MESSAGE);
            } else if (e.getSource() == botonAgregar) {
                ventana.agregarDesdeCatalogo(ejercicio);
            }
        }
    }
}
