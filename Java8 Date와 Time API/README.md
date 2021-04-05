# Date와 Time API

## Java 8에 새로운 Date와 Time API가 생긴 이유
- [Reference](https://codeblog.jonskeet.uk/2017/04/23/all-about-java-util-date/)
- 이전까지 사용하던 `java.util.Date` 클래스는 `mutable`하기 때문에 `thread unsafe`했다.
```java
Date date = new Date();
long time = date.getTime();        // Mon Apr 05 16:06:21 KST 2021
        
Thread.sleep(1000 * 3);
Date after3Seconds = new Date();
System.out.println(after3Seconds); // Mon Apr 05 16:06:24 KST 2021
        
// mutable(값이 변할 수 있는)
after3Seconds.setTime(time);
System.out.println(after3Seconds); // Mon Apr 05 16:06:21 KST 2021
```
- 버그 발생 여지가 많았다.
    1. 타입 불안정성
        > ex) Year에 음수를 넣거나, Month에 13 이상의 값을 넣는 등의 행위가 가능했다.
    2. Date라는 이름과 달리, 특정 시점을 밀리초 단위로 표현
    3. 1900년 기준의 offset, 0부터 시작하는 Month 등의 모호한 설계
    ```java
    Calendar seonpilBirthDay = new GregorianCalendar(1997, Calendar.MARCH, 6); // 3월을 표현하려면 2를 넣거나, Calendar.MARCH를 넣어주어야 했다.
    ```
- 결과 또한 직관적이지 않았음.
- 이러한 문제로 [Joda Time](https://www.joda.org/joda-time/) 라이브러리를 사용했었다.

## Java 8에서 제공하는 Date-Time API
- [JSR-310 스팩](https://jcp.org/en/jsr/detail?id=310)의 구현체를 제공한다.

### 디자인 철학
1. `Clear`
    - Date-Time API의 메소드들은 명확하며, 결과를 예측하기 쉽다.
        > ex) Date-Time 메소드에 null을 parameter로 넘기면, NPE를 발생시킨다.
2. `Fluent`
    - Date-Time API는 프로그래머가 읽기 쉬운 interface를 제공한다.
    ```java
    LocalDate today = LocalDate.now();
    LocalDate payday = today.with(TemporalAdjusterslastDayOfMonth()).minusDays(2);
    ```
3. `Immutable`
    - Date-Time API의 classes는 `immutable(불변의)` 객체를 생성하며, 이는 `thread-safe`를 의미한다.
    ```java
    LocalDate dateOfBirth = LocalDate.of(1997, Month.MARCH, 6);
    LocalDate firstBirthday = dateOfBirth.plusYears(1);
    // dateOfBirth.plus(1, ChronoUnit.YEARS) 와 동일함.
    ```
4. `Extensible`
    - Date-Time API는 가능한 모든 곳에서 확장가능하다.
        > ex) 자신만의 time adjusters와 queries를 정의할 수있다.<br>
        > ex) 자신만의 calendar system을 만들 수 있다.

### 주요 API
- Machine time과 Human time으로 나뉘어진다.
    - `Machine time`은 `EPOCK(1970년 1월 1일 0시 0분 0초 UTC)`부터 현재까지의 타임스탬프를 표현한다.
    - `Humam time`은 우리가 흔히 사용하는 연, 월, 일, 시, 분, 초 등을 표현한다.
- 타임스탬프는 `Instant`를 사용한다.
    ```java
    Instant now = Instant.now(); // 현재 UTC를 출력한다.
    System.out.println(now); // 2021-04-05T12:33:24.346389300Z
    System.out.println(now.atZone(ZoneId.of("UTC"))); // 2021-04-05T12:33:24.346389300Z[UTC]

    ZondId zone = ZoneId.systemDefault();
    ZonedDateTime zonedDateTime = now.atZone(zone);
    System.out.println(zonedDateTime); // 2021-04-05T21:33:24.346389300+09:00[Asia/Seoul]
    ```
