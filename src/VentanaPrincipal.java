import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class VentanaPrincipal extends JFrame implements ActionListener {
    private SistemaFidness sistema;
    private JButton botonCerrar;
    private JTabbedPane pestanas;
    private PanelConstructorRutina constructor;
    private PanelMisRutinas misRutinas;

    public VentanaPrincipal(SistemaFidness sistema, Usuario usuario) {
        this.sistema = sistema;
        setTitle("Fidness - Ejercicios y rutinas");
        setSize(1000, 640);
        setMinimumSize(new java.awt.Dimension(920, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Polimorfismo: se usa Usuario, pero getRol() depende de su clase real.
        superior.add(new JLabel("Usuario: " + usuario.toString()));
        botonCerrar = new JButton("Cerrar sesión");
        botonCerrar.addActionListener(this);
        superior.add(botonCerrar);
        add(superior, BorderLayout.NORTH);

        constructor = new PanelConstructorRutina(sistema, usuario, this);
        misRutinas = new PanelMisRutinas(sistema, usuario);
        pestanas = new JTabbedPane();
        pestanas.addTab("Catálogo", new PanelCatalogo(sistema, this));
        pestanas.addTab("Constructor de rutina", constructor);
        pestanas.addTab("Mis rutinas", misRutinas);
        add(pestanas, BorderLayout.CENTER);
    }

    public void agregarDesdeCatalogo(Ejercicio ejercicio) {
        constructor.seleccionarEjercicio(ejercicio);
        pestanas.setSelectedIndex(1);
    }

    public void actualizarRutinas() {
        misRutinas.actualizar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonCerrar) {
            dispose();
            new VentanaInicioSesion(sistema).setVisible(true);
        }
    }
}
