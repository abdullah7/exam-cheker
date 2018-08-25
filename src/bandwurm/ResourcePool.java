package bandwurm;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class ResourcePool<T> {
    private  int maxResources;
    private final Semaphore sem;
    private final Queue<T> resources;


    public ResourcePool(int maxResources) {
        this.maxResources = maxResources;
        this.sem = new Semaphore(1);
        this.resources = new ConcurrentLinkedQueue<>();
    }

    public synchronized  T getResource() {
        T retVal = null;
        try {
            sem.acquire();
            retVal = resources.poll();
            sem.release();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        return retVal;
    }

    public synchronized void addResource(T res) {
        try {
            sem.acquire();
            resources.add(res);
            sem.release();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}
