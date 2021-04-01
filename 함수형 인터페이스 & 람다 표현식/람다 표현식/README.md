# 람다 표현식 (Lambda Expression)

## 람다 (Lambda)
- `(인자 리스트) -> {바디}`
- `람다 함수` : 식별자 없이 실행가능한 함수, `익명 함수`형태로 사용 됨
- 장점
    1. 코드의 간결성 : 불필요한 반복문 삭제, 복잡한 식을 단순하게 표현 가능
    2. 지연연산 수행 : 불필요한 연산 최소화
    3. 병렬처리 가능 : 멀티스레드 활용
- 단점
    1. 불필요하게 많이 사용하면 가독성을 떨어뜨릴 수 있음
    2. 재사용이 불가능함
    3. 디버깅이 까다로움

## 인자 리스트 (Parameter List)
- 인자 x : `()`
```java 
Supplier<Integer> get10 = () -> 10;
```
- 인자 1 개 : `(one)` or `one` 
```java
UnaryOperator<Integer> add10 = n -> n + 10;
UnaryOperator<Integer> add10 = (n) -> n + 10;
```
- 인자 여러 개 : `(one, two, ...)`
```java
BinaryOperator<Integer> multiply = (n, m) -> n * m;
```
- 인자의 `타입은 생략 가능`, 컴파일러가 추론(infer)하지만 명시할 수도 있다.
```java
UnaryOperator<Integer> add10 = (Integer n) -> n + 10;
```

## 바디 (Body)
- 화살표 오른쪽에 함수 본문을 정의한다.
- 여러 줄인 경우에 {}를 사용하여 묶는다.
- `한 줄`인 경우에 `{} 생략` 가능하며, `return도 생략` 가능하다.
```java
UnaryOperator<Integer> add10 = n -> n + 10;
UnaryOperator<Integer> add10 = (n) -> {
    return n + 10;
};
```

## 변수 캡처 (Variable Capture)
- 로컬 변수 캡처
    - `final`이거나 `effective final`인 경우에만 참조할 수 있다.
    - 그렇지 않을 경우 `concurrency 문제`가 생길 수 있어서 컴파일러가 방지한다.
    - 익명 함수 외부에 있는 지역 변수(자유 변수)는 `람다 캡처링`에 의해 복사되기 때문에, 다른 스레드에서 참조할 수 있다.
        - 람다 캡처링 : parameter로 넘겨진 변수가 아닌, 외부에서 정의된 변수(`자유 변수`)의 복사본을 만들어 접근을 허용.
    - 람다 캡처링에 의해 복사된 참조 값을 변경하면, 람다 실행 시점에 따라 해당 값을 예측할 수 없기 때문에, 이를 방지하기 위해 자유변수를 final, effective final 변수로 사용한다.
    - 즉, 자바의 `스레드 한정 원칙`을 위배하지 않기 위함이다.
- `effective final`
    - 자바 8부터 지원하는 기능으로, `사실상 final`인 변수
    - 재할당하지 않고, 참조가 변경되지 않는 변수
    - final 키워드를 사용하지 않은 변수가 `익명 클래스 구현체` 또는 `람다`에서 참조될 수 있다.
- 익명 클래스 구현체와 달리 `쉐도윙(shadowing)`하지 않는다.
    - 익명 클래스는 새로 `scope`를 형성하지만, 람다는 람다를 감싸고 있는 scope와 동일하다.
```java
private void run() {
    int baseNumber = 10;    // final이 생략되어있다. (effective final)
                            // 익명/로컬 클래스나 람다함수에서 참조하는 경우 값이 변경되어서는 안되기 때문에 상수취급.
    // 로컬 클래스
    class LocalClass{
        void printBaseNumber(){
            int baseNumber = 11;
            System.out.println(baseNumber); // 쉐도윙, 11 출력
        }
    }

    // 익명 클래스
    IntConsumer integerConsumer = new IntConsumer() {
        @Override
        public void accept(int baseNumber) {
            System.out.println(baseNumber); // 쉐도윙, 매개 변수 값 출력
        }
    };

    // 람다
    IntConsumer printInt = i -> System.out.println(i + baseNumber);
    // 쉐도윙 불가능. 같은 scope 취급을 하므로 같은 변수명으로 사용 불가
    // (baseNumber) -> System.out.println(baseNumber); 불가능!
    printInt.accept(10);
}
```
