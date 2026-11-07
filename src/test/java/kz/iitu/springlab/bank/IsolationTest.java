package kz.iitu.springlab.bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

class IsolationTest extends AbstractLabTest {

    @Autowired
    PlatformTransactionManager txManager;

    private void run(String label, int isolation) {
        TransactionTemplate outer = new TransactionTemplate(txManager);
        outer.setIsolationLevel(isolation);

        TransactionTemplate other = new TransactionTemplate(txManager);
        other.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        outer.executeWithoutResult(status -> {
            BigDecimal first = balanceOf("Alice");

            // another transaction updates the same account and commits
            other.executeWithoutResult(s ->
                    jdbc.update("UPDATE accounts SET balance = balance + 50 WHERE owner_name = 'Alice'"));

            BigDecimal second = balanceOf("Alice");

            System.out.println("=== " + label + " ===");
            System.out.println("First read : " + first);
            System.out.println("Second read: " + second);
            System.out.println("Changed value visible: " + (first.compareTo(second) != 0));
        });
    }

    @Test
    void readCommitted() {
        run("READ_COMMITTED", TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    @Test
    void repeatableRead() {
        run("REPEATABLE_READ", TransactionDefinition.ISOLATION_REPEATABLE_READ);
    }
}