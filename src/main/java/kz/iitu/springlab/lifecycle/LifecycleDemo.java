package kz.iitu.springlab.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LifecycleDemo {

    private final List<String> events = new ArrayList<>();

    public LifecycleDemo() {
        events.add("1. Constructor called");
    }

    @PostConstruct
    public void init() {
        events.add("2. @PostConstruct method called");
    }

    @PreDestroy
    public void destroy() {
        events.add("3. @PreDestroy method called");
    }

    public List<String> getEvents() {
        return events;
    }
}
