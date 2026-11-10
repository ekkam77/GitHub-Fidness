import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class PanelMisRutinas extends JPanel implements ActionListener {
    private SistemaFidness sistema;
    private Usuario usuario;
    private JTable tabla;
    private DefaultTableModel modelo;
    private ArrayList<Rutina> rutinas;
    private JButton botonVer;
    private JButton botonExportar;
    private JButton botonEliminar;
    private JButton botonActualizar;

    public PanelMisRutinas(SistemaFidness sistema, Usuario usuario) {
        this.sistema = sistema;
        this.usuario = usuario;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        modelo = new DefaultTableModel(new String[]{"Nombre", "Ejercicios", "Creada"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botonVer = new JButton("Ver rutina");
        botonExportar = new JButton("Exportar a archivo .txt");
        botonEliminar = new JButton("Eliminar");
        botonActualizar = new JButton("Actualizar");
        botones.add(botonVer);
        botones.add(botonExportar);
        botones.add(botonEliminar);
        botones.add(botonActualizar);
        add(botones, BorderLayout.SOUTH);
        botonVer.addActionListener(this);
        botonExportar.addActionListener(this);
        botonEliminar.addActionListener(this);
        botonActualizar.addActionListener(this);
        actualizar();
    }

    public void actualizar() {
        rutinas = sistema.obtenerRutinas(usuario);
        modelo.setRowCount(0);
        for (int i = 0; i < rutinas.size(); i++) {
            Rutina rutina = rutinas.get(i);
            modelo.addRow(new Object[]{rutina.getNombre(), rutina.getItems().size(), rutina.getFecha()});
        }
    }

    private void exportar(Rutina rutina) throws IOException {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar rutina como texto");
        selector.setFileFilter(new FileNameExtensionFilter("Archivo de texto (*.txt)", "txt"));
        selector.setSelectedFile(new File("rutina.txt"));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File archivo = selector.getSelectedFile();
        if (!archivo.getName().toLowerCase(java.util.Locale.ROOT).endsWith(".txt")) {
            archivo = new File(archivo.getAbsolutePath() + ".txt");
        }
        if (archivo.exists() && JOptionPane.showConfirmDialog(this,
                "El archivo ya existe. ¿Desea reemplazarlo?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        ExportadorRutina.exportar(rutina, archivo);
        JOptionPane.showMessageDialog(this, "Rutina exportada en:\n" + archivo.getAbsolutePath());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonActualizar) {
            actualizar();
            return;
        }
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una rutina de la tabla.");
            return;
        }
        Rutina rutina = rutinas.get(fila);
        try {
            if (e.getSource() == botonVer) {
                JTextArea area = new JTextArea(rutina.obtenerTexto(), 22, 55);
                area.setEditable(false);
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setCaretPosition(0);
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Mi rutina",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (e.getSource() == botonExportar) {
                exportar(rutina);
            } else if (e.getSource() == botonEliminar) {
                if (JOptionPane.showConfirmDialog(this, "¿Eliminar la rutina " + rutina.getNombre() + "?",
                        "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    sistema.eliminarRutina(usuario, rutina);
                    actualizar();
                }
            }
        } catch (DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo completar la operación.\n" + ex.getMessage());
        }
    }
}
