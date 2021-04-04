# Optional

## Optional 소개
- `Optional` 객체를 사용하면, 예상치 못한 `NullPointerException` 예외를 제공되는 `메소드를 이용`하여 간단히 `회피`할 수 있다.
```java
public Optional<Progress> getProgress(){
    // null값이 들어있을 수도 있는 값을 출력할 때 사용
    return Optional.ofNullable(progress);
    // ofNullable이 아닌, of를 사용하면 null이 반드시 없음을 보장한다는 의미
}
```
- 즉, 복잡한 조건문없이 `null` 값으로 인한 예외를 처리할 수 있게 되었다.
- `Optional` : 오직 값 한 개가 들어있을 수도, 없을 수도 있는 `컨테이너`.

### 메소드에서 작업 중 특별한 상황에서 값을 제대로 return할 수 없는 경우 선택할 수 있는 방법
- 예외를 던진다.
    - `stack trace`를 찍어두기 때문에 `비싸`다.
    ```java
    public Progress getProgress() {
        // 자바 8 이전에는 이런식으로 에러 처리를 해왔다.
        if(this.progress == null)
            throw new IllegalStateException();
        return progress;
    }
    ```
- null을 return 한다.
    - 비용 문제는 없지만, 코드를 사용하는 클라이언트 코드에서 주의해야 한다.
    ```java
    Progress progress = spring_Boot.getProgress();
    if(progress != null){
        System.out.println(progress.getStudyDuration());
    }
    ```
- Java 8 부터 `Optional`을 return 한다.
    - 클라이언트 코드에게 명시적으로 빈 값일 수도 있다는 것을 알려주고, 빈 값인 경우에 대한 처리를 `강제`한다.

### 주의사항
- `메소드`가 `반환할 결과값`이 `없음`을 명백하게 표현할 필요가 있고, `null`을 반환하면 에러를 유발할 가능성이 높은 상황에서 `메소드의 반환 타입`으로 `Optional`을 사용하는 것이 주된 목적이다.
    - 메소드 parameter type, Map의 key type, instance field type으로 `사용하지 말 것`!
- Optional을 return하는 메소드에서 `null을 return하지 말 것`!
    - 정 return할 것이 없다면, `Optional.empty()`를 return하자.
    ```java
    public Optional<Progress> getProgress(){
        return Optional.empty();
    }
    ```
    - 만약, null을 return하면, Optional을 사용하는 의미가 없어진다.
- `기본(primitive) 타입용 Optional`이 존재한다.
    > OptionalInt, OptionalLong, OptionalDouble
    - 예를들어, Optional.of(10)을 사용하면, 내부에서 boxing, unboxing이 일어나기 때문에 성능이 좋지 않다.
    - 그러므로 OptionalInt.of(10)을 사용하는 것이 효율적이다.
- Collection, Map, Stream, array, Optional 등의 `Container type을 Optional로 감싸지 말 것`!

## Optional API

### Optional 만들기
- `Optional.of(value)` : `null이 아닌` 객체를 담고있는 Optional 객체 생성
```java
OptionalInt ten = OptionalInt.of(10);
```
- `Optional.ofNullable()` : `null일수도 있는` 객체를 담고있는 Optional 객체 생성
```java
public Optional<Progress> getProgress(){
    return Optional.ofNullable(progress);
}
```
- `Optional.empty()` : `빈` Optional 객체를 생성
```java
Optional<String> opstr = Optional.empty();
```

### Optional에 값이 있는지 없는지 확인하기
- `isPresent()`
```java
Optional<OnlineClass> optional = springClaases.stream()
        .filter(oc -> oc.getTitle().startsWith("spring"))
        .findFirst();
boolean present = optional.isPresent();
```
- `isEmpty()` (Java 11)
```java
Optional<OnlineClass> onlineClass4 = optional.filter(oc -> !oc.isClosed());
System.out.println(onlineClass4.isEmpty());
```

### Optional에 있는 값 가져오기
- `get()`
```java
Optional<OnlineClass> optional = springClaases.stream()
        .filter(oc -> oc.getTitle().startsWith("spring"))
        .findFirst();
System.out.println(optional.get().getTitle());  
// 찾는 데이터가 없으면 NoSuchElementException 발생
```
___
```java
private static OnlineClass createNewClass() {
    System.out.println("creating new online class");
    return new OnlineClass(10, "New Class", false);
}

Optional<OnlineClass> optional = springClaases.stream()
        .filter(oc -> oc.getTitle().startsWith("spring"))
        .findFirst();
```
### Optional에 값이 있는 경우, 그 값을 가지고 ~~를 하라.
- `ifPresent(Consumer)`
    > ex) 제목이 spring으로 시작하는 수업이 있으면 id를 출력하라.
