package kz.iitu.springlab.config;

import kz.iitu.springlab.notify.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
public class ContainerReport implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ContainerReport.class);

    private final ApplicationContext context;
    private final Map<String, Notifier> notifiers;

    public ContainerReport(ApplicationContext context, Map<String, Notifier> notifiers) {
        this.context = context;
        this.notifiers = notifiers;
    }

    @Override
    public void run(String... args) {
        log.info("=== CONTAINER REPORT ===");
        log.info("Total bean definitions count: {}", context.getBeanDefinitionCount());
        log.info("Notifier implementations count: {}", notifiers.size());

        log.info("Beans in package kz.iitu.springlab:");
        Arrays.stream(context.getBeanDefinitionNames())
                .filter(name -> context.getBean(name).getClass().getPackageName().startsWith("kz.iitu.springlab"))
                .forEach(name -> log.info(" -> Bean name: {}", name));
        log.info("========================");
    }
}
