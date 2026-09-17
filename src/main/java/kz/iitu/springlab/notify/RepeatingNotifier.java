package kz.iitu.springlab.notify;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("repeating")
@Order(3)
public class RepeatingNotifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(RepeatingNotifier.class);

    @Value("${notifier.repeat-count:2}")
    private int repeatCount;

    @PostConstruct
    public void init() {
        log.info("RepeatingNotifier initialized with repeatCount = {}", repeatCount);
    }

    @Override
    public String send(String message) {
        if (message == null) {
            return "";
        }
        String result = message.repeat(repeatCount);
        log.info("REPEATING >> {}", result);
        return "repeating: " + result;
    }

    @Override
    public String channel() {
        return "repeating";
    }
}