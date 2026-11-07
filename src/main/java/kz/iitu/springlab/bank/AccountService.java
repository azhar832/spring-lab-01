package kz.iitu.springlab.bank;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account from = repository.findById(fromId).orElseThrow();
        Account to = repository.findById(toId).orElseThrow();

        from.debit(amount);
        to.credit(amount);
        repository.save(from);
        repository.save(to);
    }


    @Transactional
    public void transferFailAfterDebit(Long fromId, Long toId, BigDecimal amount) {
        Account from = repository.findById(fromId).orElseThrow();
        from.debit(amount);
        repository.save(from);
        throw new RuntimeException("Simulated failure after debit");
    }


    @Transactional
    public void transferCheckedFailure(Long fromId, Long toId, BigDecimal amount) throws Exception {
        Account from = repository.findById(fromId).orElseThrow();
        from.debit(amount);
        repository.save(from);
        throw new Exception("Checked failure after debit");
    }


    @Transactional(rollbackFor = Exception.class)
    public void transferCheckedFailureWithRollbackFor(Long fromId, Long toId, BigDecimal amount) throws Exception {
        Account from = repository.findById(fromId).orElseThrow();
        from.debit(amount);
        repository.save(from);
        throw new Exception("Checked failure after debit");
    }
}