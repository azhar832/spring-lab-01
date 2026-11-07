package kz.iitu.springlab.bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransferTransactionTest extends AbstractLabTest {

    @Autowired
    AccountService service;

    @Test
    void a1_successfulTransfer() {
        service.transfer(aliceId, bobId, new BigDecimal("100.00"));

        System.out.println("A1 RESULT: Alice = " + balanceOf("Alice") + ", Bob = " + balanceOf("Bob"));
        assertThat(balanceOf("Alice")).isEqualByComparingTo("900.00");
        assertThat(balanceOf("Bob")).isEqualByComparingTo("600.00");
    }

    @Test
    void a2_insufficientFunds_nothingChanges() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.transfer(aliceId, bobId, new BigDecimal("5000.00")));

        System.out.println("A2 RESULT: " + ex.getMessage()
                + " | Alice = " + balanceOf("Alice") + ", Bob = " + balanceOf("Bob"));
        assertThat(balanceOf("Alice")).isEqualByComparingTo("1000.00");
        assertThat(balanceOf("Bob")).isEqualByComparingTo("500.00");
    }

    @Test
    void a3_uncheckedExceptionAfterDebit_rollsBack() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.transferFailAfterDebit(aliceId, bobId, new BigDecimal("100.00")));

        System.out.println("A3 RESULT: " + ex.getMessage()
                + " | Alice = " + balanceOf("Alice") + ", Bob = " + balanceOf("Bob"));
        assertThat(balanceOf("Alice")).isEqualByComparingTo("1000.00");
        assertThat(balanceOf("Bob")).isEqualByComparingTo("500.00");
    }

    @Test
    void a4_checkedException_commitsByDefault() {
        Exception ex = assertThrows(Exception.class,
                () -> service.transferCheckedFailure(aliceId, bobId, new BigDecimal("100.00")));

        System.out.println("A4 RESULT: " + ex.getMessage()
                + " | Alice = " + balanceOf("Alice") + ", Bob = " + balanceOf("Bob"));
        assertThat(balanceOf("Alice")).isEqualByComparingTo("900.00");
        assertThat(balanceOf("Bob")).isEqualByComparingTo("500.00");
    }

    @Test
    void a5_checkedException_withRollbackFor_rollsBack() {
        Exception ex = assertThrows(Exception.class,
                () -> service.transferCheckedFailureWithRollbackFor(aliceId, bobId, new BigDecimal("100.00")));

        System.out.println("A5 RESULT: " + ex.getMessage()
                + " | Alice = " + balanceOf("Alice") + ", Bob = " + balanceOf("Bob"));
        assertThat(balanceOf("Alice")).isEqualByComparingTo("1000.00");
        assertThat(balanceOf("Bob")).isEqualByComparingTo("500.00");
    }
}