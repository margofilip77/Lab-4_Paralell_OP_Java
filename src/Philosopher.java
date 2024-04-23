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
        int leftFork = id;
        int rightFork = (id + 1) % 5;

        // Блокуємо доступ до виделок
        while (true) {
            // Спробуємо забрати обидві виделки одночасно
            if (forks[leftFork].tryAcquire() && forks[rightFork].tryAcquire()) {
                System.out.println("P: " + id + " took left and right");
                break; // Вийти з циклу, якщо обидві виделки забрані
            } else {
                // Звільнити виделку, якщо обидві не доступні одночасно
                if (forks[leftFork].availablePermits() == 0) {
                    forks[leftFork].release();
                }
                if (forks[rightFork].availablePermits() == 0) {
                    forks[rightFork].release();
                }
                Thread.sleep(100);
            }
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