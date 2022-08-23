package persistentdatatests;

import data.candidate.Candidate;
import data.hibernateinmemory.HibernateInMemorySessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestHibernateCandidates {

    private static Long id = null;
    private static Long testOrder = 0L;
    private static final Logger logger = LogManager.getLogger(TestHibernateCandidates.class);

    @Test
    @Order(1)
    public void testSave() throws ClassNotFoundException {
        Candidate chef = new Candidate("chef");
        HibernateInMemorySessionFactory.save(chef);
        id = chef.getId();
        logger.info(++testOrder);
    }

    @Test
    @Order(2)
    public void testGet() throws ClassNotFoundException {
        Candidate retrieved = HibernateInMemorySessionFactory.
                getByClassAndID(Candidate.class, id);
        assertEquals(retrieved.getName(), "chef");
        logger.info(++testOrder);
    }

    @Test
    @Order(3)
    public void testCandidatesByName() {
        Candidate judeaDumont = new Candidate(9123L, "Judea Dumont");
        HibernateInMemorySessionFactory.save(judeaDumont);

        List<Candidate> candidatesByName = HibernateInMemorySessionFactory.getByName(Candidate.class, "Judea Dumont");
        for (Candidate person : candidatesByName) {
            System.out.println("You want to hire " + person.getName());
        }
        logger.info(++testOrder);
    }
}
