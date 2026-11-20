package kz.iitu.springlab;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired AppUserRepository repository;
    @Autowired TestEntityManager em;

    private AppUser newUser(String username) {
        var user = new AppUser();
        ReflectionTestUtils.setField(user, "username", username);
        ReflectionTestUtils.setField(user, "passwordHash", "hash");
        return user;
    }

    @Test
    void findsAUserByUsername() {
        em.persist(newUser("alice"));
        em.flush();
        em.clear();          // force the query to hit the database

        var found = repository.findByUsername("alice");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("alice");
    }

    @Test
    void unknownUsernameGivesEmptyResult() {
        em.persist(newUser("alice"));
        em.flush();
        em.clear();

        var found = repository.findByUsername("nobody");

        assertThat(found).isEmpty();
    }
}