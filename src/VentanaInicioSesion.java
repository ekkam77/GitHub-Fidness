import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VentanaInicioSesion extends JFrame implements ActionListener {
    private SistemaFidness sistema;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonIngresar;
    private JButton botonRegistro;

    public VentanaInicioSesion(SistemaFidness sistema) {
        this.sistema = sistema;
        setTitle("Fidness - Inicio de sesión");
        setSize(460, 320);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("FIDNESS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridLayout(4, 1, 5, 5));
        campos.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        campoUsuario = new JTextField();
        campoContrasena = new JPasswordField();
        campos.add(new JLabel("Usuario:"));
        campos.add(campoUsuario);
        campos.add(new JLabel("Contraseña:"));
        campos.add(campoContrasena);
        add(campos, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        botonIngresar = new JButton("Ingresar");
        botonRegistro = new JButton("Registrarme");
        botonIngresar.addActionListener(this);
        botonRegistro.addActionListener(this);
        botones.add(botonIngresar);
        botones.add(botonRegistro);
        add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(botonIngresar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonRegistro) {
            new VentanaRegistro(this, sistema).setVisible(true);
        } else if (e.getSource() == botonIngresar) {
            try {
                Usuario usuario = sistema.iniciarSesion(campoUsuario.getText(),
                        new String(campoContrasena.getPassword()));
                new VentanaPrincipal(sistema, usuario).setVisible(true);
                campoContrasena.setText("");
                dispose();
            } catch (DatosInvalidosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                campoContrasena.setText("");
            }
        }
    }
}