```java
// 데이터가 없을 때, 처리하는 방식이 없음
optional.ifPresent(oc -> System.out.println(oc.getId()));
```

### Optional에 값이 있으면 가져오고, 없으면 ~~를 return 하라.
- `orElse(T)`
```java
// 데이터가 없으면, 새로운 클래스를 생성(better), orElse
// 그러나 데이터가 있던 없던 createNewClass() 함수는 반드시 실행된다.
// 이미 만들어져있는 상수 instance를 참조하여 사용할 때는 orElse 사용
OnlineClass onlineClass = optional.orElse(createNewClass());
System.out.println(onlineClass.getTitle());
```

### Optional에 값이 있으면 가져오고, 없는 경우에만 ~~를 하라.
- `orElseGet(Supplier)`
```java
// 데이터가 없을 때만, createNewClass() 함수를 실행시키는 방법(better), orElseGet
// 동적으로 작업을 할 때, 추가 작업이 필요한 경우 orElseGet 사용
OnlineClass onlineClass1 = optional.orElseGet(App::createNewClass);
System.out.println(onlineClass1.getTitle());
```

### Optional에 값이 있으면 가져오고, 없는 경우에만 error를 throw해라.
- `orElseThrow()`
```java
// 데이터가 없을 때 대안이 없는 경우, 에러를 던지는 방법
OnlineClass onlineClass2 = optional.orElseThrow();
// 원하는 에러를 던지고 싶으면, parameter에 supplier 형태로 추가해주면 된다.
OnlineClass onlineClass3 = optional.orElseThrow(IllegalAccessError::new);
```
___
### Optional에 들어있는 값 걸러내기
- `Optional filter(Predicate)`
```java
Optional<OnlineClass> onlineClass4 = optional.filter(oc -> !oc.isClosed());
System.out.println(onlineClass4.isEmpty());
```

### Optional에 들어있는 값 변환하기
- `Optional map(Function)`
```java
// OnlineClass의 메소드 레퍼런스를 인자로 넘겨서 메소드의 결과값을 Optional 컨테이너에 담는다.
Optional<Integer> integer = optional.map(OnlineClass::getId);
System.out.println(integer.isPresent());
```
- `Optional flatMap(Function)` : Optional 안에 들어있는 instance가 Optional인 경우에 사용하면 편리하다.
```java
// map으로 꺼내는 type이 optional인 경우
Optional<Optional<Progress>> progress = optional.map(OnlineClass::getProgress);
Optional<Progress> progress1 = progress.orElseThrow();
Progress progress2 = progress1.orElseThrow()

// 위와 같은 방식은 너무 복잡하다.
// optional이 제공하는 flatMap은 optional 타입으로 리턴하는 껍질을 한 번 까준다.
Optional<Progress> progress3 = optional.flatMap(OnlineClass::getProgress);
Progress progress4 = progress3.orElseThrow();
```

## Java Optional 바르게 사용하기

### `isPresnet()-get()` 대신 `orElse()/orElseGet/orElseThrow()` 사용하기
- 이왕 비싼 `Optional`을 사용한 거 코드를 줄여서 사용하는 편이 낫다.
```java
// worst
Optional<OnlineClass> optional = springClaases.stream()
        .filter(oc -> oc.getTitle().startsWith("spring"))
        .findFirst();
if(optional.isPresent())
    return optional.get();
else
    return null;

// much better
Optional<OnlineClass> optional = springClaases.stream()
        .filter(oc -> oc.getTitle().startsWith("spring"))
        .findFirst();
return optional.orElse(null);
```

### `orElse(new ...)` 대신 `orElseGet(() -> new ...)` 사용하기
- `orElse(...)` 에서 `...`는 `Optional`에 값이 있든 없든 무조건 실행된다.
    - `Optional`에 값이 있으면, `orElse(T)`의 parameter로 실행된 값이 무시되고 버려지므로 불필요한 `overhead`가 발생한다.
- 따라서 `...`가 새로운 객체를 생성하거나, 새로운 연산을 수행하는 경우에는 `orElse(T)` 대신 `orElseGet(Supplier)`을 사용하자.
    - `orElseGet(Supplier)`에서 `Supplier`는 `Optional`에 값이 없을 때만 실행되므로, 불필요한 `overhead`가 없다.
```java
// worst
OnlineClass onlineClass1 = optional.orElse(createNewClass);

// much better
OnlineClass onlineClass1 = optional.orElseGet(App::createNewClass);
```

