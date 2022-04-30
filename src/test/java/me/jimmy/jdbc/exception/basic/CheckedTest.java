package me.jimmy.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {
    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyCheckException.class);
    }

    // Exception을 상속받은 예외는 체크 예외
    static class MyCheckException extends Exception {
        public MyCheckException(String message) {
            super(message);
        }
    }

    static class Service {
        Repository repository = new Repository();

        // 예외를 잡아서 처리하는 코드
        public void callCatch() {
            try {
                repository.call();
            } catch (MyCheckException e) {
                // 예외처리 로직
                log.info("예외처리, message={}", e.getMessage(), e);
            }
        }

        // 체크 예외를 밖으로 던진다. 체크 예외는 예외를 잡지 않고 밖으로 던지려면 throws 예외를 메서드에 필수로 선언해야한다. 없으면 컴파일 안된다.
        public void callThrow() throws MyCheckException {
            repository.call();
        }
    }

    static class Repository {
        public void call() throws MyCheckException {
            throw new MyCheckException("ex");
        }
    }
}