- `특정 날짜(LocalDate)`, `시간(LocalTime)`, `일시(LocalDateTime)`, `특정 zone의 일시(ZonedDateTime)`를 사용할 수 있다.
    1. `LocalDate`
    ```java
    LocalDate today = LocalDate.now(); // 현재 날짜 정보를 표시
    LocalDate date = LocalDate.of(2021, 4, 5); // 2021-04-05
    int year = date.getYear(); // 2021
    Month month = date.getMonth(); // APRIL
    int dayOfMonth = date.getDayOfMonth(); // 05
    DayOfWeek dayOfWeek = date.getDayOfWeek(); // MONDAY
    int len = date.lengthOfMonth(); // 30 (4월의 일 수)
    boolean leapYear = date.isLeapYear(); // 윤년 여부
    ```
    - `get()` 메소드에 `TemporalField`를 전달하여 정보를 얻을 수도  있다.
        - `TemporalField` 시간 관련 객체에서 어떤 필드의 값에 접근할지를 정의하는 interface이다.
        - `enum` 타입인 `ChronoField`는 `TemporalField` interface를 정의한다.
        ```java
        int year = today.get(ChronoField.YEAR);
        int month = today.get(ChronoField.MONTH_OF_YEAR);
        int day = today.get(ChronoField.DAY_OF_MONTH);
        ```
    2. `LocalTime`
    ```java
    LocalTime now = LocalTime.now(); // 현재 시간 정보를 표시
    LocalTime time = LocalTime.of(17, 44, 15); // 오후 5시 44분 15초
    int hour = time.getHour(); // 17
    int minute = time.getMinute(); // 44
    int second = time.getSecond(); // 15
    ```
    - 날짜와 시간 문자열로 `LocalDate`와 `LocalTime`의 instance를 만드는 방법도 있다.
        - 정적 `parse()` 메소드를 이용
        ```java
        LocalDate date = LocalDate.parse("2021-04-05");
        LocalTime time = LocalTime.parse("17:53:15");
        ```
        - `parse()` 메소드에 `DateTimeFormatter`를 전달할 수 있다.
    3. `LocalDateTime`
    - `LocalDateTime`은 LocalDate와 LocalTime을 쌍으로 갖는 복합클래스이다.
    - 날짜와 시간 모두 표현할 수 있고, 조합할 수도 있다.
    - `LocalDate`의 `atTime` 메소드와 `LocalTime`의 `atDate` 메소드를 이용하여 `LocalDateTime`을 만들 수도 있다.
    ```java
    LocalDateTime dt1 = LocalDateTime.of(2021, Month.APRIL, 5, 17, 53, 0);
    LocalDateTime dt2 = LocalDateTime.of(2021, 4, 5, 17, 53, 0);
    LocalDateTime dt3 = LocalDateTime.of(date, time);
    LocalDateTime dt4 = date.atTime(17, 53, 0);
    LocalDateTime dt5 = date.atTime(time);
    LocalDateTime dt6 = time.atDate(date);
    ```
    - `LocalDateTime`의 `toLocalDate`와 `toLocalTime` 메소드를 이용하여 LocalDate와 LocalTime instance를 만들 수도 있다.
    ```java
    LocalDate date1 = dt1.toLocalDate(); // 2021-04-05
    LocalTime time1 = dt1.toLocalTime(); // 17:53:15
    ```
    4. `ZonedDateTime`
    - `ZonedDAteTime`은 특정 zone의 LocalDateTime을 구하는 클래스이다.
    ```java
    ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul")); // 2021-04-05T21:57:38.948501800+09:00[Asia/Seoul]
    ```
    - `toLocal___()`, `atZone()` 메소드를 이용하여 서로 변환이 가능하다. 
- 기간을 표현할 때는, `Duration(시간 기반)`과 `Period(날짜 기반)`를 사용할 수 있다.
    1. `Period`는 Human time 비교를 할 때 사용한다.
    ```java
    LocalDate today = LocalDate.now();
    LocalDate thisYearBirthday = LocalDate.of(2021, Month.MARCH, 6);

    // 차이가 나는 전체 일 수가 아니다.
    // 차이가 30일이 넘어가면 다시 1부터 시작해서 계산한다.
    // getMonths()와 getDays()를 혼용하거나, ChronoUnit.DAYS.between(now, future)을 사용하면 차이가 나는 전체 일 수를 알 수 있다.
    Period period = Period.between(today, thisYearBirthday);
    // 상동
    Period until = today.until(thisYearBirthday);

    System.out.println(period.getDays());
    // 상동
    System.out.println(period.get(ChronoUnit.DAYS));

    // 차이가 나는 전체 일 수 확인하기
    long between = ChronoUnit.DAYS.between(today, thisYearBirthday);
    System.out.println(between);
    ```
    2. `Duraiton`은 Machine time 비교를 할 때 사용한다.
    ```java
    Instant now = Instant.now();
    Instant plus = now.plus(10, ChronoUnit.SECONDS);
    Duration between1 = Duration.between(now, plus);
    System.out.println(between1.getSeconds());
    ```

- `DateTimeFormatter`를 사용해서 날짜를 특정한 문자열로 변환할 수 있다.
```java
LocalDateTime now = LocalDateTime.now();
DateTimeFormatter MMddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
System.out.println(now.format(MMddyyyy)); // 04/05/2021
```
- [Predefined Formatters](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ofLocalizedDate-java.time.format.FormatStyle-)
```java
LocalDateTime now = LocalDateTime.now();
DateTimeFormatter MMddyyyy = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
System.out.println(now.format(MMddyyyy)); // 2021. 4. 5. 오후 10:31
```

- `parse()`를 사용해서 날짜나 시간을 표현하는 문자열을 `날짜 객체`로 변환할 수 있다.
```java
LocalDate date1 = LocalDate.parse("20210405", DateTimeFormatter.BASIC_ISO_DATE);
LocalDate date2 = LocalDate.parse("2021-04-05", DateTimeFormatter.ISO_LOCAL_DATE);
LocalDate date3 = LocalDate.parse("04/05/2021", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

// 모두 2021-04-05 으로 변환된다.
```
- `Legacy API`(이전 API) 지원
    - `GregorianCalendar`와 `Date` 타입의 instance를 `Instant`나 `ZonedDateTime`으로 변환 가능.
    - `java.util.TimeZone`에서 `java.time.ZoneId`로 상호 변환 가능.
    ```java
    Date date = new Date();
    // Legacy -> Recent : Instant로 변환
    Instant instant = date.toInstant();
    // Recent -> Legacy : Date로 변환
    Date newDate = Date.from(instant);

    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    // Legacy -> Recent : ZonedDateTime으로 변환
    ZonedDateTime dateTime = gregorianCalendar.toInstant()
            .atZone(ZoneId.systemDefault());
    // ZonedDateTime -> LocalDateTime으로 변환
    LocalDateTime localdateTime = dateTime.toLocalDateTime();
    // Recent -> Legacy : GregorianCalendar으로 변환
    GregorianCalendar from = GregorianCalendar.from(dateTime);

    // Recent -> Leagacy : ZoneId로 변환
    ZoneId zoneId = TimeZone.getTimeZone("PST").toZoneId(); 
    System.out.println(zoneId); // America/Los_Angeles
    // Legacy -> Recent : TimeZone으로 변환
    TimeZone timeZone = TimeZone.getTimeZone(zoneId);
    System.out.println(timeZone.getId()); // America/Los_Angeles
    ```
