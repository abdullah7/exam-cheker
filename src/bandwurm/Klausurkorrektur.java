package bandwurm;

import java.util.Arrays;

public class Klausurkorrektur {

    public static void main(String[] args) throws InterruptedException {
        ResourcePool<Klausur>[] resourcePools = generateBuffers();

        // creating tutors
        Tutor[] tutors = assignTutorsAufgaben(resourcePools);
        startAllTutors(tutors);
        joinAllTutors(tutors);

        // crating zweitkorrektur
        Uebungsleitung uebungsleitung = new Uebungsleitung(resourcePools[8], resourcePools[9]);
        uebungsleitung.run();

        // Punkte Addieren und Ausgeben
        punkteAddierenUndPrintieren(resourcePools);

        System.out.println("Korrektur der Info 1 Klausur beendet :)");
    }

    private static void punkteAddierenUndPrintieren(ResourcePool<Klausur>[] resourcePools) {

        System.out.println("===============================");
        System.out.println("   Ergebnis nach Korrekturen");
        System.out.println("===============================\n");

        Klausur klausur = null;
        while ((klausur = resourcePools[9].getResource()) != null) {
            int gesamtpunktzahl = Arrays.stream(klausur.getZweitkorrektur()).sum();
            klausur.setGesamtpunktzahl(gesamtpunktzahl);
            klausur.setNote(Korrekturschema.note(gesamtpunktzahl));
            System.out.println(klausur);
        }

        System.out.println("\n===============================");
        System.out.println("            Beendet            ");
        System.out.println("===============================\n");
    }

    private static void joinAllTutors(Tutor[] tutors) throws InterruptedException {
        for (int i = 0; i < tutors.length; i++) {
            tutors[i].join();
        }
    }

    private static void startAllTutors(Tutor[] tutors) {
        for (int i = 0; i < tutors.length; i++) {
            tutors[i].start();
        }
    }

    private static Tutor[] assignTutorsAufgaben(ResourcePool<Klausur>[] resourcePools) {
        Tutor[] tutors = new Tutor[60];
        int aufgabe = 0;
        for (int i = 0; i < 60; ) {

            // allocating 8 threads to odd aufgabe and 7 to even one
            // to allocate all 60 tutors (one tutor can only do one aufgabe)
            int totalAufgabeThreads = aufgabe % 2 == 0 ? 8 : 7;

            for (int j = 0; j < totalAufgabeThreads; j++) {
                tutors[i + j] = new Tutor("Tutor-" + (i + j + 1), aufgabe, resourcePools[aufgabe], resourcePools[aufgabe + 1]);
            }

            aufgabe += 1;
            i += totalAufgabeThreads;
        }
        return tutors;
    }

    private static ResourcePool<Klausur>[] generateBuffers() {
        ResourcePool<Klausur> firstPool = new ResourcePool<>(1700);
        for (int i = 0; i < 1700; i++) {
            firstPool.addResource(new Klausur());
        }

        ResourcePool<Klausur>[] resourcePools = new ResourcePool[10];
        for (int i = 1; i < 8; i++) {
            resourcePools[i] = new ResourcePool<Klausur>(50);
        }

        // replace 1st with filled buffer
        // these are the buffers with 1700 capacity
        resourcePools[0] = firstPool;
        resourcePools[8] = new ResourcePool<>(1700);
        resourcePools[9] = new ResourcePool<>(1700);

        return resourcePools;
    }
}
