package kz.iitu.springlab.controller;

import kz.iitu.springlab.service.CatalogService;
import org.springframework.aop.support.AopUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lab4")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/proxy")
    public Map<String, Object> proxyInfo() {
        Class<?> clazz = catalogService.getClass();
        Map<String, Object> info = new HashMap<>();
        info.put("className", clazz.getName());
        info.put("superClass", clazz.getSuperclass().getName());
        info.put("isAopProxy", AopUtils.isAopProxy(catalogService));
        info.put("isCglib", AopUtils.isCglibProxy(catalogService));
        return info;
    }

    @GetMapping("/item/{id}")
    public String getItem(@PathVariable long id) {
        return catalogService.findById(id);
    }

    @GetMapping("/items")
    public List<String> getItems(@RequestParam(defaultValue = "3") int limit) {
        return catalogService.findAll(limit);
    }

    @DeleteMapping("/item/{id}")
    public String deleteItem(@PathVariable long id) {
        return catalogService.remove(id);
    }

    // Удобный дублер для быстрого GET-запроса из браузера для Скриншота 9
    @GetMapping("/delete/{id}")
    public String deleteItemGet(@PathVariable long id) {
        return catalogService.remove(id);
    }

    @GetMapping("/remove-twice/{id}")
    public String removeTwice(@PathVariable long id) {
        catalogService.removeTwice(id);
        return "Attempted to remove twice item no. " + id;
    }
}