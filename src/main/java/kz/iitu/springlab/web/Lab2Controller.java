package kz.iitu.springlab.web;

import kz.iitu.springlab.lifecycle.LifecycleDemo;
import kz.iitu.springlab.notify.NotificationService;
import kz.iitu.springlab.scope.TicketOffice;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lab2")
public class Lab2Controller {

    private final NotificationService notificationService;
    private final TicketOffice ticketOffice;
    private final LifecycleDemo lifecycleDemo;

    public Lab2Controller(NotificationService notificationService,
                          TicketOffice ticketOffice,
                          LifecycleDemo lifecycleDemo) {
        this.notificationService = notificationService;
        this.ticketOffice = ticketOffice;
        this.lifecycleDemo = lifecycleDemo;
    }

    @GetMapping("/notify")
    public Map<String, Object> notifyDemo() {
        return Map.of(
                "primary", notificationService.sendViaPrimary("Hello Primary"),
                "qualified", notificationService.sendViaQualified("Hello Qualified"),
                "all", notificationService.sendViaAll("Hello All")
        );
    }

    @GetMapping("/custom")
    public String customEndpoint(@RequestParam(defaultValue = "Default Text") String text) {
        return notificationService.sendViaCustom(text);
    }

    @GetMapping("/scopes")
    public Map<String, String> scopesDemo() {
        return Map.of(
                "directTicketId", ticketOffice.getDirectTicketId(),
                "providerTicketId", ticketOffice.getProviderTicketId()
        );
    }

    @GetMapping("/lifecycle")
    public List<String> lifecycleDemo() {
        return lifecycleDemo.getEvents();
    }
}