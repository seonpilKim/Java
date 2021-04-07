package java8to11;

import java.util.concurrent.*;

public class App {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete("seonpil"); // future의 기본 값을 "seonpil" 으로 설정.

        String s1 = future.get(); // future의 작업이 끝날 때 까지 blocking
        System.out.println(s1);

        // static factory method
        System.out.println("static factory method------------");
        CompletableFuture<String> seonpil = CompletableFuture.completedFuture("seonpil");
        System.out.println(seonpil.get());

        // return이 없는 작업
        System.out.println("return이 없는 작업---------------");
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
        });
        voidCompletableFuture.get();

        // return이 있는 작업
        System.out.println("return이 있는 작업--------------");
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });
        System.out.println(stringCompletableFuture.get());

        // asynchronous callback
        System.out.println("asynchronous callback------------");
        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenApply(s -> {
            System.out.println(Thread.currentThread().getName());
            return s.toUpperCase();
        });
        System.out.println(stringCompletableFuture1.get());

        // return이 없는 callback
        System.out.println("return이 없는 callback-------------");
        CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenAccept(s -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(s.toUpperCase());
        });
        voidCompletableFuture1.get();

        // 결과값을 받지도 return하지도 않고, callback만
        System.out.println("결과값을 받지도 return하지도 않고, callback만-----------");
        CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).thenRun(() -> {
            System.out.println(Thread.currentThread().getName());
        });
        voidCompletableFuture2.get();

        /*
        * CompletableFuture는 Executors를 사용하지 않아도, 내부적으로 Fork-Join pool에 있는 common pool을 사용하게 된다.
        * 하지만, 원한다면 얼마든지 Executors를 만들어서 runAsync, supplyAsync의 두번째 parameter에 넣어 사용할 수도 있다.
        * 또한, thenRun, thenApply, thenAccept 뒤에 Async를 붙여서 두 번째 parameter에 Executors를 넣어 사용할 수도 있다.
        * */
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CompletableFuture<Void> voidCompletableFuture3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }, executorService).thenRunAsync(()->{
            System.out.println(Thread.currentThread().getName());
        }, executorService);
        voidCompletableFuture3.get();
        executorService.shutdown();
    }
}
