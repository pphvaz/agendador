package ai.agendi.agendador.utils;

import java.util.Random;

public class TestUtilities {

    public String generateRandomName() {
        String[] firstNames = {"Pedro", "Maria", "João", "Ana", "Carlos", "Julia", "Lucas", "Fernanda", "Gabriel", "Beatriz"};
        String[] lastNames = {"Silva", "Santos", "Oliveira", "Souza", "Pereira", "Costa", "Almeida", "Ferreira", "Ribeiro", "Lima"};

        Random random = new Random();

        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];

        return firstName + " " + lastName;
    }
}
