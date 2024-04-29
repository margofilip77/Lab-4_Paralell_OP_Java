import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Semaphore[] forks = new Semaphore[5];
        Philosopher[] philosophers = new Philosopher[5];
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            forks[i] = new Semaphore(1);
        }

        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher(i, forks, latch);
            philosophers[i].start();
        }

        latch.await();

        System.out.println("success");
    }
}