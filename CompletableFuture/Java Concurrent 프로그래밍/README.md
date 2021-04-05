# Java Concurrent 프로그래밍

### Concurrent 소프트웨어
- 동시에 여러 작업을 할 수 있는 소프트웨어
    > ex) 웹 브라우저로 유튜브를 보면서, 키보드로 문서에 타이핑을 할 수 있음

### 자바에서 지원하는 Concurrent 프로그래밍
- 멀티프로세싱 (ProcessBuilder)
- 멀티쓰레드

### 자바 멀티쓰레드 프로그래밍
- `Thread` / `Runnable`

### Thread 상속
- main thread와 myThread가 랜덤 순서로 실행된다.
```java
public static void main(String[] args) {
    MyThread myThread = new MyThread();
    myThread.start();
    System.out.println("Hello " + Thread.currentThread().getName());
}
static class MyThread extends Thread{
    @Override
    public void run(){
        System.out.println("Thread: " + Thread.currentThread().getName());
    }
}
```

### Runnable 구현 or 람다
- Runnable 구현
```java
public class RunnableTest{
	public static void main(String[] args){
		Runnable r = new foo();
		Thread t = new Thread(r);
		t.start();
	}
}
class foo implements Runnable{
	@Override
	public void run() {}
}
```
- 람다
```java
public static void main(String[] args) {
    Thread thread = new Thread(() -> {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread: " + Thread.currentThread().getName());
    });
    thread.start();
    System.out.println("Hello " + Thread.currentThread().getName());
}
```

### Thread 주요 기능
- 현재 thread 멈춰두기 (`sleep`) : 다른 thread가 처리할 수 있도록 기회를 주지만, `lock`을 풀진 않는다. (deadlock 위험 有)
```java
public static void main(String[] args) throws InterruptedException{
    Thread thread = new Thread(() -> {
        while(true) {
            System.out.println("Thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                System.out.println("exit~!");
                return;
            }
        }
    });
    thread.start();
```
- 다른 thread 깨우기 (`interrupt`) : 다른 thread를 깨워서 `interruptedException`을 발생시킨다.
```java
System.out.println("Hello " + Thread.currentThread().getName());
Thread.sleep(3000L);
thread.interrupt();
```
- 다른 thread 기다리기 (`join`) : 다른 thread가 끝날 때 까지 기다린다.
```java
try {
    thread.join();
} catch (InterruptedException e) {
    e.printStackTrace();
}
System.out.println(thread + " is finished");
```