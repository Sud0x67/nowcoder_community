package com.bobo.community;

import org.springframework.jmx.export.naming.IdentityNamingStrategy;

import java.util.concurrent.*;

public class ThreadPool {
    static  class  TaskF<T> implements Callable<T>{
        @Override
        public T call() throws Exception {
            System.out.println("Thread-" + Thread.currentThread().getId() + " : hello world");
            return (T) Integer.valueOf(1);
        }
    }
    static class Task implements Runnable{
        int a=0 ;

        @Override
        public void run() {
            a++;
            System.out.println(a);
            System.out.println("Thread-" + Thread.currentThread().getId() + " : hello world");
        }
    }
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(5);
        Task task = new Task();
        TaskF<Number> a = new TaskF<>();
        es.submit(task);
        es.submit(task);
        es.submit(task);
        es.submit(task);
        es.submit(task);
        es.submit(task);
        Future<Number> future = es.submit(a);
        try {
            System.out.println(future.get().getClass());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }
}
