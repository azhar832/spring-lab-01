package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService service;

    public BookRestController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Map<String, Object>> list(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "10") int limit,
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String lang) {
        String[] names = fieldNames(lang);
        return service.findAll(author).stream()
                .limit(limit)
                .map(book -> toLocalized(book, names))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> find(@PathVariable long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book saved = service.create(book);
        return ResponseEntity
                .created(URI.create("/api/books/" + saved.id()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> replace(@PathVariable long id, @RequestBody Book book) {
        return service.findById(id)
                .map(existing -> {
                    Book updated = new Book(id, book.title(), book.author(), book.year());
                    service.create(updated);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // picks the field names by the beginning of the Accept-Language header
    private static String[] fieldNames(String acceptLanguage) {
        String code = acceptLanguage.trim().toLowerCase();
        if (code.startsWith("ru")) {
            return new String[]{"id", "название", "автор", "год"};
        }
        if (code.startsWith("kk")) {
            return new String[]{"id", "атауы", "автор", "жылы"};
        }
        return new String[]{"id", "title", "author", "year"}; // default: English
    }

    private static Map<String, Object> toLocalized(Book book, String[] names) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(names[0], book.id());
        map.put(names[1], book.title());
        map.put(names[2], book.author());
        map.put(names[3], book.year());
        return map;
    }
}