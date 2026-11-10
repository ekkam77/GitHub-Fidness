import java.util.ArrayList;

public class CatalogoInicial {
    public static ArrayList<Ejercicio> crear() {
        ArrayList<Ejercicio> ejercicios = new ArrayList<Ejercicio>();
        ejercicios.add(new Ejercicio("Sentadilla libre", "Pierna",
                "Trabaja los músculos de las piernas.", "Barra y discos", "Intermedio",
                "1. Apoye la barra en la parte alta de la espalda.\n"
                + "2. Separe los pies al ancho de los hombros.\n"
                + "3. Baje flexionando las rodillas de forma controlada.\n"
                + "4. Suba empujando con los talones.", 4, 10));
        ejercicios.add(new Ejercicio("Zancadas", "Pierna",
                "Ejercicio alternado para piernas y glúteos.", "Sin equipo", "Principiante",
                "1. Colóquese de pie.\n2. Dé un paso hacia delante.\n"
                + "3. Flexione ambas rodillas.\n4. Regrese y cambie de pierna.", 3, 12));
        ejercicios.add(new Ejercicio("Remo con barra", "Espalda",
                "Trabaja los músculos de la espalda.", "Barra", "Intermedio",
                "1. Sujete la barra y flexione ligeramente las rodillas.\n"
                + "2. Incline el torso manteniendo la espalda recta.\n"
                + "3. Acerque la barra al abdomen.\n4. Baje de forma controlada.", 3, 10));
        ejercicios.add(new Ejercicio("Curl de bíceps", "Brazo",
                "Ejercicio de flexión de brazos para bíceps.", "Mancuernas", "Principiante",
                "1. Sujete las mancuernas a los lados.\n2. Mantenga los codos cerca del cuerpo.\n"
                + "3. Flexione los brazos.\n4. Baje lentamente.", 3, 12));
        ejercicios.add(new Ejercicio("Press de banca", "Pecho",
                "Trabaja pecho y tríceps.", "Banco y barra", "Intermedio",
                "1. Acuéstese en el banco con los pies apoyados.\n"
                + "2. Sujete la barra con ayuda al retirarla.\n"
                + "3. Bájela de forma controlada hacia el pecho.\n4. Empuje hacia arriba.", 3, 10));
        ejercicios.add(new Ejercicio("Elevaciones laterales", "Hombro",
                "Trabaja la parte lateral de los hombros.", "Mancuernas", "Principiante",
                "1. Colóquese de pie con una mancuerna en cada mano.\n"
                + "2. Eleve los brazos hacia los lados hasta los hombros.\n"
                + "3. Mantenga los codos ligeramente flexionados.\n4. Baje despacio.", 3, 12));
        ejercicios.add(new Ejercicio("Abdominal corto", "Abdomen",
                "Ejercicio de contracción abdominal.", "Colchoneta", "Principiante",
                "1. Acuéstese con las rodillas flexionadas.\n2. Apoye los pies en el suelo.\n"
                + "3. Eleve ligeramente los hombros sin tirar del cuello.\n4. Regrese despacio.", 3, 15));
        return ejercicios;
    }
}
