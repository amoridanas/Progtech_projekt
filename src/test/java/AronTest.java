import org.junit.jupiter.api.Test;
import ticketing.Factory.RequestTicketFactory;
import ticketing.Factory.TicketAbstractFactory;
import ticketing.Model.Priority;
import ticketing.Model.Ticket;
import ticketing.Repository.TicketRepo;
import ticketing.Strategy.Priority.HighPriorityStrategy;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AronTest {

    @Test
    void testRequestTicketFactoryCreatesCorrectType() {
        TicketAbstractFactory factory = new RequestTicketFactory();
        Ticket ticket = factory.create("Teszt jegy", "Ez egy teszt leírás", Priority.LOW, 1L);

        assertNotNull(ticket);
        assertEquals("REQUEST", ticket.getType());
        assertEquals("LOW", ticket.getPriority());
    }

    @Test
    void testHighPriorityStrategyDueCalculation() {
        LocalDateTime created = LocalDateTime.of(2024, 12, 1, 10, 0);
        HighPriorityStrategy strategy = new HighPriorityStrategy();

        LocalDateTime expected = created.plusDays(1);
        LocalDateTime actual = strategy.calculateDueDate(created);

        assertEquals(expected, actual);
    }

    @Test
    void testTicketRepoGetAllReturnsList() {
        List<Ticket> tickets = TicketRepo.getAll();
        assertNotNull(tickets);
        assertTrue(tickets.size() >= 0);
    }

    @Test
    void testCreatedTicketHasDefaultStatus() {
        Ticket ticket = new Ticket("Cím", "Leírás", "LOW", 1L);
        assertEquals("NEW", ticket.getStatus());
    }

    @Test
    void testCriticalPriorityHasDueDate() {
        Ticket ticket = new Ticket("Cím", "Leírás", "CRITICAL", 1L);
        assertNotNull(ticket.getDue());
    }
}