import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
    private int id;
    private Semaphore[] forks;
    private CountDownLatch latch;

    public Philosopher(int id, Semaphore[] forks, CountDownLatch latch) {
        this.id = id;
        this.forks = forks;
        this.latch = latch;
    }

    private void lock() throws InterruptedException {
        if (id % 2 == 0) {
            forks[id].acquire();
            forks[(id + 1) % 5].acquire();
            System.out.println("P: " + id + " took right");
            System.out.println("P: " + id + " took left");
        } else {
            forks[(id + 1) % 5].acquire();
            forks[id].acquire();
            System.out.println("P: " + id + " took left");
            System.out.println("P: " + id + " took right");
        }
    }

    private void unlock() {
        forks[id].release();
        forks[(id + 1) % 5].release();
        System.out.println("P: " + id + " put right");
        System.out.println("P: " + id + " put left");
    }

    @Override
    public void run() {
        System.out.println("P: " + id + " is thinking");

        try {
            lock();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("P: " + id + " is eating");
        unlock();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        latch.countDown();
    }
}