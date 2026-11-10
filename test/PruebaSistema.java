import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

// Prueba sin librerías adicionales. Usa una carpeta temporal, no los datos reales.
public class PruebaSistema {
    private static int comprobaciones = 0;

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
        comprobaciones++;
    }

    public static void main(String[] args) throws Exception {
        File carpeta = Files.createTempDirectory("prueba-fidness-").toFile();
        File datos = new File(carpeta, "fidness.dat");
        ArchivoDatos archivo = new ArchivoDatos(datos);
        SistemaFidness sistema = new SistemaFidness(archivo);
        Usuario eduardo = sistema.iniciarSesion(" EDUARDO ", "clave123");
        Usuario admin = sistema.iniciarSesion("admin", "admin123");
        comprobar(eduardo.getRol().equals("Cliente"), "Rol de cliente");
        comprobar(admin.getRol().equals("Administrador"), "Polimorfismo de administrador");
        try {
            sistema.iniciarSesion("eduardo", "incorrecta");
            throw new AssertionError("Aceptó una contraseña incorrecta");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        try {
            sistema.iniciarSesion(" ", "");
            throw new AssertionError("Aceptó campos vacíos");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        try {
            sistema.registrarUsuario("nuevo", "123", "123");
            throw new AssertionError("Aceptó una contraseña corta");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        try {
            sistema.registrarUsuario("nuevo", "123456", "654321");
            throw new AssertionError("Aceptó confirmación distinta");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        sistema.registrarUsuario("nuevo", "123456", "123456");
        Usuario nuevo = sistema.iniciarSesion("nuevo", "123456");
        try {
            sistema.registrarUsuario("NUEVO", "123456", "123456");
            throw new AssertionError("Aceptó un usuario repetido");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        comprobar(sistema.buscarEjercicios("Todos", "").size() == 7, "Catálogo completo");
        comprobar(sistema.buscarEjercicios("Pierna", "").size() == 2, "Filtro de tipo");
        comprobar(sistema.buscarEjercicios("Todos", "SENTADILLA").size() == 1, "Búsqueda por nombre");
        comprobar(sistema.buscarEjercicios("Brazo", "sentadilla").isEmpty(), "Combinar filtros");
        comprobar(sistema.buscarEjercicios("Todos", "inexistente").isEmpty(), "Búsqueda sin resultados");
        ArrayList<Ejercicio> ejercicios = sistema.buscarEjercicios("Todos", "");
        comprobar(ejercicios.get(0).obtenerDetalle().contains("4."), "Pasos numerados");
        try {
            new ItemRutina(ejercicios.get(0), 0, 10);
            throw new AssertionError("Aceptó cero series");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        try {
            new ItemRutina(ejercicios.get(0), 11, 10);
            throw new AssertionError("Aceptó más de diez series");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        try {
            new ItemRutina(ejercicios.get(0), 3, 0);
            throw new AssertionError("Aceptó cero repeticiones");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        try {
            new ItemRutina(ejercicios.get(0), 3, 51);
            throw new AssertionError("Aceptó más de cincuenta repeticiones");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        new ItemRutina(ejercicios.get(0), 1, 1);
        new ItemRutina(ejercicios.get(0), 10, 50);
        comprobaciones++;
        try {
            new Rutina("  ", eduardo.getNombre());
            throw new AssertionError("Aceptó nombre de rutina vacío");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        Rutina rutina = new Rutina("Pierna lunes", eduardo.getNombre());
        try {
            sistema.guardarRutina(eduardo, rutina);
            throw new AssertionError("Aceptó rutina vacía");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        rutina.agregarItem(new ItemRutina(ejercicios.get(0), 4, 10));
        try {
            rutina.agregarItem(new ItemRutina(ejercicios.get(0), 3, 12));
            throw new AssertionError("Aceptó ejercicio repetido");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        rutina.agregarItem(new ItemRutina(ejercicios.get(1), 3, 12));
        rutina.moverItem(1, 0);
        comprobar(rutina.getItems().get(0).getEjercicio().getNombre().equals("Zancadas"), "Subir ejercicio");
        rutina.moverItem(0, 1);
        comprobar(rutina.getItems().get(0).getEjercicio().getNombre().equals("Sentadilla libre"), "Bajar ejercicio");
        rutina.quitarItem(1);
        comprobar(rutina.getItems().size() == 1, "Quitar ejercicio");
        rutina.agregarItem(new ItemRutina(ejercicios.get(1), 3, 12));
        sistema.guardarRutina(eduardo, rutina);
        try {
            sistema.guardarRutina(eduardo, rutina);
            throw new AssertionError("Aceptó nombre de rutina duplicado");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        comprobar(sistema.obtenerRutinas(nuevo).isEmpty(), "Separación de rutinas por usuario");
        try {
            sistema.eliminarRutina(nuevo, rutina);
            throw new AssertionError("Eliminó una rutina ajena");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        try {
            sistema.guardarRutina(nuevo, rutina);
            throw new AssertionError("Guardó una rutina ajena");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        Rutina otra = new Rutina("Pierna lunes", nuevo.getNombre());
        otra.agregarItem(new ItemRutina(ejercicios.get(0), 1, 1));
        sistema.guardarRutina(nuevo, otra);
        comprobar(sistema.obtenerRutinas(nuevo).size() == 1, "Mismo nombre en otro usuario");
        SistemaFidness reabierto = new SistemaFidness(new ArchivoDatos(datos));
        Usuario usuarioRecuperado = reabierto.iniciarSesion("nuevo", "123456");
        comprobar(usuarioRecuperado.getRol().equals("Cliente"), "Cuenta recuperada al reiniciar");
        Rutina recuperada = reabierto.obtenerRutinas(eduardo).get(0);
        comprobar(recuperada.getItems().size() == 2, "Rutina recuperada al reiniciar");
        comprobar(recuperada.getItems().get(0).getSeries() == 4, "Series recuperadas");
        File exportado = new File(carpeta, "rutina.txt");
        ExportadorRutina.exportar(recuperada, exportado);
        String texto = new String(Files.readAllBytes(exportado.toPath()), "UTF-8");
        comprobar(texto.contains("RUTINA: Pierna lunes") && texto.contains("Fecha:")
                && texto.contains("4 series x 10 repeticiones") && texto.contains("1. Apoye")
                && texto.contains("Total de ejercicios: 2"), "Contenido de exportación");
        reabierto.eliminarRutina(eduardo, recuperada);
        SistemaFidness finalizado = new SistemaFidness(new ArchivoDatos(datos));
        comprobar(finalizado.obtenerRutinas(eduardo).isEmpty(), "Eliminación persistente");
        comprobar(finalizado.obtenerRutinas(nuevo).size() == 1, "Se conserva la rutina del otro usuario");
        File corrupto = new File(carpeta, "corrupto.dat");
        Files.write(corrupto.toPath(), new byte[]{1, 2, 3});
        try {
            new ArchivoDatos(corrupto).cargar();
            throw new AssertionError("Aceptó archivo corrupto");
        } catch (java.io.IOException e) { comprobaciones++; }
        comprobar(Files.readAllBytes(corrupto.toPath()).length == 3, "No sobrescribe archivo corrupto");
        // Provocar un error real de escritura para comprobar que se revierte la operación.
        File bloqueado = new File(datos.getAbsolutePath() + ".tmp");
        comprobar(bloqueado.mkdir(), "Preparar error de escritura");
        try {
            finalizado.registrarUsuario("fallido", "123456", "123456");
            throw new AssertionError("Ignoró error de escritura");
        } catch (java.io.IOException e) { comprobaciones++; }
        try {
            finalizado.iniciarSesion("fallido", "123456");
            throw new AssertionError("Conservó una cuenta que no pudo guardar");
        } catch (DatosInvalidosException e) { comprobaciones++; }
        bloqueado.delete();
        for (File file : carpeta.listFiles()) { file.delete(); }
        carpeta.delete();
        System.out.println("CORRECTO: " + comprobaciones + " comprobaciones superadas.");
    }
}
