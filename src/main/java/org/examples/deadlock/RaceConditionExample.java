package org.examples.deadlock;

public class RaceConditionExample {
    private int counter = 0;

    public static void main(String[] args) {
        RaceConditionExample example = new RaceConditionExample();
        example.runTest();
    }

    public void runTest() {
        Thread t1 = new Thread(this::increment);
        Thread t2 = new Thread(this::increment);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final Counter Value: " + counter);
    }

    public synchronized void increment() {
        for (int i = 0; i < 10000; i++) {
            counter++;  // This is not thread-safe, leading to a race condition
        }
    }
}
