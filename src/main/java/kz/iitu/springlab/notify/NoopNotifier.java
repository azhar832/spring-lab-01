package kz.iitu.springlab.notify;

import org.springframework.stereotype.Component;

@Component
public class NoopNotifier implements Notifier {

    @Override
    public String notify(String message) {
        return "NOOP: " + message;
    }
}