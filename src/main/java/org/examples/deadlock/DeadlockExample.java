package org.examples.deadlock;

public class DeadlockExample {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public static void main(String[] args) {
        DeadlockExample example = new DeadlockExample();
        example.runTest();
    }

    public void runTest() {
        Thread t1 = new Thread(this::task1);
        Thread t2 = new Thread(this::task2);

        t1.start();
        t2.start();
    }

    public void task1() {
        synchronized (lock1) {
            System.out.println("Thread 1: Holding lock 1...");
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            System.out.println("Thread 1: Waiting for lock 2...");
            synchronized (lock2) {
                System.out.println("Thread 1: Acquired lock 2");
            }
        }
    }

    public void task2() {
        synchronized (lock2) {
            System.out.println("Thread 2: Holding lock 2...");
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            System.out.println("Thread 2: Waiting for lock 1...");
            synchronized (lock1) {
                System.out.println("Thread 2: Acquired lock 1");
            }
        }
    }
}

/*
Expected Output:
- Thread 1 acquires lock1 and waits for lock2.
- Thread 2 acquires lock2 and waits for lock1.
- This results in a deadlock, and both threads are stuck.
*/
