package kz.iitu.springlab.bank;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:txlab-test;DB_CLOSE_DELAY=-1",
        "logging.level.org.springframework.transaction.interceptor=TRACE"
})
abstract class AbstractLabTest {

    @Autowired
    protected JdbcTemplate jdbc;

    @Autowired
    protected AccountRepository accountRepository;

    protected Long aliceId;
    protected Long bobId;

    @BeforeEach
    void resetData() {
        jdbc.update("DELETE FROM audit_log");
        jdbc.update("UPDATE accounts SET balance = 1000.00, version = 0 WHERE owner_name = 'Alice'");
        jdbc.update("UPDATE accounts SET balance = 500.00, version = 0 WHERE owner_name = 'Bob'");
        aliceId = accountRepository.findByOwnerName("Alice").orElseThrow().getId();
        bobId = accountRepository.findByOwnerName("Bob").orElseThrow().getId();
    }

    protected BigDecimal balanceOf(String owner) {
        return jdbc.queryForObject(
                "SELECT balance FROM accounts WHERE owner_name = ?", BigDecimal.class, owner);
    }

    protected Long versionOf(String owner) {
        return jdbc.queryForObject(
                "SELECT version FROM accounts WHERE owner_name = ?", Long.class, owner);
    }
}