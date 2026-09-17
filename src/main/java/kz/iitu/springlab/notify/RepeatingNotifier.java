package kz.iitu.springlab.notify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("repeatingNotifier")
public class RepeatingNotifier implements Notifier {

    @Value("${lab2.repeating.count:3}")
    private int count;

    @Override
    public String notify(String message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(message).append(" ");
        }
        return "Repeating (" + count + "): " + sb.toString().trim();
    }
}