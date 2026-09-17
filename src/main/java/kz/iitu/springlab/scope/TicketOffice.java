package kz.iitu.springlab.scope;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class TicketOffice {
    private final Ticket directTicket;
    private final ObjectProvider<Ticket> ticketProvider;

    public TicketOffice(Ticket directTicket, ObjectProvider<Ticket> ticketProvider) {
        this.directTicket = directTicket;
        this.ticketProvider = ticketProvider;
    }

    public String getDirectTicketId() {
        return directTicket.getId();
    }

    public String getProviderTicketId() {
        return ticketProvider.getObject().getId();
    }
}