package ticketing.Observer;

import ticketing.Model.Ticket;

public class NotificationObserver implements TicketObserver {
    private final NotificationService notificationService;

    public NotificationObserver(NotificationService service) {
        this.notificationService = service;
    }

    @Override
    public void update(Ticket ticket, String message) {
        notificationService.addNotification(ticket.getAssignedTo(), message);
    }
}