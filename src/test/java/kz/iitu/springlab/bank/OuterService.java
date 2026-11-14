package kz.iitu.springlab.bank;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OuterService {

    private final AuditService auditService;

    public OuterService(AuditService auditService) {
        this.auditService = auditService;
    }

    @Transactional
    public void outerWithRequired() {
        auditService.writeRequired("audit written with REQUIRED");
        throw new RuntimeException("Outer failure after inner method returned");
    }

    @Transactional
    public void outerWithRequiresNew() {
        auditService.writeRequiresNew("audit written with REQUIRES_NEW");
        throw new RuntimeException("Outer failure after inner method returned");
    }
}