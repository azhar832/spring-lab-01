package kz.iitu.springlab.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock BookRepository repository;
    @InjectMocks BookService service;

    private final Book effectiveJava = new Book(1L, "Effective Java", "Joshua Bloch", 2018);
    private final Book cleanCode = new Book(2L, "Clean Code", "Robert Martin", 2008);

    @Test
    void withoutAuthorFilterReturnsAllBooksFromRepository() {
        given(repository.findAll()).willReturn(List.of(effectiveJava, cleanCode));

        var result = service.findAll(null);

        assertThat(result).containsExactly(effectiveJava, cleanCode);
    }

    @Test
    void unknownIdGivesEmptyOptional() {
        given(repository.findById(99L)).willReturn(Optional.empty());

        assertThat(service.findById(99L)).isEmpty();
    }

    @Test
    void creatingABookPassesItToTheRepositoryUnchanged() {
        var newBook = new Book(null, "Dune", "Frank Herbert", 1965);
        given(repository.save(any(Book.class)))
                .willReturn(new Book(4L, "Dune", "Frank Herbert", 1965));

        var created = service.create(newBook);

        var captor = ArgumentCaptor.forClass(Book.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().title()).isEqualTo("Dune");
        assertThat(captor.getValue().id()).isNull();
        assertThat(created.id()).isEqualTo(4L);
    }

    @Test
    void searchByAuthorWithNoMatchReturnsEmptyList() {
        given(repository.findAll()).willReturn(List.of(effectiveJava, cleanCode));

        var result = service.findAll("nobody");

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void blankAuthorFilterIsTreatedAsNoFilter() {
        given(repository.findAll()).willReturn(List.of(effectiveJava, cleanCode));

        var result = service.findAll("   ");

        assertThat(result).containsExactly(effectiveJava, cleanCode);
    }
}