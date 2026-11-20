package kz.iitu.springlab;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "spring.h2.console.enabled=false")
@Testcontainers
class PostgresIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired JdbcTemplate jdbc;
    @Autowired AppUserRepository users;
    @Autowired TestRestTemplate rest;

    @Test
    void flywayMigrationsRunOnARealPostgres() {
        String version = jdbc.queryForObject("select version()", String.class);
        Integer applied = jdbc.queryForObject(
                "select count(*) from flyway_schema_history where success = true", Integer.class);

        assertThat(version).contains("PostgreSQL");
        assertThat(applied).isEqualTo(6);
    }

    @Test
    void seededAdminHasBothRoles() {
        var admin = users.findByUsername("admin");

        assertThat(admin).isPresent();
        assertThat(admin.get().getRoles()).containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    void applicationAnswersOnTheHomePage() {
        var response = rest.getForEntity("/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}