package org.examples.deadlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockExampleFixedTryLock {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        DeadlockExampleFixedTryLock example = new DeadlockExampleFixedTryLock();
        example.runTest();
    }

    public void runTest() {
        Thread t1 = new Thread(this::task1);
        Thread t2 = new Thread(this::task2);

        t1.start();
        t2.start();
    }

    public void task1() {
        boolean lock1Acquired = false;
        boolean lock2Acquired = false;

        try {
            while (!lock1Acquired || !lock2Acquired) {
                lock1Acquired = lock1.tryLock();
                lock2Acquired = lock2.tryLock();

                if (lock1Acquired && lock2Acquired) {
                    System.out.println("Thread 1: Acquired both locks");
                }
            }
        } finally {
            if (lock1Acquired) {
                lock1.unlock();
            }
            if (lock2Acquired) {
                lock2.unlock();
            }
        }
    }

    public void task2() {
        boolean lock1Acquired = false;
        boolean lock2Acquired = false;

        try {
            while (!lock1Acquired || !lock2Acquired) {
                lock1Acquired = lock1.tryLock();
                lock2Acquired = lock2.tryLock();

                if (lock1Acquired && lock2Acquired) {
                    System.out.println("Thread 2: Acquired both locks");
                }
            }
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

/*
Expected Output:
- Threads will acquire both locks if available, or back off and try again, avoiding deadlock.
*/
