package bandwurm;

import java.util.Random;

public class Tutor extends Thread {

    private int aufgabe;
    private String name;
    private ResourcePool<Klausur> leftBuffer;
    private ResourcePool<Klausur> rightBuffer;

    public Tutor(String name, int aufgabe, ResourcePool<Klausur> leftBuffer, ResourcePool<Klausur> rightBuffer) {
        if (aufgabe < 0 || aufgabe >= 8) {
            throw new IllegalArgumentException("Index muss im Bereich [0,7] sein.");
        }
        this.name = name;
        this.aufgabe = aufgabe;
        this.leftBuffer = leftBuffer;
        this.rightBuffer = rightBuffer;
    }

    public void run() {

        System.out.println("[INFO] [Tutor] Thread " + this.name + " gestartet.");

        String antwort = null;
        Klausur klausur = null;
        int count = 0;
        while ((klausur = this.leftBuffer.getResource()) != null) {
            antwort = klausur.getAntwort(this.aufgabe);
            int punkte = Korrekturschema.punkte(this.aufgabe, antwort);
            klausur.setPunkte(this.aufgabe, punkte);
            this.rightBuffer.addResource(klausur);
            count ++;
        }

        System.out.println("[INFO] [Tutor] [ " + this.name + " ] Fertig mit Aufgabe-" + this.aufgabe + " processed: " + count);
    }
}
