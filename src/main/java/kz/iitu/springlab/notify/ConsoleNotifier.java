package kz.iitu.springlab.notify;

import org.springframework.context.annotation.Primary; // <-- добавь этот импорт
import org.springframework.stereotype.Component;

@Primary // <-- добавь эту аннотацию
@Component
public class ConsoleNotifier implements Notifier {

    @Override
    public String notify(String message) {
        return "CONSOLE: " + message;
    }
}