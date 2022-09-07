package persistentdatatests;

import com.webapi.webapi.databasedrivers.hibernateinmemory.HibernateConnection;
import com.webapi.webapi.model.candidate.Candidate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestGenericHibernateMethodsViaCandidate {

    private static Long id = null;
    private static Long testOrder = 0L;
    private static final Logger logger = LogManager.getLogger(TestGenericHibernateMethodsViaCandidate.class);

    @Test
    @Order(1)
    public void testSave() throws ClassNotFoundException {
        Candidate chef = new Candidate("chef");
        HibernateConnection.genericSave(chef);
        id = chef.getId();
        logger.info(++testOrder);
    }

    @Test
    @Order(2)
    public void testGet() throws ClassNotFoundException {
        Candidate retrieved = HibernateConnection.
                genericGetByClassAndID(Candidate.class, id);
        assertEquals(retrieved.getName(), "chef");
        logger.info(++testOrder);
    }

    @Test
    @Order(3)
    public void testUpdate() throws ClassNotFoundException {
        Candidate retrieved = HibernateConnection.
                genericGetByClassAndID(Candidate.class, id);
        assertEquals(retrieved.getName(), "chef");
        retrieved.setName("jeff");
        HibernateConnection.genericUpdate(retrieved);
        Candidate retrievedAgain = HibernateConnection.
                genericGetByClassAndID(Candidate.class, id);
        assert (Objects.equals(retrievedAgain.getName(), "jeff"));
        logger.info(++testOrder);
    }

    @Test
    @Order(4)
    public void testDelete() throws ClassNotFoundException {
        Candidate retrieved = HibernateConnection.
                genericGetByClassAndID(Candidate.class, id);
        assertEquals(retrieved.getName(), "jeff");
        HibernateConnection.genericDelete(retrieved);
        Candidate retrievedAgain = HibernateConnection.
                genericGetByClassAndID(Candidate.class, id);
        assert (retrievedAgain == null);
        logger.info(++testOrder);
    }

    @Test
    @Order(5)
    public void testCandidatesByName() {
        Candidate judeaDumont = new Candidate("Judea Dumont");
        HibernateConnection.genericSave(judeaDumont);

        List<Candidate> candidatesByName = HibernateConnection.genericGetByName(Candidate.class, "Judea Dumont");
        for (Candidate person : candidatesByName) {
            System.out.println("You want to hire " + person.getName());
        }
        logger.info(++testOrder);
    }
}
