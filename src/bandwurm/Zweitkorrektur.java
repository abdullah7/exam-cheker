package bandwurm;

public class Zweitkorrektur extends Thread {

    private String name;
    private ResourcePool<Klausur> leftBuffer;
    private ResourcePool<Klausur> rightBuffer;

    private int counter;

    public Zweitkorrektur(String name,
                          ResourcePool<Klausur> leftBuffer,
                          ResourcePool<Klausur> rightBuffer) {
        this.name = name;
        this.leftBuffer = leftBuffer;
        this.rightBuffer = rightBuffer;
        this.counter = 0;
    }

    public void run() {

        System.out.println("[INFO] [Zweitkorrektur] Thread " + this.name + " gestartet.");

        Klausur klausur = null;
        while ((klausur = this.leftBuffer.getResource()) != null) {

            if (this.counter != 9) {
                for (int i = 0; i < 8; i++) {
                    int korrektur = klausur.getPunkte()[i];
                    if (korrektur <= 2) {
                        korrektur = korrektur + 1;
                    } else {
                        korrektur = korrektur - 1;
                    }
                    klausur.setZweitkorrektur(i, korrektur);
                }
            }
            this.counter = (this.counter + 1) % 10;
            this.rightBuffer.addResource(klausur);
        }

        System.out.println("[INFO] [Zweitkorrektur] Thread " + this.name + " beendet.");
    }
}
