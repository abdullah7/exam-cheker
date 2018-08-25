package bandwurm;

public class Uebungsleitung {

    private ResourcePool<Klausur> leftBuffer;
    private ResourcePool<Klausur> rightBuffer;

    public Uebungsleitung(ResourcePool<Klausur> leftBuffer, ResourcePool<Klausur> rightBuffer) {
        this.leftBuffer = leftBuffer;
        this.rightBuffer = rightBuffer;
    }

    public void run() throws InterruptedException {
        Zweitkorrektur[] threads = new Zweitkorrektur[]{
                new Zweitkorrektur("Zweitkorrektur-1", this.leftBuffer, this.rightBuffer),
                new Zweitkorrektur("Zweitkorrektur-2", this.leftBuffer, this.rightBuffer)
        };

        startAllThreads(threads);
        joinAllThreads(threads);
    }

    private void startAllThreads(Zweitkorrektur[] threads) {
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }

    private void joinAllThreads(Zweitkorrektur[] threads) {
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
