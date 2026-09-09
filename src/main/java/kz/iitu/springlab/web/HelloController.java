package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors());
    }

    @GetMapping("/wordcount")
    public WordCountResult wordCount(@RequestParam(required = false) String text) {
        if (text == null || text.isBlank()) {
            return new WordCountResult(0, 0, "", "Parameter 'text' is missing or empty");
        }
        String trimmed = text.trim();
        String[] words = trimmed.split("\\s+");
        int wordCount = words.length;
        int charCount = trimmed.length();
        String longest = "";
        for (String word : words) {
            if (word.length() > longest.length()) {
                longest = word;
            }
        }
        return new WordCountResult(wordCount, charCount, longest, null);
    }

    public record Greeting(String message, String owner, LocalDateTime timestamp) { }
    public record Info(String owner, String javaVersion, int cpuCores) { }
    public record WordCountResult(int wordCount, int charCount, String longestWord, String error) { }
}