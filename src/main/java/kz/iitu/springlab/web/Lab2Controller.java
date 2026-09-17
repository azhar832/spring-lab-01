package kz.iitu.springlab.web;

import kz.iitu.springlab.notify.NotificationService;
import kz.iitu.springlab.scope.TicketOffice;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/lab2")
public class Lab2Controller {

    private final NotificationService notifications;
    private final DateTimeFormatter isoFormatter;
    private final TicketOffice ticketOffice;

    public Lab2Controller(NotificationService notifications,
                          DateTimeFormatter isoFormatter,
                          TicketOffice ticketOffice) {
        this.notifications = notifications;
        this.isoFormatter = isoFormatter;
        this.ticketOffice = ticketOffice;
    }

    @GetMapping("/notify")
    public Map<String, Object> notify(@RequestParam(defaultValue = "Hello") String text) {
        return Map.of(
                "primary", notifications.viaPrimary(text),
                "console", notifications.viaConsole(text),
                "all", notifications.viaAll(text),
                "beanNames", notifications.names()
        );
    }

    @GetMapping("/format")
    public String formatCurrentTime() {
        return isoFormatter.format(OffsetDateTime.now());
    }

    @GetMapping("/scopes")
    public Map<String, Object> scopes() {
        return Map.of(
                "injectedDirectly", ticketOffice.getDirectTicketId(),
                "viaProvider", ticketOffice.getProviderTicketId(),
                "office", System.identityHashCode(ticketOffice)
        );
    }
}