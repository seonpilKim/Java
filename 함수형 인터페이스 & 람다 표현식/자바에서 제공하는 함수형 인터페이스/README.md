# 자바에서 제공하는 함수형 인터페이스

## [java.lang.function](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html) 패키지

### Function<T, R>
- `T 타입의 매개변수`를 받아서 `R 타입을 반환`하는 함수 인터페이스
    - 추상 메소드 : `R apply(T t)`
    ```java
    Function<Integer, Integer> plus_10 = n -> n + 10;
    System.out.println(plus_10.apply(2)); // 12 출력
    ```
    - default 메소드
        1. `andThen`
            - 아래의 예시로 설명하자면, plus10 함수의 결과값을 multiply2 함수의 매개변수로 전달한다.
            ```java
            Function<Integer, Integer> plus_10 = n -> n + 10;
            Function<Integer, Integer> multiply2 = (n) -> n * 2;
            System.out.println(plus10.andThen(multiply2).apply(2)); // 24 출력
            ```
        2. `compose`
            - 아래의 예시로 설명하자면, multiply2 함수의 결과값을 plus10    함수의 매개변수로 전달한다.
            ```java
            Function<Integer, Integer> plus_10 = n -> n + 10;
            Function<Integer, Integer> multiply2 = (n) -> n * 2;
            Function<Integer, Integer> multiply2AndPlus10 = plus10. compose(multiply2);
            System.out.println(multiply2AndPlus10.apply(2)); // 14 출력
            ```
- `BiFunction<T, U, R>` : 두 개의 값(T, U)를 받아서 R 타입을 반환하는 함수 인터페이스

### Consumer<T>
- `T 타입의 매개변수`를 받아서 아무 값도 `반환하지 않는` 함수 인터페이스
    - 추상 메소드 : `accept(T t)`
    ```java
    Consumer<Integer> printT = i -> System.out.println(i);
    printT.accept(10);  // 10 출력
    ```
    - default 메소드 : `andThen`

### Supplier<T>
- `매개변수 없이` `T타입을 반환`하는 함수 인터페이스
    - 추상 메소드 : `T get()`
    ```java
    Supplier<Integer> get10 = () -> 10;
    System.out.println(get10.get()); // 10 출력
    ```
### Predicate<T>
- `T 타입의 매개변수`를 받아서 `boolean을 반환`하는 함수 인터페이스
    - 추상 메소드 : `boolean test(T t)`
    ```java
    Predicate<Integer> isEven = i -> i % 2 == 0;
    System.out.println(isEven.test(3)); // false 출력
    ```
    - 함수 조합용 메소드 : `and`, `or`, `negate`

### Operator<T>
- `UnaryOperator<T>` : `Function<T, R>`의 특수한 형태로, `T 타입의 매개변수`를 받아서 `동일 타입으로 반환`하는 함수 인터페이스
```java
UnaryOperator<Integer> plus_10 = n -> n + 10;
System.out.println(plus_10.apply(2)); // 12 출력
```
- `BinaryOperator<T>` : `BiFunction<T, U, R>`의 특수한 형태로, `동일한 타입의 T 매개변수`를 받아서 `동일 타입으로 반환`하는 함수 인터페이스