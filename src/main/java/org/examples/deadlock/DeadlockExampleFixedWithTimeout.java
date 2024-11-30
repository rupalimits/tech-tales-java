package org.examples.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockExampleFixedWithTimeout {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        DeadlockExampleFixedWithTimeout example = new DeadlockExampleFixedWithTimeout();
        example.runTest();
    }

    public void runTest() {
        Thread t1 = new Thread(this::task1);
        Thread t2 = new Thread(this::task2);

        t1.start();
        t2.start();
    }

    public void task1() {
        while (true) {
            boolean lock1Acquired = false;
            boolean lock2Acquired = false;

            try {
                lock1Acquired = lock1.tryLock(1, TimeUnit.SECONDS);
                if (lock1Acquired) {
                    System.out.println("Thread 1: Holding lock 1...");
                    Thread.sleep(50);

                    lock2Acquired = lock2.tryLock(1, TimeUnit.SECONDS);
                    if (lock2Acquired) {
                        System.out.println("Thread 1: Acquired lock 2");
                        // Perform the required task
                        break; // Successfully acquired both locks, exit loop
                    } else {
                        System.out.println("Thread 1: Could not acquire lock 2, releasing lock 1");
                    }
                } else {
                    System.out.println("Thread 1: Could not acquire lock 1");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock1Acquired) {
                    lock1.unlock();
                }
                if (lock2Acquired) {
                    lock2.unlock();
                }
            }
        }
    }

    public void task2() {
        while (true) {
            boolean lock1Acquired = false;
            boolean lock2Acquired = false;

            try {
                lock1Acquired = lock1.tryLock(1, TimeUnit.SECONDS); // allowing threads to attempt locking for a specified period
                if (lock1Acquired) {
                    System.out.println("Thread 2: Holding lock 1...");
                    Thread.sleep(50);

                    lock2Acquired = lock2.tryLock(1, TimeUnit.SECONDS);
                    if (lock2Acquired) {
                        System.out.println("Thread 2: Acquired lock 2");
                        // Perform the required task
                        break; // Successfully acquired both locks, exit loop
                    } else {
                        System.out.println("Thread 2: Could not acquire lock 2, releasing lock 1");
                    }
                } else {
                    System.out.println("Thread 2: Could not acquire lock 1");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock1Acquired) {
                    lock1.unlock();
                }
                if (lock2Acquired) {
                    lock2.unlock();
                }
            }
        }
    }
}

/*
Expected Output:
- Threads will attempt to acquire both locks with a timeout of 1 second. If they fail to acquire both, they release any held locks and try again, ensuring that progress is made and deadlock is avoided.
*/
