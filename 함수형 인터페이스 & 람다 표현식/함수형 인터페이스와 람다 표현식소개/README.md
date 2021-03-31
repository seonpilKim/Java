# 함수형 인터페이스와 람다 표현식 소개

## 함수형 인터페이스 (Functional Interface)
- `추상 메소드`를 딱 `하나만` 가지고 있는 인터페이스
    - static 메소드나 default 메소드, 상수 등은 제약 없음.
    - `SAM (Single Abstract Method) 인터페이스` 라고도 불린다.
- `@FunctionalInterface` annotation을 가지고 있는 인터페이스
```java
@FunctionalInterface
public interface RunSomething {

    void doIt();    // 추상 메소드 1개 : Functional interface

    static void printName(){
        System.out.println("SeonPil");
    }

    default void printAge(){
        System.out.println("25");
    }
}
```
## 람다 표현식 (Lambda Expressions)
- `함수형 인터페이스의 instance`를 만드는 방법으로 쓰일 수 있다.
- 코드를 효과적으로 줄일 수 있다.
- 메소드 parameter, return type, variable로 만들어 사용할 수도 있다.
```java
public static void main(String[] args) {
    // 익명 내부 클래스(anonymous inner class), Java8 이전사용 방식
    RunSomething runSomething = new RunSomething() {
        @Override
        public void doIt() {
            System.out.println("Hello");
        }
    };
    
    // 람다 표현식 Java8 이후 사용방식
    RunSomething runSomething = () -> System.outprintln("Hello");
    
}
```

## 자바의 함수형 프로그래밍
- 함수를 `First class object`로 사용할 수 있다.
    - `일급 객체(First class object)` : 다른 객체들에 일반적으로 적용 가능한 연산을 모두 지원하는 객체
        - `변수`에 할당 가능
        ```java
        RunSomething runSomething = () -> System.outprintln("Hello");
        ```
        - 다른 `함수를 인자로` 전달 받을 수 있음
        ```java
        interface Callable {
            public void call(int param);
        }

        class Test implements Callable {
            public void call(int param) {
                System.out.println( param );
            }
        }

        public class HelloWorld{
            public static void invoke(Callable callable, int param){
                callable.call(param);
            }
            public static void main(String []args){
                Callable cmd = new Test();
                invoke(cmd, 10);
            }
        }
        ```
        - 다른 `함수의 결과로서 반환`될 수 있음.
        ```java
        static int add_self(int n) {
            return n + n;
        }
        static int add(int n){
            return add_self(n);
        }
        ```
        - 고차함수를 만들 수 있다.
        - 콜 백(callback)을 사용할 수 있다.
- `순수 함수 (Pure function)`
    - Side effect가 없다. (함수 밖에 존재하는 값을 변경하지 않음)
    - 상태가 없다. (함수 밖에 존재하는 값을 사용하지 않음)
- `고차 함수 (Higher-order function)`
    - 함수를 전달인자(argument) 또는 매개변수(parameter)로 받거나 함수를 return하는 함수를 말한다.
- `콜 백 함수 (Callback function)`
    - 함수를 전달인자(Argument)로 받는 함수이다.
- `불변성`
    - 같은 입력값에는 항상 같은 출력값만 나온다.