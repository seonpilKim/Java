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