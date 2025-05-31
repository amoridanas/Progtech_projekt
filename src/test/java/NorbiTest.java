import org.junit.jupiter.api.Test;
import ticketing.Factory.FailureTicketFactory;
import ticketing.Factory.TicketAbstractFactory;
import ticketing.Model.Priority;
import ticketing.Model.Ticket;
import ticketing.Repository.TicketRepo;
import ticketing.Strategy.Priority.LowPriorityStrategy;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NorbiTest {
    @Test
    void testFailureTicketFactoryCreatesCorrectType() {
        TicketAbstractFactory factory = new FailureTicketFactory();
        Ticket ticket = factory.create("Hiba jegy", "Valami elromlott", Priority.CRITICAL, 1L);

        assertEquals("FAILURE", ticket.getType());
        assertNotNull(ticket.getDue()); // CRITICAL miatt due-t állít
    }

    @Test
    void testSetTypeMethod() {
        Ticket ticket = new Ticket("Cím", "Leírás", "LOW", 1L);
        ticket.setType("REQUEST");
        assertEquals("REQUEST", ticket.getType());
    }

    @Test
    void testGetByIdReturnsTicket() {
        List<Ticket> all = TicketRepo.getAll();
        if (!all.isEmpty()) {
            Ticket ticket = TicketRepo.getById(all.get(0).getId());
            assertNotNull(ticket);
            assertEquals(all.get(0).getId(), ticket.getId());
        } else {
            System.out.println("Nincs elérhető jegy a teszteléshez.");
        }
    }

    @Test
    void testLowPriorityDueDate() {
        LocalDateTime now = LocalDateTime.now();
        LowPriorityStrategy strategy = new LowPriorityStrategy();
        assertEquals(now.plusWeeks(1), strategy.calculateDueDate(now));
    }

    @Test
    void testTicketUpdatePersistsChanges() {
        List<Ticket> tickets = TicketRepo.getAll();
        if (!tickets.isEmpty()) {
            Ticket t = tickets.get(0);
            String originalTitle = t.getTitle();
            t.setTitle("Teszt Módosítás");

            boolean result = TicketRepo.update(t);
            assertTrue(result);

            Ticket updated = TicketRepo.getById(t.getId());
            assertEquals("Teszt Módosítás", updated.getTitle());

            // visszaállítás eredeti címre
            t.setTitle(originalTitle);
            TicketRepo.update(t);
        } else {
            System.out.println("Nincs jegy az update teszthez.");
        }
    }
}
