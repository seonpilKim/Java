# 메소드 레퍼런스 (Method Reference)
- 람다가 하는 일이 기존 메소드 또는 생성자를 호출하는 거라면, `메소드 레퍼런스`를 사용해서 매우 간결하게 표현할 수 있다.

- 메소드 참조하는 방법
```java
class Greeting{
    private String name;

    public Greeting(){}

    public Greeting(String name){
        this.name = name;
    }

    public String hello(String name){
        return "hello " + name;
    }

    public static String hi(String name){
        return "hi " + name;
    }
}
```
1. static 메소드 참조 : `타입::static 메소드`
```java
UnaryOperator<String> hi2 = Greeting::hi;
hi2.apply("seonpil");   // "hi seonpil"
```
2. 특정 객체의 instance 메소드 참조 : `객체 reference::instance 메소드`
```java
Greeting greeting = new Greeting();
UnaryOperator<String> hello = greeting::hello;
hello.apply("seonpil"); // "hello seonpil"
```
3. 임의 객체의 instance 메소드 참조 : `타입::instance 메소드`
```java
String[] names = {"seonpil", "whiteship", "toby"};
Arrays.sort(names, new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2);
    }
});
// Comparator도 함수형 인터페이스이므로 람다로 사용할 수 있으며, 메소드 레퍼런스로 사용할 수 있다.
Arrays.sort(names, (o1, o2) -> o1.compareTo(o2));
// 참고로, compareToIgnoreCase는 대소문자를 구분하지 않고 비교하는 함수이다.
Arrays.sort(names, String::compareToIgnoreCase); // 임의의 객체의 인스턴스 메소드 참조
System.out.println(Arrays.toString(names)); // [seonpil, toby, whiteship]
```
4. 생성자 참조
```java
Supplier<Greeting> newGreeting = Greeting::new; // default 생성자 참조

Function<String, Greeting> seonpilGreeting = Greeting::new; // 문자열을 받는 생성자를 참조
Greeting seonpil = seonpilGreeting.apply("seonpil");
```