package java8to11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class App2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 두 Futures 간에 의존성이 있는 경우
        
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });

        // CompletableFuture<String> stringCompletableFuture = hello.thenCompose(s -> getWorld(s));
        CompletableFuture<String> future = hello.thenCompose(App2::getWorld);
        System.out.println(future.get());
    }

    private static CompletableFuture<String> getWorld(String msg) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("World " + Thread.currentThread().getName());
            return msg + " World";
        });
    }
}
