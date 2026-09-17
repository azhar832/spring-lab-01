package kz.iitu.springlab.notify;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final Notifier primaryNotifier;
    private final Notifier qualifiedNotifier;
    private final List<Notifier> allNotifiers;
    private final Map<String, Notifier> notifierMap;
    private final Notifier repeatingNotifier;

    public NotificationService(
            Notifier primaryNotifier,
            @Qualifier("emailNotifier") Notifier qualifiedNotifier,
            List<Notifier> allNotifiers,
            Map<String, Notifier> notifierMap,
            @Qualifier("repeatingNotifier") Notifier repeatingNotifier) {
        this.primaryNotifier = primaryNotifier;
        this.qualifiedNotifier = qualifiedNotifier;
        this.allNotifiers = allNotifiers;
        this.notifierMap = notifierMap;
        this.repeatingNotifier = repeatingNotifier;
    }

    public String sendViaPrimary(String msg) {
        return primaryNotifier.notify(msg);
    }

    public String sendViaQualified(String msg) {
        return qualifiedNotifier.notify(msg);
    }

    public List<String> sendViaAll(String msg) {
        return allNotifiers.stream()
                .map(n -> n.notify(msg))
                .collect(Collectors.toList());
    }

    public String sendViaCustom(String msg) {
        return repeatingNotifier.notify(msg);
    }

    public Map<String, Notifier> getNotifierMap() {
        return notifierMap;
    }

    // Добавь этот метод для получения списка имён бинов:
    public Set<String> getBeanNames() {
        return notifierMap.keySet();
    }
}