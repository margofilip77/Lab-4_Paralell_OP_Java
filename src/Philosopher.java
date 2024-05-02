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
        if (id % 2 == 0) { // Якщо ідентифікатор парний
            forks[id].release(); // Парний філософ звільняє праву вилку першою
            forks[(id + 1) % 5].release(); // Потім ліву
            System.out.println("P: " + id + " put right");
            System.out.println("P: " + id + " put left");
        } else { // Якщо ідентифікатор непарний
            forks[(id + 1) % 5].release(); // Непарний філософ звільняє ліву вилку першою
            forks[id].release(); // Потім праву
            System.out.println("P: " + id + " put left");
            System.out.println("P: " + id + " put right");
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
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
        }


        latch.countDown();
    }
}