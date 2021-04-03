# Stream

## Stream 소개

### Stream
- `Java 8 이전`에는, 배열 or 컬렉션 instance를 `for` 또는 `foreach`문을 돌면서 요소 하나씩을 꺼내어 다루었다.
    - logic이 복잡해질수록 코드의 양이 많아져, 여러 logic이 섞이는 문제가 발생.
    - 메소드를 나눌 경우, loop를 여러 번 도는 경우가 발생.
- `Java 8 이후`에 나타난 `Stream`은 `배열 or 컬렉션(Collection) instance`에 `함수 여러 개를 조합`하여 원하는 결과를 `필터링`하고 가공된 결과를 얻을 수 있다.
- 데이터를 담고 있는 저장소(Collection)가 아닌, `데이터의 흐름`이다.
- 또한, `람다`를 이용하여 배열과 컬렉션을 `함수형`으로 처리할 수도 있다.
- 스레드(Thread)를 이용하여 많은 요소들을 `병렬 처리`하기 쉽다.
    - `병렬 처리` : 하나의 작업을 둘 이상의 작업으로 잘게 나누어 동시에 진행하는 것.
    ```java
    // 병렬적으로 출력하기가 어렵다.
    for(String name : names){
        if(name.startsWith("s"))
            System.out.println(name.toUpperCase());
    }
    // 그러나, parallelStream을 이용하면 병렬적으로 처리하는 것이 쉽다.
    List<String> collect1 = names.parallelStream().map(s-> {
        System.out.println(s + " " + Thread.currentThread().getName());
        return s.toUpperCase();
    }).collect(Collectors.toList());
    collect1.forEach(System.out::println);
    ```
- `Functional in nature`, 스트림은 태생이 함수형이라, 처리하는 데이터 소스를 변경하지 않고, 결과값만을 생성한다.
- 스트림으로 처리하는 데이터는 `오직 한 번만 처리`한다.
- 스트림이 무제한일 수도 있다. (Short Circuit 메소드를 사용해서 제한할 수 있다.)

### 스트림 파이프라인
- 0개 혹은 다수의 `중간 연산(intermediate operation)`과 한 개의 `종료 연산(terminal operation)`으로 구성된다.
- 스트림의 데이터 소스는 오직 `종료 연산`을 실행할 때만 처리된다.
```java
// 종료 연산이 없으면, 중간 연산은 실행되지 않음.
names.stream().map(s->{
    System.out.println(s);  // 출력되지 않는다.
    return s.toUpperCase();
});

List<String> collect = names.stream().map(s -> {
    System.out.println(s);  // 출력 됨.
    return s.toUpperCase(); // 대문자로 변환시킨 결과 return
}).collect(Collectors.toList());    // 종료 연산 collect
System.out.println("===========");
collect.forEach(System.out::println);   // 대문자로 변환된 결과 출력
```

### 중간 연산(intermediate operation)
- `Stream을 return한다.`
- 중간 연산은 근본적으로 `lazy`하다. (최종 연산을 할 때, 중간 연산이 같이 실행된다.)
- Stateless / Stateful operation으로 더 상세하게 구분할 수도 있다.
    - 대부분은 Stateless이지만, distinct나 osrted처럼 이전의 소스 데이터를 참조해야하는 operation은 Stateful operation이다.
> ex) filter, map, limit, skip, sorted, ...

### 종료 연산(terminal operation)
- `Stream을 return하지 않는다.`
> ex) collect, allMatch, count, forEach, min, max, ...

## Stream API

### 걸러내기
- `Filter(Predicate)`
    > ex) 이름이 3글자 이상인 데이터만 새로운 스트림으로 걸러내기
    ```java
    springClaases.stream()
            .filter(oc -> oc.getTitle()startsWith("spring"))
            .forEach(oc -> System.out.println(oc.getId()));
    ```

### 변경하기
- `Map(Function)` or `FlatMap(Function)`
    > ex) 각각의 Post instance에서 String title만 새로운 스트림으로 변경하기
    > ex) List<Stream<String>>을 String의 Stream으로 변경하기
    ```java
    Events.stream()
            .flatMap(list -> list.stream())    // 각list의 classes를 flatting 시킨 stream으로 만듦
            //.flatMap(Collection::stream) 메소드 레퍼런스
            .forEach(oc -> System.out.println(oc.getId()));
    ```

### 생성하기
- `generate(Supplier)` or `Iterate(T seed, UnaryOperator)`
    > ex) 10부터 1씩 증가하는 무제한 숫자 스트림 생성하기
    > ex) 랜덤 int 무제한 스트림 생성하기
    ```java
    Stream.iterate(10, i -> i + 1)
            .forEach(System.out::println);
    ```

### 제한하기
- `limit(long)` or `skip(long)`
    > ex) 최대 5개의 요소가 담긴 스트림을 리턴하기
    > ex) 앞에서 3개를 뺀 나머지 스트림을 리턴하기
    ```java
    Stream.iterate(10, i -> i + 1)
            .skip(10)
            .limit(10)
            .forEach(System.out::println);
    ```

### 스트림에 있는 데이터가 특정 조건을 만족하는지 확인
- `anyMatch()`, `allMatch()`, `nonMatch()`
    > ex) k로 시작하는 문자열이 있는지 확인하기 (true or false를 return)
    > ex) 스트림에 있는 모든 값이 10보다 작은지 확인하기
    ```java
    boolean test = javaClasses.stream().anyMatch(oc -> oc.getTitle().contains("Test")); // boolean이 return되므로 바로 종료.
    System.out.println(test);
    ```

### 개수 세기
- `count()`
    > ex) 10보다 큰 수의 개수를 세기
    ```java
    long count = springClaases.stream()
            .filter(oc -> !oc.isClosed())
            .count();
    ```
### 스트림을 하나의 데이터로 뭉치기
- `reduce(identity, BiFunction)`, `collect()`, `sum()`, `max()`
    > ex) 모든 숫자 합 구하기
    > ex) 모든 데이터를 하나의 List 또는 Set에 옮겨 담기
    ```java
    List<String> spring1 = springClaases.stream()
            .map(oc -> oc.getTitle())
            .filter(t -> t.contains("spring"))
            .collect(Collectors.toList());
    ```            