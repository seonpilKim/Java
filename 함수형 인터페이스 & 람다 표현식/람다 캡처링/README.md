# 람다 캡처링
- `람다 캡처링` : 익명 함수(람다) 외부의 지역변수(자유변수)를 참조하는 행위

## 자유변수의 조건
- 람다 외부의 지역변수는 `final`로 선언되어야 한다.
- 만약 final로 선언되어있지 않다면, 해당 변수는 `final처럼 동작`해야한다.
    - 즉, `값의 재할당`이 일어나면 안되며 이를 `effective final`이라 부른다.
```java
public class LambdaCapturing {
    private int a = 12; // instance 변수

    public void test() {
        final int b = 123;
        int c = 123;    // effective final
        int d = 123;

        final Runnable r = () -> {
            // instance 변수 a는 final로 선언할 필요도, final처럼 재할당하면 안된다는 제약조건도 적용되지 않는다.
            a = 123;
            System.out.println(a);
        };

        // 지역변수 b는 final로 선언하였기 때문에 OK
        final Runnable r2 = () -> System.out.println(b);

        // 지역변수 c는 final로 선언하지 않았지만 final을 선언한 것과 같이 변수에 값을 재할당하지 않았으므로 OK
        final Runnable r3 = () -> System.out.println(c);
        
        // 지역변수 d는 final로 선언하지도 않았고, 값의 재할당이 일어났으므로 final처럼 동작하지 않기 때문에 compile error 발생
        d = 12;
        final Runnable r4 = () -> System.out.println(d);
    }
}
```

## 고찰
### 왜 `람다캡처링`을 하려면 `fianl` 혹은 `effective final` 형태여야만하고, `instance 변수`에는 이러한 조건이 필요가 없을까?
- 우선 `JVM(Java Virtual Machine)`의 메모리 구조를 알아보자.
    - JVM에서 `지역 변수는 stack 영역`에 할당된다.
    - JVM은 `thread마다 별도의 stack`이 생성하므로, `지역 변수는 thread끼리 공유가 불가능`하다.
    - 그러나, `instance 변수는 heap 영역`에 할당되므로, `instance 변수는 thread끼리 상시공유가 가능`하기 때문에, final같은 제약 조건이 필요하지 않다.
- `람다`는 `별도의 thread`에서 실행이 가능하다.
    - 따라서 원래 지역 변수가 있는 thread가 사라져서 해당 지역변수도 사라졌는데도 불구하고, `람다가 실행중인 thread는 살아있을 가능성이 있다.`
    - 하지만, 람다는 지역 변수를 `자신 thread의 stack 영역에 복사`하기 때문에, 오류가 발생하지 않는다.
    - 이 때, 위와 같이 지역변수를 복사하여 사용하는데, 여기저기서 값이 변경된다고 하면 `동기(sync)`의 문제가 발생하기 때문에, 이를 방지하기 위해 지역변수를 `final` or `effective final` 형태를 컴파일러가 프로그래머에게 강제하는 것이다.