package java8to11;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class App3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 서로 연관관계가 없는 경우
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            //System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            //System.out.println("World " + Thread.currentThread().getName());
            return "World";
        });



       /* CompletableFuture<String> future = hello.thenCombine(world, (h, w) -> h + " " + w);
        System.out.println(future.get());
        */



        // Futures가 두 개 이상인 경우
        /*CompletableFuture<Void> future1 = CompletableFuture.allOf(hello, world);
        System.out.println(future1.get());

        List<CompletableFuture<String>> futures = Arrays.asList(hello, world);
        CompletableFuture[] futuresArray = futures.toArray(new CompletableFuture[futures.size()]);
        CompletableFuture<List<String>> results = CompletableFuture.allOf(futuresArray)
                .thenApply(v -> futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()));
        results.get().forEach(System.out::println);*/



        /*CompletableFuture<Void> future = CompletableFuture.anyOf(hello, world)
                .thenAccept(System.out::println);
        future.get();*/



        /*boolean throwError = true;
        CompletableFuture<String> Hello = CompletableFuture.supplyAsync(() -> {
            if (throwError)
                throw new IllegalArgumentException();
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).exceptionally(ex -> {
            System.out.println(ex);
            return "Error!";
        });
        System.out.println(Hello.get());*/



        /*boolean throwError = true;
        CompletableFuture<String> Hello = CompletableFuture.supplyAsync(() -> {
            if (throwError)
                throw new IllegalArgumentException();
            System.out.println("Hello " + Thread.currentThread().getName());
            return "Hello";
        }).handle((result, ex)->{
            if(ex!= null){
                System.out.println(ex);
                return "Error!";
            }
            return result;
        });
        System.out.println(Hello.get());*/

    }
}
