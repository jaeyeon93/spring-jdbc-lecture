package me.jimmy.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

public class CheckedAppTest {

    static class Controller {
        Service service = new Service();

        @Test
        void checked() {
            Controller controller = new Controller();
            Assertions.assertThatThrownBy(() -> service.test())
                    .isInstanceOf(SQLException.class);
        }

        public void request() throws SQLException, ConnectException {
            service.test();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient client = new NetworkClient();

        public void test() throws ConnectException, SQLException {
            repository.call();
            client.call();
        }
    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException();
        }
    }

    static class Repository {
        public void call() throws SQLException {
            throw new SQLException();
        }
    }
}
