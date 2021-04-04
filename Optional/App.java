package java8to11;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Progress {
    private Duration studyDuration;
    private boolean finished;

    public Duration getStudyDuration(){
        return studyDuration;
    }

    public void setStudyDuration(Duration studyDuration) {
        this.studyDuration = studyDuration;
    }
}

class OnlineClass{
    private Integer id;
    private String title;
    private boolean closed;

    /*public Progress getProgress() {
        // 자바 8 이전에는 이런식으로 에러 처리를 해왔다.
        if(this.progress == null)
            throw new IllegalStateException();
        return progress;
    }*/

    public Optional<Progress> getProgress(){
        // null값이 들어있을 수도 있는 값을 출력할 때 사용
        return Optional.ofNullable(progress);
        // ofNullable이 아닌, of를 사용하면 null이 무조건 없는 값일 때 사용
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public Progress progress;

    public OnlineClass(Integer id, String title, boolean closed) {
        this.id = id;
        this.title = title;
        this.closed = closed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}

public class App {

    public static void main(String[] args) {
        List<OnlineClass> springClaases = new ArrayList<>();
        springClaases.add(new OnlineClass(1, "spring boot", true));
        springClaases.add(new OnlineClass(2, "spring data jpa", true));
        springClaases.add(new OnlineClass(3, "spring mvc", false));
        springClaases.add(new OnlineClass(4, "spring core", false));
        springClaases.add(new OnlineClass(5, "rest api development", false));

        Optional<OnlineClass> optional = springClaases.stream()
                .filter(oc -> oc.getTitle().startsWith("spring"))
                .findFirst();
        boolean present = optional.isPresent();
        System.out.println(present);

        // System.out.println(optional.get().getTitle());  // 찾는 데이터가 없으면 NoSuchElementException 발생
        // 방법 1.
        // 데이터가 없을 때, 처리하는 방식이 없음
        optional.ifPresent(oc -> System.out.println(oc.getTitle()));

        // 방법 2.
        // 데이터가 없으면, 새로운 클래스를 생성(better), orElse
        // 그러나 데이터가 있던 없던 createNewClass() 함수는 반드시 실행된다.
        // 이미 만들어져있는 상수 instance를 참조하여 사용할 때는 orElse 사용
        OnlineClass onlineClass = optional.orElse(createNewClass());
        System.out.println(onlineClass.getTitle());

        // 방법 3.
        // 데이터가 없을 때만, createNewClass() 함수를 실행시키는 방법(better), orElseGet
        // 동적으로 작업을 할 때, 추가 작업이 필요한 경우 orElseGet 사용
        OnlineClass onlineClass1 = optional.orElseGet(App::createNewClass);
        System.out.println(onlineClass1.getTitle());

        // 방법 4.
        // 데이터가 없을 때 대안이 없는 경우, 에러를 던지는 방법
        // 원하는 에러를 던지고 싶으면, parameter에 supplier 형태로 추가해주면 된다.
        OnlineClass onlineClass2 = optional.orElseThrow();
        OnlineClass onlineClass3 = optional.orElseThrow(IllegalAccessError::new);

        // Optional에 들어있는 값 걸러내기
        Optional<OnlineClass> onlineClass4 = optional.filter(oc -> !oc.isClosed());
        System.out.println(onlineClass4.isEmpty());

        // Optional에 들어있는 값 변환하기
        Optional<Integer> integer = optional.map(OnlineClass::getId);
        System.out.println(integer.isPresent());

        // map으로 꺼내는 type이 optional인 경우
        Optional<Optional<Progress>> progress = optional.map(OnlineClass::getProgress);
        Optional<Progress> progress1 = progress.orElseThrow();
        Progress progress2 = progress1.orElseThrow();

        // 위와 같은 방식은 너무 복잡하다.
        // optional이 제공하는 flatMap은 optional 타입으로 리턴하는 껍질을 한 번 까준다.
        Optional<Progress> progress3 = optional.flatMap(OnlineClass::getProgress);
        Progress progress4 = progress3.orElseThrow();

    }

    private static OnlineClass createNewClass() {
        System.out.println("creating new online class");
        return new OnlineClass(10, "New Class", false);
    }
}
