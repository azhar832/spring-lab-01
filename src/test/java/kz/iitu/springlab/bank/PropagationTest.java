package kz.iitu.springlab.bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropagationTest extends AbstractLabTest {

    @Autowired
    OuterService outerService;

    @Autowired
    AuditRepository auditRepository;

    @Test
    void required_auditIsRolledBackWithOuter() {
        assertThrows(RuntimeException.class, () -> outerService.outerWithRequired());

        System.out.println("B REQUIRED RESULT: audit records = " + auditRepository.count());
        assertThat(auditRepository.count()).isZero();
    }

    @Test
    void requiresNew_auditSurvivesOuterRollback() {
        assertThrows(RuntimeException.class, () -> outerService.outerWithRequiresNew());

        System.out.println("B REQUIRES_NEW RESULT: audit records = " + auditRepository.count());
        assertThat(auditRepository.count()).isEqualTo(1);
    }
}