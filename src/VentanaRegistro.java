import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VentanaRegistro extends JDialog implements ActionListener {
    private SistemaFidness sistema;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JPasswordField campoConfirmacion;
    private JButton botonGuardar;
    private JButton botonCancelar;

    public VentanaRegistro(VentanaInicioSesion inicio, SistemaFidness sistema) {
        super(inicio, "Fidness - Crear cuenta", true);
        this.sistema = sistema;
        setSize(440, 300);
        setResizable(false);
        setLocationRelativeTo(inicio);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        JPanel campos = new JPanel(new GridLayout(6, 1, 5, 5));
        campos.setBorder(BorderFactory.createEmptyBorder(15, 30, 0, 30));
        campoUsuario = new JTextField();
        campoContrasena = new JPasswordField();
        campoConfirmacion = new JPasswordField();
        campos.add(new JLabel("Nombre de usuario:"));
        campos.add(campoUsuario);
        campos.add(new JLabel("Contraseña (mínimo 6 caracteres):"));
        campos.add(campoContrasena);
        campos.add(new JLabel("Confirmar contraseña:"));
        campos.add(campoConfirmacion);
        add(campos, BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout());
        botonGuardar = new JButton("Guardar");
        botonCancelar = new JButton("Cancelar");
        botonGuardar.addActionListener(this);
        botonCancelar.addActionListener(this);
        botones.add(botonGuardar);
        botones.add(botonCancelar);
        add(botones, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonCancelar) {
            dispose();
        } else if (e.getSource() == botonGuardar) {
            try {
                sistema.registrarUsuario(campoUsuario.getText(),
                        new String(campoContrasena.getPassword()),
                        new String(campoConfirmacion.getPassword()));
                JOptionPane.showMessageDialog(this, "Cuenta creada correctamente.");
                dispose();
            } catch (DatosInvalidosException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la cuenta.\n" + ex.getMessage());
            }
        }
    }
}
