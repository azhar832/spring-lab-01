package kz.iitu.springlab.bank;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void writeRequired(String message) {
        auditRepository.save(new AuditRecord(message));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void writeRequiresNew(String message) {
        auditRepository.save(new AuditRecord(message));
    }
}