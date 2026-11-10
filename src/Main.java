import java.awt.EventQueue;
import java.io.File;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        // La carga se realiza antes de mostrar las ventanas.
        try {
            ArchivoDatos archivo = new ArchivoDatos(new File("datos/fidness.dat"));
            final SistemaFidness sistema = new SistemaFidness(archivo);
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new VentanaInicioSesion(sistema).setVisible(true);
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar Fidness.\n"
                    + "Revise datos/fidness.dat y los permisos de la carpeta.\n"
                    + "El archivo existente no se ha borrado.\n" + e.getMessage(),
                    "Error al iniciar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
