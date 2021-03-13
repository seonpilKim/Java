# 인터페이스 & 추상 클래스
## 인터페이스 (interface) 란?
- 인터페이스는 `interface` 키워드를 사용하여 클래스를 선언하듯이 선언한다.
- 인터페이스는 `implements` 키워드를 사용하여 상속 받을 수 있다.
- 인터페이스는 다음 5종류의 멤버로 구성되며, `멤버 변수를 만들 수 없다.`
    > 상수, 추상 메소드, default 메소드, private 메소드, static 메소드
     - java 8 부터 일반(default) 메소드 사용이 가능하다.
     - java 9 부터 private, static 메소드 사용이 가능하다.
```java
interface PhoneInterface {
    public static final int TIMEOUT = 10000;    // 상수
    public abstract void sendCall();            // 추상 메소드
    public abstract void receiveCall();         // 추상 메소드
    public default void printLogo(){            // default 메소드
        System.out.println("** Phone **");
    }
}

class SamsungPhone implements PhoneInterface{
    // 인터페이스의 모든 추상메소드 구현
    @Override
    public void sendCall(){
        System.out.println("전화를 겁니다.");
    }

    @Override
    public void receiveCall(){
        System.out.println("전화를 받습니다.");
    }

    // 메소드 추가 작성
    public void flash(){
        System.out.println("손전등을 켭니다.");
    }
}
```
- 인터페이스는 `인스턴스화가 불가능`하다.
- 인터페이스는 `다중 상속`이 가능하다.
___
## 추상 클래스 (abstract class) 란?
- 추상 클래스는 클래스 앞에 `abstract` 라는 키워드를 추가하여 선언한다.
- `하나 이상의 추상 메소드가 포함된 클래스`를 추상 클래스라고 한다.
- 추상 클래스는 `인스턴스화가 불가능`하며, 이를 제대로 사용하기 위해서는 `상속`을 이용해야 한다.
    > 서브 클래스에서 슈퍼 클래스의 `모든 추상 메소드를 오버라이딩`하여 실행 가능한 코드로 구현하는 것을 의미한다. 
- 인터페이스와 달리 추상 클래스는 `다중 상속이 불가능`하지만, 슈퍼 클래스의 `멤버변수와 일반 메소드를 상속` 받을 수 있으며, 이는 슈퍼 클래스의 기능을 이용 및 확장하는 `다형성`을 실현하는 결과를 낳는다.

```java
abstract class Shape{
    public abstract void draw();
}

class Line extends Shape{
    @Override
    public void draw(){
        System.out.println("Line");
    }
}

class Circle extends Shape{
    @OVerride
    public void draw(){
        System.out.println("Circle");
    }
}

public static void main(String args[]){
    Shape wrong = new Shape();      // wrong
    Shape shape = new Line();       // ok
    Shape shape2 = new Circle();    // ok
}
```
___
### 추상 클래스 vs 인터페이스
||추상 클래스 (abstract class) |인터페이스 (interface)|
|---|---|---|
|공통점|인스터스화 불가능. <br>객체의 추상화.|인스터스화 불가능. <br>객체의 추상화.|
|상속|단일 상속|`다중 상속`|
|**목적**|공통적인 기능을 하는 객체의 추상화에 사용.<br> 슈퍼클래스의 멤버 변수와 메소드를 물려받고,<br>추상 메소드를 `고유한 기능으로 확장`|서로 관련없는 객체들의 추상화에 사용.<br> 인터페이스의 추상 메소드를 항상 `동일한 기능`으로 구현|
|키워드|abstract, extends|interface, implement|
