package kz.iitu.springlab.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookRestController.class)
@WithMockUser(roles = "ADMIN")
class BookRestControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean BookService service;

    @Test
    void existingBookIsReturnedWithItsTitle() throws Exception {
        given(service.findById(1L))
                .willReturn(Optional.of(new Book(1L, "Effective Java", "Joshua Bloch", 2018)));

        mockMvc.perform(get("/api/books/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Effective Java"));
    }

    @Test
    void missingBookBecomes404() throws Exception {
        given(service.findById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/books/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void invalidBodyBecomes400() throws Exception {
        mockMvc.perform(post("/api/books")
                       .with(csrf())
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"title\":\"\",\"author\":\"X\",\"year\":2000}"))
               .andExpect(status().isBadRequest());
    }
}