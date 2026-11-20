package kz.iitu.springlab.catalog;

import jakarta.validation.constraints.NotBlank;

public record Book(Long id, @NotBlank String title, String author, int year) { }