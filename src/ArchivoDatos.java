import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ArchivoDatos {
    private File archivo;

    public ArchivoDatos(File archivo) {
        this.archivo = archivo;
    }

    public DatosFidness cargar() throws IOException, ClassNotFoundException {
        if (!archivo.exists()) {
            DatosFidness datos = new DatosFidness();
            guardar(datos);
            return datos;
        }
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            Object objeto = entrada.readObject();
            if (!(objeto instanceof DatosFidness)) {
                throw new IOException("El archivo no contiene datos de Fidness.");
            }
            return (DatosFidness) objeto;
        }
    }

    public void guardar(DatosFidness datos) throws IOException {
        File carpeta = archivo.getAbsoluteFile().getParentFile();
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta de datos.");
        }
        // Primero se escribe una copia; solo se reemplaza el original si termina bien.
        File temporal = new File(archivo.getAbsolutePath() + ".tmp");
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(temporal))) {
            salida.writeObject(datos);
        }
        Files.move(temporal.toPath(), archivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