### 단지 값을 얻을 목적이라면, `Optional` 대신 `null` 비교하기
- `Optional`은 비싸다. 따라서 단순히 값 또는 `null`을 얻을 목적이라면, `Optional` 대신 `null` 비교를 사용하자.
```java
// worst
return Optional.ofNullable(input).orElse(INITIAL);

// much better
return input != null ? input : INITIAL;
```

### `Optional` 대신 `비어있는 컬렉션` return
- `Optional`은 비싸다. 그리고 컬렉션은 `null`이 아니라, 비어있는 컬렉션을 반환하는 것이 좋을 때가 많다.
- 그러므로 컬렉션은 `Optional`로 감싸서 반환하지 말고, 비어있는 컬렉션을 반환하자.
```java
// worst
List<Member> members = team.getMembers();
return Optional.ofNullable(members);

// much better
List<Member> members = team.getMembers();
return members != null ? members : Collections.emptyList();
```
- 마찬가지 이유로 `Spring Data JPA Repository` 메소드 선언 시, 다음과 같이 컬렉션을 `Optional`로 감싸서 변환하는 것은 좋지 않다.
- 컬렉션을 반환하는 `Spring Data JPA Repository` 메소드는 `null`을 반환하지 않고, `비어있는 컬렉션`을 반환해주기 때문이다.
```java
// worst
public interface MemberRepository<Member, Long> extends JpaRepository {
    Optional<List<Member>> findAllByNameContaining(String part);
}

// much better
public interface MemberRepository<Member, Long> extends JpaRepository {
    List<Member> findAllByNameContaining(String part);  
    // null이 반환되지 않으므로 Optional 불필요
}
```

### `Optional`을 필드로 사용 금지
- `Optional`은 필드에서 사용할 목적으로 만들어진 것이 아니며, `Serializable`을 구현하지 않았으므로, 필드로 사용하지 말자.
```java
// worst
public class Member {

    private Long id;
    private String name;
    private Optional<String> email = Optional.empty();
}

// much better
public class Member {

    private Long id;
    private String name;
    private String email;
}
```

### `Optional`을 생성자나 메소드 parameter로 사용 금지
- `Optional`을 생성자나 메소드 parameter로 사용하면, 호출할 때 마다 매번 비싼 `Optional`을 생성해서 parameter로 전달해줘야 한다.
- 그리고 어차피 API나 Library 메소드에서는 parameter가 `Optional`이든 아니든 `null` 체크를 하는 편이 안전하다.
```java
// worst
public class HRManager {
    
    public void increaseSalary(Optional<Member> member) {
        member.ifPresent(member -> member.increaseSalary(100));
    }
}
hrManager.increaseSalary(Optional.ofNullable(member));

// much better
public class HRManager {
    
    public void increaseSalary(Member member) {
        if (member != null) {
            member.increaseSalary(100);
        }
    }
}
hrManager.increaseSalary(member);
```

### `Optional`을 컬렉션의 원소로 사용 금지
- 컬렉션에는 많은 원소가 들어갈 수 있다. 따라서 비싼 `Optional`을 원소로 사용하지 말고, 원소를 꺼낼 때나 사용할 때 `null` 체크를 하는 편이 좋다.
- 특히 Map은 `getOrDefault()`, `putIfAbsent()`, `computeIfAbsent()`, `computeIfPresent()` 처럼 `null` 체크가 포함된 메소드를 제공하기도 한다.
```java
// worst
Map<String, Optional<String>> sports = new HashMap<>();
sports.put("100", Optional.of("BasketBall"));
sports.put("101", Optional.ofNullable(someOtherSports));
String basketBall = sports.get("100").orElse("BasketBall");
String unknown = sports.get("101").orElse("");

// much better
Map<String, String> sports = new HashMap<>();
sports.put("100", "BasketBall");
sports.put("101", null);
String basketBall = sports.getOrDefault("100", "BasketBall");
String unknown = sports.computeIfAbsent("101", k -> "");
```

### `Optional<T>` 대신 `기본 타입용 Optional`을 사용
- `Optional`에 담길 값이 `int`, `long`, `double` 이라면 Boxing/Unboxing이 발생하는 `Optioanl<T>`을 사용하지 말고, `OptionalInt`, `OptionalLong`, `OptionalDouble`을 사용하자.
```java
// worst
Optional<Integer> count = Optional.of(38);  // boxing 발생
for (int i = 0 ; i < count.get() ; i++) { ... }  // unboxing 발생

// much better
OptionalInt count = OptionalInt.of(38);  // boxing 발생 안 함
for (int i = 0 ; i < count.getAsInt() ; i++) { ... }  // unboxing 발생 안 함
```