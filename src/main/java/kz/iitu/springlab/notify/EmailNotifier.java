package kz.iitu.springlab.notify;

import org.springframework.stereotype.Component;

@Component("emailNotifier")
public class EmailNotifier implements Notifier {

    @Override
    public String notify(String message) {
        return "EMAIL: " + message;
    }
}