package kz.iitu.springlab.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LifecycleDemo {

    private static final Logger log = LoggerFactory.getLogger(LifecycleDemo.class);

    public LifecycleDemo() {
        log.info("1. LifecycleDemo constructor called");
    }

    @PostConstruct
    public void init() {
        log.info("2. LifecycleDemo @PostConstruct method called");
    }

    @PreDestroy
    public void cleanup() {
        log.info("3. LifecycleDemo @PreDestroy method called");
    }
}
