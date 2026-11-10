import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ExportadorRutina {
    public static void exportar(Rutina rutina, File archivo) throws IOException {
        try (BufferedWriter salida = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(archivo), "UTF-8"))) {
            salida.write(rutina.obtenerTexto());
        }
    }
}
