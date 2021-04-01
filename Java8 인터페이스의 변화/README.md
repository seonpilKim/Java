# 인터페이스 기본(default) 메소드와 스태틱(static) 메소드
```java
public interface DefaultFoo {

    String getName();
    void printName();

    /*
    * @implSpec
    * 이 구현체는 getName()으로 가져온 문자열을 대문자로 바꾸어 출력한다.
    * */
    default void printNameUpperCase(){
        System.out.println(getName().toUpperCase());
    }

    static void printAnything(){
        System.out.println("Foo");
    }
}
```
## 기본 메소드 (Default Methods)
- 인터페이스에 메소드 선언이 아닌 `구현체를 제공`하는 방법
- `목적`
    - `하위 호환성`과 `유연성`을 위함
    - 인터페이스에 변경이 일어나면, 같이 변경해줘야 하는 구현체가 훨씬 많아진다.
    - 그래서, default method로 구현체를 제공해주면, 기존 인터페이스를 구현한 클래스에서 새로 추상메소드를 구현해야하는 불편함을 해소시킬 수 있다.
    - 즉, `해당 인터페이스를 구현한 클래스를 깨트리지 않고, 새 기능을 추가`할 수 있다.
- 기본 메소드는 구현체가 모르게 추가된 기능으로 그만큼 `리스크`가 있다.
    - 컴파일 에러는 아니지만, 구현체에 따라 런타임 에러가 발생할 수 있다.
        > ex) 예제에서 getName()이 null인 경우, 런타임 에러 발생
    - 반드시 문서화 할 것. (`@implSpec` 자바독 태그 사용)
    - `Object`가 제공하는 기능(toString, equals, ...)는 기본 메소드로 제공할 수 없다.
        - `구현체가 재정의`하는 것은 가능하다.
    - 본인이 수정할 수 있는 인터페이스에만 기본 메소드를 제공할 수 있다.
    - 인터페이스를 상속받는 인터페이스에서 다시 `추상 메소드로 변경`이 가능하다.
    ```java
    public interface Bar extends DefaultFoo{
        void printNameUpperCase();
    }
    ```
    - 인터페이스 `구현체가 기본메소드를 재정의` 할 수도 있다.

## 스태틱 메소드 (Static Methods)
- 해당 `타입 관련 헬퍼(helper) 메소드` 또는 `유틸리티 메소드`를 제공하고자 할 때, 인터페이스에 스태틱 메소드를 추가할 수 있다.

```java
public class Foo implements DefaultFoo{
    public static void main(String[] args) {
        DefaultFoo foo = new Foo("seonpil");
        foo.printName();
        foo.printNameUpperCase();   // default method 사용
        DefaultFoo.printAnything(); // static method 사용
    }

    String name;

    public Foo(String name){
        this.name = name;
    }

    // 인터페이스 구현체에서 기본(default) 메소드 재정의
    @Override
    public void printNameUpperCase() {
        System.out.println(getName().toUpperCase());
    }

    // 추상 메소드 구현
    @Override
    public void printName() {
        System.out.println(this.name);
    }

    // 추상 메소드 구현
    @Override
    public String getName() {
        return this.name;
    }
}
```

## 자바 8 API의 몇 가지 기본 메소드와 스태틱 메소드
- `Iterable` 기본 메소드
    - `forEach()`
    ```java
    // 람다
    names.forEach(s ->{
        System.out.println(s);
    });
    // 메소드 레퍼런스
    names.forEach(System.out::println);
    ```
    - `spliterator()`
    ```java
    Spliterator<String> spliterator = names.spliterator();
    Spliterator<String> spliterator1 = spliterator.trySplit();  // trySplit() : 반으로 나누는 메소드
    while(spliterator.tryAdvance(System.out::println));
    while(spliterator1.tryAdvance(System.out::println));
    ```
- `Collection` 기본 메소드
    - `stream()` / `parallelStream()`
    ```java
    long l = names.stream().map(String::toUpperCase)
                .filter(s -> s.startsWith("S"))
                .count();
    System.out.println(l);
    ```
    - `removeIf(Predicate)`
    ```java
    names.removeIf(s->s.startsWith("s"));
    names.forEach(System.out::println);
    ```
    - `spliterator()`
- `Comparator` 기본 메소드 및 스태틱 메소드
    - `reversed()`
    ```java
    Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;;
    names.sort(compareToIgnoreCase.reversed());
    names.forEach(System.out::println);
    ```
    - `thenComparing()`
    - `static reverseOrder() / naturalOrder()`
    - `static nullsFirst() / nullsLast()`
    - `static comparing()`
- etc.