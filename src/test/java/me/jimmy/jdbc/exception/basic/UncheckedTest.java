package me.jimmy.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UncheckedTest {
    static class MyUnCheckedException extends RuntimeException {
        public MyUnCheckedException(String message) {
            super(message);
        }
    }

    @Test
    void unchecked_test() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUnCheckedException.class);
    }

    // UnChecked 예외는 예외를 잡거나 던지지 않아도 된다. 예외를 잡지 않으면 자동으로 밖으로 던진다.
    static class Service {
        Repository repository = new Repository();

        // 필요한 경우 예외를 잡아서 처리한다.
        public void callCatch() {
            try {
                repository.call();
            } catch (MyUnCheckedException e) {
                log.info("예외처리, message={}", e.getMessage(), e);
            }
        }

        // 예외를 잡지 않아도 된다. 자연스럽게 상위로 넘어가고, 체크 예외와 다르게 throws를 자동으로 던진다.
        public void callThrow() {
            repository.call();
        }
    }

    static class Repository {
        public void call() {
            throw new MyUnCheckedException("ex");
        }
    }
}
