package persistentdatatests;

import com.webapi.webapi.databasedrivers.hibernateinmemory.HibernateConnectionGenericMethods;
import com.webapi.webapi.model.candidate.Candidate;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

//todo: fix these tests such that they can be ran individually and have impact/left over on data if successful

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestGenericHibernateMethodsViaCandidate {

    private static Long id = null;
    private static Long testOrder = 0L;

    @Test
    @Order(1)
    public void testSave() throws ClassNotFoundException {
        Candidate chef = new Candidate("chef");
        HibernateConnectionGenericMethods.genericSave(chef);
        id = chef.getId();
    }

    @Test
    @Order(2)
    public void testGet() throws ClassNotFoundException {
        Candidate retrieved = HibernateConnectionGenericMethods.
                genericGetByClassAndID(Candidate.class, id);
        assertEquals(retrieved.getName(), "chef");
    }

    @Test
    @Order(3)
    public void testUpdate() throws ClassNotFoundException {
        Candidate retrieved = HibernateConnectionGenericMethods.
                genericGetByClassAndID(Candidate.class, id);
        assertEquals(retrieved.getName(), "chef");
        retrieved.setName("jeff");
        HibernateConnectionGenericMethods.genericUpdate(retrieved);
        Candidate retrievedAgain = HibernateConnectionGenericMethods.
                genericGetByClassAndID(Candidate.class, id);
        assert (Objects.equals(retrievedAgain.getName(), "jeff"));
    }

    @Test
    @Order(4)
    public void testDelete() throws ClassNotFoundException {
        Candidate retrieved = HibernateConnectionGenericMethods.
                genericGetByClassAndID(Candidate.class, id);
        assertEquals(retrieved.getName(), "jeff");
        HibernateConnectionGenericMethods.genericDelete(retrieved);
        Candidate retrievedAgain = HibernateConnectionGenericMethods.
                genericGetByClassAndID(Candidate.class, id);
        assert (retrievedAgain == null);
    }

    @Test
    @Order(5)
    public void testCandidatesByName() {
        Candidate judeaDumont = new Candidate("Judea Dumont");
        HibernateConnectionGenericMethods.genericSave(judeaDumont);

        List<Candidate> candidatesByName = HibernateConnectionGenericMethods.genericGetByName(Candidate.class, "Judea Dumont");
        for (Candidate person : candidatesByName) {
            System.out.println("You want to hire " + person.getName());
        }
    }
}
