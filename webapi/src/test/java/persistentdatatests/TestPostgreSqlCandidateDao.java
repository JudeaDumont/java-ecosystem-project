package persistentdatatests;

import com.webapi.webapi.databasedrivers.Dao;
import com.webapi.webapi.databasedrivers.postgres.PostgreSqlCandidateDaoService;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Objects;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPostgreSqlCandidateDao {

    public static Candidate savedCandidate = null;
    public static Candidate updatedCandidate = null;

    private static final Dao<Candidate, Long> candidateDAO = new PostgreSqlCandidateDaoService();

    @Test
    @Order(1)
    public void testSave() {
        Candidate c = new Candidate(
                1L,
                "chef");
        assert (candidateDAO.save(c) != 0);
        savedCandidate = c;
    }

    //todo: the idea that tests have to run in order for later tests to work is a bad idea
    @Test
    @Order(2)
    public void testGetCandidateAndGeneratedIdPropagation() throws NonExistentCandidateException {
        assert (Objects.equals(candidateDAO.get(savedCandidate.getId()).getId(), savedCandidate.getId()));
    }

    @Test
    @Order(3)
    public void testUpdate() {

        Candidate firstCandidate = new Candidate(2L, "Manuel");
        assert (candidateDAO.save(firstCandidate) != 0);
        updatedCandidate = firstCandidate;

        firstCandidate.setName("Franklin");
        assert (candidateDAO.update(firstCandidate));
    }

    @Test
    @Order(4)
    public void testDelete() {

        assert (candidateDAO.delete(savedCandidate));
        assert (candidateDAO.delete(updatedCandidate));
    }

    @Test
    @Order(5)
    public void testPrintCandidates() {
        candidateDAO.getAll().forEach(System.out::println);
    }
}
