package kz.iitu.springlab.bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptimisticLockingTest extends AbstractLabTest {

    @Autowired
    PlatformTransactionManager txManager;

    @Test
    void staleUpdateIsRejected() {
        TransactionTemplate tx1 = new TransactionTemplate(txManager);

        TransactionTemplate tx2 = new TransactionTemplate(txManager);
        tx2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        OptimisticLockingFailureException ex = assertThrows(OptimisticLockingFailureException.class, () ->
                tx1.executeWithoutResult(s1 -> {
                    // copy 1 is loaded with version 0
                    Account copy1 = accountRepository.findById(aliceId).orElseThrow();

                    // second transaction loads its own copy, updates it and commits first
                    tx2.executeWithoutResult(s2 -> {
                        Account copy2 = accountRepository.findById(aliceId).orElseThrow();
                        copy2.credit(new BigDecimal("10.00"));
                    });

                    // stale copy 1 is updated and tries to commit
                    copy1.credit(new BigDecimal("20.00"));
                }));

        System.out.println("CONFLICT: " + ex);
        System.out.println("FINAL STATE: Alice balance = " + balanceOf("Alice")
                + ", version = " + versionOf("Alice"));

        assertThat(balanceOf("Alice")).isEqualByComparingTo("1010.00");
        assertThat(versionOf("Alice")).isEqualTo(1L);
    }
}