package com.zou.basejava;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BlockingQueue实现生产消费模型
 */
public class BlockingQueueP2C {

    public static void main(String[] args) {
        //queue capacity>3 block
        HamburgerFactory factory = new HamburgerFactory(100);
        new Thread(new Producer(factory)).start();
        new Thread(new Consumer(factory)).start();
    }


    static class HamburgerFactory {
        private BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        private int total;

        public HamburgerFactory(int total) {
            this.total = total;
        }

        public void produce() {
            System.out.println("今天只做" + total + "个汉堡");
            for (int i = 0; i < total; i++) {
                produce(i);
            }
            produce("over");
        }

        public void consumer() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String eat;

            try {
                while (!(eat = queue.take()).equals("over")) {
                    Thread.sleep(1000);
                    System.out.println(eat + "吃完了");
                }
                System.out.println("打烊了");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        private void produce(int i) {
            try {
                queue.put("汉堡" + i);
                System.out.println("汉堡" + i + "做好了-------");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void produce(String str) {
            try {
                queue.put(str);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    static class Producer implements Runnable {
        private HamburgerFactory factory;

        public Producer(HamburgerFactory factory) {
            this.factory = factory;
        }

        @Override
        public void run() {
            factory.produce();
        }
    }

    static class Consumer implements Runnable {
        private HamburgerFactory factory;
        public Consumer(HamburgerFactory factory) {
            this.factory = factory;
        }
        @Override
        public void run() {
            factory.consumer();
        }
    }

}
